package com.example.app_ui.data.repository

import android.content.Context
import com.example.app_ui.data.dao.ProductCartDao
import com.example.app_ui.data.entity.ProductCart
import com.example.app_ui.data.firebase.FirestoreService
import com.example.app_ui.utils.FirebaseUserHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first

/**
 * Enhanced Cart Repository that syncs with both Room (local) and Firestore (cloud)
 * This provides offline-first functionality with cloud sync
 */
class FirebaseCartRepository(
    private val cartDao: ProductCartDao,
    private val context: Context
) {
    private val userId: String?
        get() = FirebaseUserHelper.getCurrentUserId(context)
    
    /**
     * Get all cart items - try Firestore first if user is logged in, fallback to Room
     */
    fun getAllCartItems(): Flow<List<ProductCart>> = flow {
        if (userId != null) {
            try {
                // Get from Firestore
                val firestoreItems = FirestoreService.getCartItems(userId!!)
                val cartItems = firestoreItems.mapNotNull { data ->
                    try {
                        ProductCart(
                            id = (data["id"] as? Number)?.toLong() ?: 0,
                            productId = data["productId"] as? String ?: "",
                            name = data["name"] as? String ?: "",
                            description = data["description"] as? String,
                            price = (data["price"] as? Number)?.toDouble() ?: 0.0,
                            quantity = (data["quantity"] as? Number)?.toInt() ?: 1,
                            imageUrl = data["imageUrl"] as? String,
                            addedAt = (data["addedAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                emit(cartItems)
            } catch (e: Exception) {
                // Fallback to Room if Firestore fails
                emit(cartDao.getAllCartItems().first())
            }
        } else {
            // No user logged in, use Room only
            emit(cartDao.getAllCartItems().first())
        }
    }
    
    /**
     * Add or update cart item - save to both Room and Firestore
     */
    suspend fun addOrUpdateCartItem(
        productId: String,
        name: String,
        description: String?,
        price: Double,
        imageUrl: String?,
        quantity: Int = 1
    ) {
        val existingItem = cartDao.getCartItemByProductId(productId)
        
        if (existingItem != null) {
            // Update existing item
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
            cartDao.updateCartItem(updatedItem)
            
            // Sync to Firestore
            if (userId != null) {
                try {
                    val itemData = mapOf(
                        "id" to updatedItem.id,
                        "productId" to updatedItem.productId,
                        "name" to updatedItem.name,
                        "description" to (updatedItem.description ?: ""),
                        "price" to updatedItem.price,
                        "quantity" to updatedItem.quantity,
                        "imageUrl" to (updatedItem.imageUrl ?: ""),
                        "addedAt" to updatedItem.addedAt
                    )
                    FirestoreService.addCartItem(userId!!, updatedItem.id.toString(), itemData)
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Continue with local save even if Firestore fails
                }
            }
        } else {
            // Insert new item
            val newItem = ProductCart(
                productId = productId,
                name = name,
                description = description,
                price = price,
                quantity = quantity,
                imageUrl = imageUrl
            )
            val itemId = cartDao.insertCartItem(newItem)
            
            // Sync to Firestore
            if (userId != null) {
                try {
                    val itemData = mapOf(
                        "id" to itemId,
                        "productId" to newItem.productId,
                        "name" to newItem.name,
                        "description" to (newItem.description ?: ""),
                        "price" to newItem.price,
                        "quantity" to newItem.quantity,
                        "imageUrl" to (newItem.imageUrl ?: ""),
                        "addedAt" to newItem.addedAt
                    )
                    FirestoreService.addCartItem(userId!!, itemId.toString(), itemData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    /**
     * Remove cart item - delete from both Room and Firestore
     */
    suspend fun removeCartItem(item: ProductCart) {
        // Remove from Room
        cartDao.deleteCartItem(item)
        
        // Remove from Firestore
        if (userId != null) {
            try {
                FirestoreService.removeCartItem(userId!!, item.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Update cart item quantity - sync to both Room and Firestore
     */
    suspend fun updateCartItemQuantity(item: ProductCart, newQuantity: Int) {
        val updatedItem = item.copy(quantity = newQuantity)
        cartDao.updateCartItem(updatedItem)
        
        // Sync to Firestore
        if (userId != null) {
            try {
                FirestoreService.updateCartItemQuantity(userId!!, item.id.toString(), newQuantity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Get cart item count
     */
    fun getCartItemCount(): Flow<Int> = cartDao.getCartItemCount()
    
    /**
     * Get cart total
     */
    fun getCartTotal(): Flow<Double?> = cartDao.getCartTotal()
    
    /**
     * Clear all cart items
     */
    suspend fun clearCart() {
        cartDao.deleteAllCartItems()
        
        // Clear from Firestore
        if (userId != null) {
            try {
                FirestoreService.clearCart(userId!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

