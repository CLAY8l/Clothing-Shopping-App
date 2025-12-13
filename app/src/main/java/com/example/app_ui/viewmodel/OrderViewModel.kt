package com.example.app_ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_ui.data.entity.Order
import com.example.app_ui.data.entity.OrderItem
import com.example.app_ui.data.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {
    val allOrders: Flow<List<Order>> = orderRepository.getAllOrders()
    val orderCount: Flow<Int> = orderRepository.getOrderCount()
    
    private val _uiState = MutableStateFlow<OrderUiState>(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()
    
    fun createOrder(
        userId: String?,
        deliveryAddress: String,
        paymentMethod: String,
        cardNumber: String?,
        promoCode: String?,
        subtotal: Double,
        vat: Double,
        shippingFee: Double,
        total: Double,
        items: List<OrderItem>
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                val orderId = orderRepository.createOrder(
                    userId = userId,
                    deliveryAddress = deliveryAddress,
                    paymentMethod = paymentMethod,
                    cardNumber = cardNumber,
                    promoCode = promoCode,
                    subtotal = subtotal,
                    vat = vat,
                    shippingFee = shippingFee,
                    total = total,
                    items = items
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    lastCreatedOrderId = orderId
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to create order"
                )
            }
        }
    }
    
    suspend fun getOrderById(orderId: Long): Order? = orderRepository.getOrderById(orderId)
    
    suspend fun getOrderByOrderNumber(orderNumber: String): Order? = 
        orderRepository.getOrderByOrderNumber(orderNumber)
    
    fun getOrdersByUserId(userId: String): Flow<List<Order>> = 
        orderRepository.getOrdersByUserId(userId)
    
    suspend fun getOrderItemsByOrderId(orderId: Long): List<OrderItem> = 
        orderRepository.getOrderItemsByOrderId(orderId)
    
    fun getOrderItemsByOrderIdFlow(orderId: Long): Flow<List<OrderItem>> = 
        orderRepository.getOrderItemsByOrderIdFlow(orderId)
    
    fun updateOrder(order: Order) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                orderRepository.updateOrder(order)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to update order"
                )
            }
        }
    }
    
    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                orderRepository.deleteOrder(order)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to delete order"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class OrderUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val lastCreatedOrderId: Long? = null
)

class OrderViewModelFactory(
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

