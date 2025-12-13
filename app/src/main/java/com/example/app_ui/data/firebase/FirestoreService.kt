package com.example.app_ui.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

object FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    
    // Collections
    private const val COLLECTION_USERS = "users"
    private const val COLLECTION_CART = "cart"
    private const val COLLECTION_FAVORITES = "favorites"
    private const val COLLECTION_ORDERS = "orders"
    private const val COLLECTION_NOTIFICATIONS = "notifications"
    
    // ========== USER OPERATIONS ==========
    
    suspend fun saveUser(userId: String, userData: Map<String, Any>) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .set(userData)
            .await()
    }
    
    suspend fun getUser(userId: String): Map<String, Any>? {
        val documentSnapshot = db.collection(COLLECTION_USERS)
            .document(userId)
            .get()
            .await()
        return documentSnapshot.data as? Map<String, Any>
    }
    
    suspend fun getUserByEmail(email: String): Map<String, Any>? {
        val querySnapshot = db.collection(COLLECTION_USERS)
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .await()
        
        return if (querySnapshot.isEmpty) null
        else querySnapshot.documents.first().data
    }
    
    suspend fun updateUser(userId: String, updates: Map<String, Any>) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .update(updates)
            .await()
    }
    
    // ========== CART OPERATIONS ==========
    
    suspend fun addCartItem(userId: String, itemId: String, itemData: Map<String, Any>) {
        db.collection(COLLECTION_CART)
            .document(userId)
            .collection("items")
            .document(itemId)
            .set(itemData)
            .await()
    }
    
    suspend fun getCartItems(userId: String): List<Map<String, Any>> {
        val querySnapshot = db.collection(COLLECTION_CART)
            .document(userId)
            .collection("items")
            .get()
            .await()
        
        return querySnapshot.documents.mapNotNull { it.data }
    }
    
    suspend fun removeCartItem(userId: String, itemId: String) {
        db.collection(COLLECTION_CART)
            .document(userId)
            .collection("items")
            .document(itemId)
            .delete()
            .await()
    }
    
    suspend fun updateCartItemQuantity(userId: String, itemId: String, quantity: Int) {
        db.collection(COLLECTION_CART)
            .document(userId)
            .collection("items")
            .document(itemId)
            .update(mapOf("quantity" to quantity))
            .await()
    }
    
    suspend fun clearCart(userId: String) {
        val items = getCartItems(userId)
        items.forEach { item ->
            val itemId = item["id"]?.toString() ?: return@forEach
            removeCartItem(userId, itemId)
        }
    }
    
    // ========== FAVORITES OPERATIONS ==========
    
    suspend fun addFavorite(userId: String, productId: String, productData: Map<String, Any>) {
        db.collection(COLLECTION_FAVORITES)
            .document(userId)
            .collection("products")
            .document(productId)
            .set(productData)
            .await()
    }
    
    suspend fun getFavorites(userId: String): List<Map<String, Any>> {
        val querySnapshot = db.collection(COLLECTION_FAVORITES)
            .document(userId)
            .collection("products")
            .get()
            .await()
        
        return querySnapshot.documents.mapNotNull { it.data }
    }
    
    suspend fun removeFavorite(userId: String, productId: String) {
        db.collection(COLLECTION_FAVORITES)
            .document(userId)
            .collection("products")
            .document(productId)
            .delete()
            .await()
    }
    
    suspend fun isFavorite(userId: String, productId: String): Boolean {
        val doc = db.collection(COLLECTION_FAVORITES)
            .document(userId)
            .collection("products")
            .document(productId)
            .get()
            .await()
        
        return doc.exists()
    }
    
    // ========== ORDERS OPERATIONS ==========
    
    suspend fun saveOrder(userId: String, orderId: String, orderData: Map<String, Any>) {
        db.collection(COLLECTION_ORDERS)
            .document(userId)
            .collection("orders")
            .document(orderId)
            .set(orderData)
            .await()
    }
    
    suspend fun getOrders(userId: String): List<Map<String, Any>> {
        val querySnapshot = db.collection(COLLECTION_ORDERS)
            .document(userId)
            .collection("orders")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
        
        return querySnapshot.documents.mapNotNull { it.data }
    }
    
    suspend fun getOrder(userId: String, orderId: String): Map<String, Any>? {
        val documentSnapshot = db.collection(COLLECTION_ORDERS)
            .document(userId)
            .collection("orders")
            .document(orderId)
            .get()
            .await()
        return documentSnapshot.data as? Map<String, Any>
    }
    
    suspend fun deleteOrder(userId: String, orderId: String) {
        db.collection(COLLECTION_ORDERS)
            .document(userId)
            .collection("orders")
            .document(orderId)
            .delete()
            .await()
    }
    
    // ========== NOTIFICATIONS OPERATIONS ==========
    
    suspend fun addNotification(userId: String, notificationId: String, notificationData: Map<String, Any>) {
        db.collection(COLLECTION_NOTIFICATIONS)
            .document(userId)
            .collection("notifications")
            .document(notificationId)
            .set(notificationData)
            .await()
    }
    
    suspend fun getNotifications(userId: String): List<Map<String, Any>> {
        val querySnapshot = db.collection(COLLECTION_NOTIFICATIONS)
            .document(userId)
            .collection("notifications")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()
        
        return querySnapshot.documents.mapNotNull { it.data }
    }
    
    suspend fun getUnreadNotifications(userId: String): List<Map<String, Any>> {
        val querySnapshot = db.collection(COLLECTION_NOTIFICATIONS)
            .document(userId)
            .collection("notifications")
            .whereEqualTo("isRead", false)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()
        
        return querySnapshot.documents.mapNotNull { it.data }
    }
    
    suspend fun deleteNotification(userId: String, notificationId: String) {
        db.collection(COLLECTION_NOTIFICATIONS)
            .document(userId)
            .collection("notifications")
            .document(notificationId)
            .delete()
            .await()
    }
    
    suspend fun markNotificationAsRead(userId: String, notificationId: String) {
        db.collection(COLLECTION_NOTIFICATIONS)
            .document(userId)
            .collection("notifications")
            .document(notificationId)
            .update(mapOf("isRead" to true))
            .await()
    }
    
    suspend fun markAllNotificationsAsRead(userId: String) {
        val notifications = getUnreadNotifications(userId)
        notifications.forEach { notification ->
            val notificationId = notification["id"]?.toString() ?: return@forEach
            markNotificationAsRead(userId, notificationId)
        }
    }
}

