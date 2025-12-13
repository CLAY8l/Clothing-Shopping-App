package com.example.app_ui.utils

import android.content.Context
import com.example.app_ui.data.database.AppDatabase
import com.example.app_ui.data.firebase.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

object FirebaseMigrationHelper {
    
    /**
     * Migrate all Room database data to Firestore
     * Call this after user logs in
     * @param context Application context
     * @param userId Firebase user ID or email-based ID
     */
    suspend fun migrateToFirebase(context: Context, userId: String) = withContext(Dispatchers.IO) {
        val database = AppDatabase.getDatabase(context)
        
        try {
            // Migrate Cart Items
            val cartItems = database.productCartDao().getAllCartItems().first()
            cartItems.forEach { item ->
                val itemData = mapOf(
                    "id" to item.id,
                    "productId" to item.productId,
                    "name" to item.name,
                    "description" to (item.description ?: ""),
                    "price" to item.price,
                    "quantity" to item.quantity,
                    "imageUrl" to (item.imageUrl ?: ""),
                    "addedAt" to item.addedAt
                )
                FirestoreService.addCartItem(userId, item.id.toString(), itemData)
            }
            
            // Migrate Favorites
            val favorites = database.favoriteProductDao().getAllFavorites().first()
            favorites.forEach { favorite ->
                val favoriteData = mapOf(
                    "id" to favorite.id,
                    "productId" to favorite.productId,
                    "name" to favorite.name,
                    "description" to (favorite.description ?: ""),
                    "price" to favorite.price,
                    "imageUrl" to (favorite.imageUrl ?: ""),
                    "addedAt" to favorite.addedAt
                )
                FirestoreService.addFavorite(userId, favorite.productId, favoriteData)
            }
            
            // Migrate Orders
            val orders = database.orderDao().getAllOrders().first()
            orders.forEach { order ->
                val orderData = mapOf(
                    "id" to order.id,
                    "orderNumber" to order.orderNumber,
                    "userId" to (order.userId ?: ""),
                    "deliveryAddress" to order.deliveryAddress,
                    "paymentMethod" to order.paymentMethod,
                    "cardNumber" to (order.cardNumber ?: ""),
                    "promoCode" to (order.promoCode ?: ""),
                    "subtotal" to order.subtotal,
                    "vat" to order.vat,
                    "shippingFee" to order.shippingFee,
                    "total" to order.total,
                    "status" to order.status,
                    "createdAt" to order.createdAt,
                    "updatedAt" to order.updatedAt
                )
                FirestoreService.saveOrder(userId, order.id.toString(), orderData)
            }
            
            // Migrate Notifications
            val notifications = database.notificationDao().getAllNotifications().first()
            notifications.forEach { notification ->
                val notificationData = mapOf(
                    "id" to notification.id,
                    "title" to notification.title,
                    "message" to notification.message,
                    "type" to notification.type,
                    "timestamp" to notification.timestamp,
                    "isRead" to notification.isRead
                )
                FirestoreService.addNotification(
                    userId,
                    notification.id.toString(),
                    notificationData
                )
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle error - maybe show user a message
            throw e
        }
    }
    
    /**
     * Sync Firestore data back to Room (for offline support)
     * This is optional - you can use Firestore's offline persistence instead
     */
    suspend fun syncFromFirebase(context: Context, userId: String) = withContext(Dispatchers.IO) {
        val database = AppDatabase.getDatabase(context)
        
        try {
            // Sync Cart Items
            val firestoreCartItems = FirestoreService.getCartItems(userId)
            // Convert and save to Room if needed
            // This is optional since Firestore has offline support
            
            // Sync Favorites
            val firestoreFavorites = FirestoreService.getFavorites(userId)
            // Convert and save to Room if needed
            
            // Sync Orders
            val firestoreOrders = FirestoreService.getOrders(userId)
            // Convert and save to Room if needed
            
            // Sync Notifications
            val firestoreNotifications = FirestoreService.getNotifications(userId)
            // Convert and save to Room if needed
            
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
