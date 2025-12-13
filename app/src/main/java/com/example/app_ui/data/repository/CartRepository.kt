package com.example.app_ui.data.repository

import com.example.app_ui.data.dao.ProductCartDao
import com.example.app_ui.data.entity.ProductCart
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: ProductCartDao) {
    fun getAllCartItems(): Flow<List<ProductCart>> = cartDao.getAllCartItems()
    
    suspend fun getCartItemById(id: Long): ProductCart? = cartDao.getCartItemById(id)
    
    suspend fun getCartItemByProductId(productId: String): ProductCart? = 
        cartDao.getCartItemByProductId(productId)
    
    fun getCartItemByProductIdFlow(productId: String): Flow<ProductCart?> = 
        cartDao.getCartItemByProductIdFlow(productId)
    
    suspend fun insertCartItem(item: ProductCart): Long = cartDao.insertCartItem(item)
    
    suspend fun updateCartItem(item: ProductCart) = cartDao.updateCartItem(item)
    
    suspend fun deleteCartItem(item: ProductCart) = cartDao.deleteCartItem(item)
    
    suspend fun deleteCartItemById(id: Long) = cartDao.deleteCartItemById(id)
    
    suspend fun deleteCartItemByProductId(productId: String) = 
        cartDao.deleteCartItemByProductId(productId)
    
    suspend fun deleteAllCartItems() = cartDao.deleteAllCartItems()
    
    fun getCartItemCount(): Flow<Int> = cartDao.getCartItemCount()
    
    fun getCartTotal(): Flow<Double?> = cartDao.getCartTotal()
    
    suspend fun isProductInCart(productId: String): Boolean = 
        cartDao.isProductInCart(productId)
    
    fun isProductInCartFlow(productId: String): Flow<Boolean> = 
        cartDao.isProductInCartFlow(productId)
    
    // Bulk operations
    suspend fun insertCartItems(items: List<ProductCart>): List<Long> = 
        cartDao.insertCartItems(items)
    
    suspend fun updateCartItems(items: List<ProductCart>) = cartDao.updateCartItems(items)
    
    suspend fun deleteCartItems(items: List<ProductCart>) = cartDao.deleteCartItems(items)
    
    // Helper function to add or update quantity
    suspend fun addOrUpdateCartItem(
        productId: String,
        name: String,
        description: String?,
        price: Double,
        imageUrl: String?,
        quantity: Int = 1
    ) {
        val existingItem = getCartItemByProductId(productId)
        if (existingItem != null) {
            updateCartItem(existingItem.copy(quantity = existingItem.quantity + quantity))
        } else {
            insertCartItem(
                ProductCart(
                    productId = productId,
                    name = name,
                    description = description,
                    price = price,
                    quantity = quantity,
                    imageUrl = imageUrl
                )
            )
        }
    }
}

