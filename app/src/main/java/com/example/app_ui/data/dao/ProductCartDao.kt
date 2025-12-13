package com.example.app_ui.data.dao

import androidx.room.*
import com.example.app_ui.data.entity.ProductCart
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCartDao {
    @Query("SELECT * FROM product_cart ORDER BY addedAt DESC")
    fun getAllCartItems(): Flow<List<ProductCart>>
    
    @Query("SELECT * FROM product_cart WHERE id = :id")
    suspend fun getCartItemById(id: Long): ProductCart?
    
    @Query("SELECT * FROM product_cart WHERE productId = :productId")
    suspend fun getCartItemByProductId(productId: String): ProductCart?
    
    @Query("SELECT * FROM product_cart WHERE productId = :productId")
    fun getCartItemByProductIdFlow(productId: String): Flow<ProductCart?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: ProductCart): Long
    
    @Update
    suspend fun updateCartItem(item: ProductCart)
    
    @Delete
    suspend fun deleteCartItem(item: ProductCart)
    
    @Query("DELETE FROM product_cart WHERE id = :id")
    suspend fun deleteCartItemById(id: Long)
    
    @Query("DELETE FROM product_cart WHERE productId = :productId")
    suspend fun deleteCartItemByProductId(productId: String)
    
    @Query("DELETE FROM product_cart")
    suspend fun deleteAllCartItems()
    
    @Query("SELECT COUNT(*) FROM product_cart")
    fun getCartItemCount(): Flow<Int>
    
    @Query("SELECT SUM(price * quantity) FROM product_cart")
    fun getCartTotal(): Flow<Double?>
    
    @Query("SELECT EXISTS(SELECT 1 FROM product_cart WHERE productId = :productId)")
    suspend fun isProductInCart(productId: String): Boolean
    
    @Query("SELECT EXISTS(SELECT 1 FROM product_cart WHERE productId = :productId)")
    fun isProductInCartFlow(productId: String): Flow<Boolean>
    
    // Bulk operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItems(items: List<ProductCart>): List<Long>
    
    @Update
    suspend fun updateCartItems(items: List<ProductCart>)
    
    @Delete
    suspend fun deleteCartItems(items: List<ProductCart>)
}

