package com.example.app_ui.data.dao

import androidx.room.*
import com.example.app_ui.data.entity.Order
import com.example.app_ui.data.entity.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    // Order operations
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<Order>>
    
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Long): Order?
    
    @Query("SELECT * FROM orders WHERE orderNumber = :orderNumber")
    suspend fun getOrderByOrderNumber(orderNumber: String): Order?
    
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getOrdersByUserId(userId: String): Flow<List<Order>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long
    
    @Update
    suspend fun updateOrder(order: Order)
    
    @Delete
    suspend fun deleteOrder(order: Order)
    
    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteOrderById(orderId: Long)
    
    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
    
    // Order count
    @Query("SELECT COUNT(*) FROM orders")
    fun getOrderCount(): Flow<Int>
    
    // OrderItem operations
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getOrderItemsByOrderId(orderId: Long): List<OrderItem>
    
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItemsByOrderIdFlow(orderId: Long): Flow<List<OrderItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: OrderItem): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItems: List<OrderItem>): List<Long>
    
    @Update
    suspend fun updateOrderItem(orderItem: OrderItem)
    
    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItem)
    
    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteOrderItemsByOrderId(orderId: Long)
    
    // Get order with items
    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderWithItems(orderId: Long): OrderWithItems?
}

data class OrderWithItems(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItem>
)

