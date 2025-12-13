package com.example.app_ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_ui.data.entity.FavoriteProduct
import com.example.app_ui.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    val allFavorites: Flow<List<FavoriteProduct>> = favoriteRepository.getAllFavorites()
    val favoriteCount: Flow<Int> = favoriteRepository.getFavoriteCount()
    
    private val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()
    
    fun addFavorite(
        productId: String,
        name: String,
        description: String?,
        price: Double,
        imageUrl: String?
    ) {
        viewModelScope.launch {
            try {
                favoriteRepository.insertFavorite(
                    FavoriteProduct(
                        productId = productId,
                        name = name,
                        description = description,
                        price = price,
                        imageUrl = imageUrl
                    )
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    fun removeFavorite(favorite: FavoriteProduct) {
        viewModelScope.launch {
            try {
                favoriteRepository.deleteFavorite(favorite)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    fun removeFavoriteByProductId(productId: String) {
        viewModelScope.launch {
            try {
                favoriteRepository.deleteFavoriteByProductId(productId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    fun toggleFavorite(
        productId: String,
        name: String,
        description: String?,
        price: Double,
        imageUrl: String?
    ) {
        viewModelScope.launch {
            try {
                favoriteRepository.toggleFavorite(
                    productId = productId,
                    name = name,
                    description = description,
                    price = price,
                    imageUrl = imageUrl
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    fun clearFavorites() {
        viewModelScope.launch {
            try {
                favoriteRepository.deleteAllFavorites()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    fun isProductFavorite(productId: String): Flow<Boolean> = 
        favoriteRepository.isProductFavoriteFlow(productId)
}

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class FavoriteViewModelFactory(
    private val favoriteRepository: FavoriteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(favoriteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

