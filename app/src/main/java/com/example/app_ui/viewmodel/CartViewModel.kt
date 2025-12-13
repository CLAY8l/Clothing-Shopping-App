package com.example.app_ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_ui.data.entity.ProductCart
import com.example.app_ui.data.repository.CartRepository
import com.example.app_ui.data.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val notificationRepository: NotificationRepository? = null
) : ViewModel() {
    val allCartItems: Flow<List<ProductCart>> = cartRepository.getAllCartItems()
    val cartItemCount: Flow<Int> = cartRepository.getCartItemCount()
    val cartTotal: Flow<Double?> = cartRepository.getCartTotal()
    
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()
    
    fun addCartItem(
        productId: String,
        name: String,
        description: String?,
        price: Double,
        imageUrl: String?,
        quantity: Int = 1
    ) {
        viewModelScope.launch {
            try {
                cartRepository.addOrUpdateCartItem(
                    productId = productId,
                    name = name,
                    description = description,
                    price = price,
                    imageUrl = imageUrl,
                    quantity = quantity
                )
                
                // Create notification when product is added to cart
                notificationRepository?.let { repo ->
                    val notification = com.example.app_ui.data.entity.Notification(
                        title = "Added to Cart",
                        message = "$name has been added to your cart",
                        type = "cart_added"
                    )
                    repo.insertNotification(notification)
                }
                
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
    
    fun updateCartItemQuantity(item: ProductCart, newQuantity: Int) {
        viewModelScope.launch {
            try {
                if (newQuantity > 0) {
                    cartRepository.updateCartItem(item.copy(quantity = newQuantity))
                } else {
                    cartRepository.deleteCartItem(item)
                }
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
    
    fun removeCartItem(item: ProductCart) {
        viewModelScope.launch {
            try {
                cartRepository.deleteCartItem(item)
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
    
    fun removeCartItemById(id: Long) {
        viewModelScope.launch {
            try {
                cartRepository.deleteCartItemById(id)
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
    
    fun clearCart() {
        viewModelScope.launch {
            try {
                cartRepository.deleteAllCartItems()
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
    
    fun isProductInCart(productId: String): Flow<Boolean> = 
        cartRepository.isProductInCartFlow(productId)
}

data class CartUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CartViewModelFactory(
    private val cartRepository: CartRepository,
    private val notificationRepository: NotificationRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartRepository, notificationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

