package com.example.app_ui.data.dao

import androidx.room.*
import com.example.app_ui.data.entity.FavoriteProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    @Query("SELECT * FROM favorite_products ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteProduct>>
    
    @Query("SELECT * FROM favorite_products WHERE id = :id")
    suspend fun getFavoriteById(id: Long): FavoriteProduct?
    
    @Query("SELECT * FROM favorite_products WHERE productId = :productId")
    suspend fun getFavoriteByProductId(productId: String): FavoriteProduct?
    
    @Query("SELECT * FROM favorite_products WHERE productId = :productId")
    fun getFavoriteByProductIdFlow(productId: String): Flow<FavoriteProduct?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteProduct): Long
    
    @Update
    suspend fun updateFavorite(favorite: FavoriteProduct)
    
    @Delete
    suspend fun deleteFavorite(favorite: FavoriteProduct)
    
    @Query("DELETE FROM favorite_products WHERE id = :id")
    suspend fun deleteFavoriteById(id: Long)
    
    @Query("DELETE FROM favorite_products WHERE productId = :productId")
    suspend fun deleteFavoriteByProductId(productId: String)
    
    @Query("DELETE FROM favorite_products")
    suspend fun deleteAllFavorites()
    
    @Query("SELECT COUNT(*) FROM favorite_products")
    fun getFavoriteCount(): Flow<Int>
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_products WHERE productId = :productId)")
    suspend fun isProductFavorite(productId: String): Boolean
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_products WHERE productId = :productId)")
    fun isProductFavoriteFlow(productId: String): Flow<Boolean>
    
    // Bulk operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favorites: List<FavoriteProduct>): List<Long>
    
    @Update
    suspend fun updateFavorites(favorites: List<FavoriteProduct>)
    
    @Delete
    suspend fun deleteFavorites(favorites: List<FavoriteProduct>)
}

