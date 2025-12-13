package com.example.app_ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_ui.data.entity.User
import com.example.app_ui.data.firebase.FirestoreService
import com.example.app_ui.data.repository.UserRepository
import com.example.app_ui.utils.FirebaseMigrationHelper
import com.example.app_ui.utils.FirebaseUserHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class UserViewModel(
    private val userRepository: UserRepository,
    private val context: Context? = null
) : ViewModel() {
    val currentUser: Flow<User?> = userRepository.getCurrentUser()
    
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                // Check if user already exists
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Account with this email already exists. Please login instead.",
                        isSignUpSuccess = false
                    )
                    return@launch
                }
                
                // Create new user
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    email = email,
                    name = name,
                    password = password
                )
                
                userRepository.insertUser(newUser)
                
                // Save user ID for Firestore operations
                context?.let {
                    FirebaseUserHelper.setCurrentUserId(it, newUser.id)
                    FirebaseUserHelper.setCurrentUserEmail(it, newUser.email)
                    
                    // Save user data to Firestore
                    try {
                        val userData = mapOf(
                            "id" to newUser.id,
                            "email" to newUser.email,
                            "name" to newUser.name,
                            "createdAt" to newUser.createdAt
                        )
                        FirestoreService.saveUser(newUser.id, userData)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Continue even if Firestore save fails
                    }
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    isSignUpSuccess = true,
                    currentUser = newUser
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to create account",
                    isSignUpSuccess = false
                )
            }
        }
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                // Check if user exists
                val user = userRepository.getUserByEmail(email)
                
                if (user == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Account not found. Please sign up first.",
                        isLoginSuccess = false
                    )
                    return@launch
                }
                
                // Verify password
                if (user.password != password) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Incorrect password. Please try again.",
                        isLoginSuccess = false
                    )
                    return@launch
                }
                
                // Save user ID for Firestore operations
                context?.let {
                    FirebaseUserHelper.setCurrentUserId(it, user.id)
                    FirebaseUserHelper.setCurrentUserEmail(it, user.email)
                    
                    // Optionally migrate existing Room data to Firestore
                    try {
                        FirebaseMigrationHelper.migrateToFirebase(it, user.id)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Continue even if migration fails
                    }
                }
                
                // Login successful
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    isLoginSuccess = true,
                    currentUser = user
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to login",
                    isLoginSuccess = false
                )
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            try {
                // Clear Firebase user data
                context?.let {
                    FirebaseUserHelper.clearUserData(it)
                }
                
                // Clear UI state
                _uiState.value = AuthUiState()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to logout"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false,
    val isSignUpSuccess: Boolean = false,
    val currentUser: User? = null
)

class UserViewModelFactory(
    private val userRepository: UserRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

