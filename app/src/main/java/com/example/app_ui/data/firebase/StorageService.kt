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
     * Upload product image from byte array
     * @param imageBytes Image bytes
     * @param productId Product ID
     * @return Download URL of uploaded image
     */
    suspend fun uploadProductImageBytes(imageBytes: ByteArray, productId: String): String {
        val imageRef = storageRef.child("$PRODUCT_IMAGES_PATH/$productId/${UUID.randomUUID()}.jpg")
        
        val uploadTask = imageRef.putBytes(imageBytes).await()
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
        try {
            val imageRef = storage.getReferenceFromUrl(imageUrl)
            imageRef.delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
            // Continue even if deletion fails
        }
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
    
    /**
     * Get user profile image URL
     * @param userId User ID
     * @return Download URL or null if not found
     */
    suspend fun getUserProfileImageUrl(userId: String): String? {
        return try {
            val imageRef = storageRef.child("$USER_PROFILE_IMAGES_PATH/$userId/profile.jpg")
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }
}

