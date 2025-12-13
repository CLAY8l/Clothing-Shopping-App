# 🔥 Firebase Integration Guide

This guide will help you integrate Firebase into your Clothing Shopping App. We'll set up:
- **Firebase Firestore** - For syncing database across devices
- **Firebase Storage** - For storing product images
- **Firebase Authentication** (Optional) - For better security

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Step 1: Create Firebase Project](#step-1-create-firebase-project)
3. [Step 2: Add Firebase to Android Project](#step-2-add-firebase-to-android-project)
4. [Step 3: Add Dependencies](#step-3-add-dependencies)
5. [Step 4: Initialize Firebase](#step-4-initialize-firebase)
6. [Step 5: Setup Firestore](#step-5-setup-firestore)
7. [Step 6: Setup Firebase Storage](#step-6-setup-firebase-storage)
8. [Step 7: Migrate Data to Firestore](#step-7-migrate-data-to-firestore)
9. [Step 8: Update Repositories](#step-8-update-repositories)
10. [Firebase Security Rules](#firebase-security-rules)

---

## Prerequisites

- Android Studio with Firebase Assistant plugin
- Google account
- Firebase project (we'll create this)

---

## Step 1: Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **"Add project"** or **"Create a project"**
3. Enter project name: `Clothing Shopping App`
4. Disable Google Analytics (optional) or enable it
5. Click **"Create project"**
6. Wait for project creation

---

## Step 2: Add Firebase to Android Project

### Option A: Using Firebase Assistant (Recommended)

1. In Android Studio, go to **Tools → Firebase**
2. Click **"Firestore"** → **"Add Cloud Firestore to your app"**
3. Click **"Connect to Firebase"** and sign in
4. Select your project or create a new one
5. Click **"Add Firestore to app"**
6. Follow the setup wizard

### Option B: Manual Setup

1. In Firebase Console, click **Android icon** (Add app)
2. Enter package name: `com.example.app_ui`
3. Enter app nickname (optional)
4. Download `google-services.json`
5. Place it in `app/` directory (same level as `build.gradle.kts`)
6. Click **"Next"** and follow instructions

---

## Step 3: Add Dependencies

Update your `app/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.gms.google-services") // Add this
}

// ... existing code ...

dependencies {
    // ... existing dependencies ...
    
    // Firebase BOM (Bill of Materials) - manages all Firebase library versions
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    
    // Firebase Firestore
    implementation("com.google.firebase:firebase-firestore-ktx")
    
    // Firebase Storage
    implementation("com.google.firebase:firebase-storage-ktx")
    
    // Firebase Authentication (Optional but recommended)
    implementation("com.google.firebase:firebase-auth-ktx")
    
    // Firebase Analytics (Optional)
    implementation("com.google.firebase:firebase-analytics-ktx")
}
```

Update your **project-level** `build.gradle.kts` (or `build.gradle`):

```kotlin
buildscript {
    dependencies {
        // ... existing dependencies ...
        classpath("com.google.gms:google-services:4.4.0")
    }
}
```

**Important**: Make sure `google-services.json` is in your `app/` directory.

---

## Step 4: Initialize Firebase

Update `AppApplication.kt`:

```kotlin
package com.example.app_ui

import android.app.Application
import com.google.firebase.FirebaseApp

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase (usually automatic, but explicit is better)
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
        
        // ... existing Room database initialization ...
    }
}
```

---

## Step 5: Setup Firestore

Create a new file `app/src/main/java/com/example/app_ui/data/firebase/FirestoreService.kt`:

```kotlin
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
        return db.collection(COLLECTION_USERS)
            .document(userId)
            .get()
            .await()
            .data()
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
        
        return querySnapshot.documents.map { it.data!! }
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
            .update("quantity", quantity)
            .await()
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
        
        return querySnapshot.documents.map { it.data!! }
    }
    
    suspend fun removeFavorite(userId: String, productId: String) {
        db.collection(COLLECTION_FAVORITES)
            .document(userId)
            .collection("products")
            .document(productId)
            .delete()
            .await()
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
        
        return querySnapshot.documents.map { it.data!! }
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
        
        return querySnapshot.documents.map { it.data!! }
    }
    
    suspend fun deleteNotification(userId: String, notificationId: String) {
        db.collection(COLLECTION_NOTIFICATIONS)
            .document(userId)
            .collection("notifications")
            .document(notificationId)
            .delete()
            .await()
    }
}
```

---

## Step 6: Setup Firebase Storage

Create `app/src/main/java/com/example/app_ui/data/firebase/StorageService.kt`:

```kotlin
package com.example.app_ui.data.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

object StorageService {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    
    // Storage paths
    private const val PRODUCT_IMAGES_PATH = "product_images"
    private const val USER_PROFILE_IMAGES_PATH = "user_profiles"
    
    /**
     * Upload product image to Firebase Storage
     * @param imageUri Local URI of the image
     * @param productId Product ID
     * @return Download URL of uploaded image
     */
    suspend fun uploadProductImage(imageUri: Uri, productId: String): String {
        val imageRef = storageRef.child("$PRODUCT_IMAGES_PATH/$productId/${UUID.randomUUID()}.jpg")
        
        val uploadTask = imageRef.putFile(imageUri).await()
        return uploadTask.storage.downloadUrl.await().toString()
    }
    
    /**
     * Upload user profile image
     * @param imageUri Local URI of the image
     * @param userId User ID
     * @return Download URL of uploaded image
     */
    suspend fun uploadUserProfileImage(imageUri: Uri, userId: String): String {
        val imageRef = storageRef.child("$USER_PROFILE_IMAGES_PATH/$userId/profile.jpg")
        
        val uploadTask = imageRef.putFile(imageUri).await()
        return uploadTask.storage.downloadUrl.await().toString()
    }
    
    /**
     * Delete product image
     * @param imageUrl Full URL of the image to delete
     */
    suspend fun deleteProductImage(imageUrl: String) {
        val imageRef = storage.storage.getReferenceFromUrl(imageUrl)
        imageRef.delete().await()
    }
    
    /**
     * Get download URL for a product image
     * @param productId Product ID
     * @param imageName Image file name
     * @return Download URL
     */
    suspend fun getProductImageUrl(productId: String, imageName: String): String {
        val imageRef = storageRef.child("$PRODUCT_IMAGES_PATH/$productId/$imageName")
        return imageRef.downloadUrl.await().toString()
    }
}
```

---

## Step 7: Migrate Data to Firestore

Create a migration helper `app/src/main/java/com/example/app_ui/utils/FirebaseMigrationHelper.kt`:

```kotlin
package com.example.app_ui.utils

import android.content.Context
import com.example.app_ui.data.database.AppDatabase
import com.example.app_ui.data.firebase.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FirebaseMigrationHelper {
    
    /**
     * Migrate all Room database data to Firestore
     * Call this after user logs in
     */
    suspend fun migrateToFirebase(context: Context, userId: String) = withContext(Dispatchers.IO) {
        val database = AppDatabase.getDatabase(context)
        
        try {
            // Migrate Cart Items
            val cartItems = database.productCartDao().getAllCartItems()
            cartItems.forEach { item ->
                val itemData = mapOf(
                    "productId" to item.productId,
                    "name" to item.name,
                    "price" to item.price,
                    "quantity" to item.quantity,
                    "imageUrl" to (item.imageUrl ?: ""),
                    "imageRes" to (item.imageRes ?: 0)
                )
                FirestoreService.addCartItem(userId, item.id.toString(), itemData)
            }
            
            // Migrate Favorites
            val favorites = database.favoriteProductDao().getAllFavorites()
            favorites.forEach { favorite ->
                val favoriteData = mapOf(
                    "productId" to favorite.productId,
                    "name" to favorite.name,
                    "price" to favorite.price,
                    "imageUrl" to (favorite.imageUrl ?: ""),
                    "description" to (favorite.description ?: "")
                )
                FirestoreService.addFavorite(userId, favorite.productId.toString(), favoriteData)
            }
            
            // Migrate Orders
            val orders = database.orderDao().getAllOrders()
            orders.forEach { order ->
                val orderData = mapOf(
                    "orderNumber" to order.orderNumber,
                    "total" to order.total,
                    "itemCount" to order.itemCount,
                    "date" to order.date,
                    "status" to order.status
                )
                FirestoreService.saveOrder(userId, order.id.toString(), orderData)
            }
            
            // Migrate Notifications
            val notifications = database.notificationDao().getAllNotifications()
            notifications.forEach { notification ->
                val notificationData = mapOf(
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
        }
    }
}
```

---

## Step 8: Update Repositories

Update your repositories to use both Room (local) and Firestore (cloud). Example for `CartRepository.kt`:

```kotlin
package com.example.app_ui.data.repository

import com.example.app_ui.data.dao.ProductCartDao
import com.example.app_ui.data.entity.ProductCart
import com.example.app_ui.data.firebase.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CartRepository(
    private val cartDao: ProductCartDao,
    private val currentUserId: String? = null // Get from auth
) {
    
    // Get cart items - try Firestore first, fallback to Room
    fun getAllCartItems(): Flow<List<ProductCart>> = flow {
        if (currentUserId != null) {
            try {
                // Get from Firestore
                val firestoreItems = FirestoreService.getCartItems(currentUserId)
                val cartItems = firestoreItems.map { data ->
                    ProductCart(
                        id = (data["id"] as? Long) ?: 0,
                        productId = (data["productId"] as? Int) ?: 0,
                        name = data["name"] as? String ?: "",
                        price = (data["price"] as? Double) ?: 0.0,
                        quantity = (data["quantity"] as? Int) ?: 1,
                        imageUrl = data["imageUrl"] as? String,
                        imageRes = (data["imageRes"] as? Int)
                    )
                }
                emit(cartItems)
            } catch (e: Exception) {
                // Fallback to Room
                emit(cartDao.getAllCartItems())
            }
        } else {
            // No user logged in, use Room only
            emit(cartDao.getAllCartItems())
        }
    }
    
    suspend fun addOrUpdateCartItem(item: ProductCart) {
        // Save to Room (local)
        cartDao.insertOrUpdateCartItem(item)
        
        // Save to Firestore (cloud) if user is logged in
        if (currentUserId != null) {
            try {
                val itemData = mapOf(
                    "id" to item.id,
                    "productId" to item.productId,
                    "name" to item.name,
                    "price" to item.price,
                    "quantity" to item.quantity,
                    "imageUrl" to (item.imageUrl ?: ""),
                    "imageRes" to (item.imageRes ?: 0)
                )
                FirestoreService.addCartItem(
                    currentUserId,
                    item.id.toString(),
                    itemData
                )
            } catch (e: Exception) {
                e.printStackTrace()
                // Continue with local save even if Firestore fails
            }
        }
    }
    
    suspend fun removeCartItem(item: ProductCart) {
        // Remove from Room
        cartDao.deleteCartItem(item)
        
        // Remove from Firestore
        if (currentUserId != null) {
            try {
                FirestoreService.removeCartItem(currentUserId, item.id.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    // ... other methods following same pattern
}
```

---

## Firebase Security Rules

Go to Firebase Console → Firestore Database → Rules, and add:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users collection
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Cart collection
    match /cart/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      match /items/{itemId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }
    
    // Favorites collection
    match /favorites/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      match /products/{productId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }
    
    // Orders collection
    match /orders/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      match /orders/{orderId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }
    
    // Notifications collection
    match /notifications/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      match /notifications/{notificationId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }
  }
}
```

For **Firebase Storage Rules** (Storage → Rules):

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Product images - readable by all, writable by authenticated users
    match /product_images/{productId}/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null;
    }
    
    // User profile images - only accessible by the user
    match /user_profiles/{userId}/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

---

## Optional: Firebase Authentication

If you want to use Firebase Auth instead of Room-based auth:

1. **Enable Authentication in Firebase Console**
   - Go to Authentication → Sign-in method
   - Enable Email/Password

2. **Update UserViewModel**:

```kotlin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    
    fun signUp(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                
                // Save user data to Firestore
                val userData = mapOf(
                    "email" to email,
                    "name" to name,
                    "createdAt" to System.currentTimeMillis()
                )
                FirestoreService.saveUser(result.user!!.uid, userData)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSignUpSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val result = auth.signInWithEmailAndPassword(email, password).await()
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoginSuccess = true,
                    currentUserId = result.user?.uid
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
}
```

---

## Next Steps

1. **Sync Gradle** - Let Android Studio sync dependencies
2. **Test Connection** - Run the app and check Firebase Console for data
3. **Gradual Migration** - Start with one feature (e.g., cart) and test thoroughly
4. **Handle Offline** - Firestore has built-in offline support
5. **Monitor Usage** - Check Firebase Console for usage and costs

---

## Troubleshooting

### Issue: `google-services.json` not found
- Make sure the file is in `app/` directory
- Sync Gradle after adding

### Issue: Build fails
- Check if `google-services` plugin is in project-level `build.gradle`
- Ensure all dependencies are synced

### Issue: Permission denied
- Check Firestore Security Rules
- Make sure user is authenticated (if using auth)

---

## Benefits of Firebase Integration

✅ **Cloud Sync** - Data syncs across devices  
✅ **Offline Support** - Firestore works offline  
✅ **Real-time Updates** - Automatic data updates  
✅ **Scalable** - Handles growth automatically  
✅ **Secure** - Built-in security rules  
✅ **Image Storage** - Firebase Storage for images  

---

**Need Help?** Check [Firebase Documentation](https://firebase.google.com/docs)

