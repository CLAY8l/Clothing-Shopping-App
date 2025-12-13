package com.example.app_ui.data.repository

import com.example.app_ui.data.dao.FavoriteProductDao
import com.example.app_ui.data.entity.FavoriteProduct
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val favoriteDao: FavoriteProductDao) {
    fun getAllFavorites(): Flow<List<FavoriteProduct>> = favoriteDao.getAllFavorites()
    
    suspend fun getFavoriteById(id: Long): FavoriteProduct? = favoriteDao.getFavoriteById(id)
    
    suspend fun getFavoriteByProductId(productId: String): FavoriteProduct? = 
        favoriteDao.getFavoriteByProductId(productId)
    
    fun getFavoriteByProductIdFlow(productId: String): Flow<FavoriteProduct?> = 
        favoriteDao.getFavoriteByProductIdFlow(productId)
    
    suspend fun insertFavorite(favorite: FavoriteProduct): Long = 
        favoriteDao.insertFavorite(favorite)
    
    suspend fun updateFavorite(favorite: FavoriteProduct) = favoriteDao.updateFavorite(favorite)
    
    suspend fun deleteFavorite(favorite: FavoriteProduct) = favoriteDao.deleteFavorite(favorite)
    
    suspend fun deleteFavoriteById(id: Long) = favoriteDao.deleteFavoriteById(id)
    
    suspend fun deleteFavoriteByProductId(productId: String) = 
        favoriteDao.deleteFavoriteByProductId(productId)
    
    suspend fun deleteAllFavorites() = favoriteDao.deleteAllFavorites()
    
    fun getFavoriteCount(): Flow<Int> = favoriteDao.getFavoriteCount()
    
    suspend fun isProductFavorite(productId: String): Boolean = 
        favoriteDao.isProductFavorite(productId)
    
    fun isProductFavoriteFlow(productId: String): Flow<Boolean> = 
        favoriteDao.isProductFavoriteFlow(productId)
    
    // Bulk operations
    suspend fun insertFavorites(favorites: List<FavoriteProduct>): List<Long> = 
        favoriteDao.insertFavorites(favorites)
    
    suspend fun updateFavorites(favorites: List<FavoriteProduct>) = 
        favoriteDao.updateFavorites(favorites)
    
    suspend fun deleteFavorites(favorites: List<FavoriteProduct>) = 
        favoriteDao.deleteFavorites(favorites)
    
    // Toggle favorite - add if not exists, remove if exists
    suspend fun toggleFavorite(
        productId: String,
        name: String,
        description: String?,
        price: Double,
        imageUrl: String?
    ) {
        val existing = getFavoriteByProductId(productId)
        if (existing != null) {
            deleteFavorite(existing)
        } else {
            insertFavorite(
                FavoriteProduct(
                    productId = productId,
                    name = name,
                    description = description,
                    price = price,
                    imageUrl = imageUrl
                )
            )
        }
    }
}

