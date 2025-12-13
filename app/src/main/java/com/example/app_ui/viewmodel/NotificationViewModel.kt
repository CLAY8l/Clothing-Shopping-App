package com.example.app_ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_ui.data.entity.Notification
import com.example.app_ui.data.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NotificationUiState(
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class NotificationViewModel(
    private val repository: NotificationRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()
    
    val allNotifications: Flow<List<Notification>> = repository.getAllNotifications()
    val unreadNotifications: Flow<List<Notification>> = repository.getUnreadNotifications()
    val unreadCount: Flow<Int> = repository.getUnreadCount()
    
    init {
        observeNotifications()
    }
    
    private fun observeNotifications() {
        viewModelScope.launch {
            allNotifications.collect { notifications ->
                _uiState.value = _uiState.value.copy(notifications = notifications)
            }
        }
        
        viewModelScope.launch {
            unreadCount.collect { count ->
                _uiState.value = _uiState.value.copy(unreadCount = count)
            }
        }
    }
    
    fun addNotification(title: String, message: String, type: String) {
        viewModelScope.launch {
            try {
                val notification = Notification(
                    title = title,
                    message = message,
                    type = type
                )
                repository.insertNotification(notification)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to add notification: ${e.message}"
                )
            }
        }
    }
    
    fun deleteNotification(notification: Notification) {
        viewModelScope.launch {
            try {
                repository.deleteNotification(notification)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to delete notification: ${e.message}"
                )
            }
        }
    }
    
    fun deleteNotificationById(notificationId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteNotificationById(notificationId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to delete notification: ${e.message}"
                )
            }
        }
    }
    
    fun markAsRead(notificationId: Long) {
        viewModelScope.launch {
            repository.markAsRead(notificationId)
        }
    }
    
    fun markAllAsRead() {
        viewModelScope.launch {
            repository.markAllAsRead()
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

class NotificationViewModelFactory(
    private val repository: NotificationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

