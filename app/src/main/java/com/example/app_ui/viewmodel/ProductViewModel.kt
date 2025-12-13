package com.example.app_ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_ui.data.repository.ProductRepository
import com.example.app_ui.screens.ProductUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _products = MutableStateFlow<List<ProductUi>>(emptyList())
    val products: StateFlow<List<ProductUi>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                val fetchedProducts = productRepository.getMensClothingProducts(limit = 4)
                _products.value = fetchedProducts
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load products"
                // Fallback to local products on error
                _products.value = com.example.app_ui.screens.sampleProducts.take(4)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshProducts() {
        loadProducts()
    }
}

class ProductViewModelFactory(private val productRepository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

