package com.example.app_ui.data.repository

import com.example.app_ui.data.dao.OrderDao
import com.example.app_ui.data.entity.Order
import com.example.app_ui.data.entity.OrderItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OrderRepository(private val orderDao: OrderDao) {
    fun getAllOrders(): Flow<List<Order>> = orderDao.getAllOrders()
    
    suspend fun getOrderById(orderId: Long): Order? = orderDao.getOrderById(orderId)
    
    suspend fun getOrderByOrderNumber(orderNumber: String): Order? = 
        orderDao.getOrderByOrderNumber(orderNumber)
    
    fun getOrdersByUserId(userId: String): Flow<List<Order>> = 
        orderDao.getOrdersByUserId(userId)
    
    suspend fun insertOrder(order: Order): Long = orderDao.insertOrder(order)
    
    suspend fun updateOrder(order: Order) = orderDao.updateOrder(order)
    
    suspend fun deleteOrder(order: Order) = orderDao.deleteOrder(order)
    
    suspend fun deleteOrderById(orderId: Long) = orderDao.deleteOrderById(orderId)
    
    suspend fun deleteAllOrders() = orderDao.deleteAllOrders()
    
    fun getOrderCount(): Flow<Int> = orderDao.getOrderCount()
    
    suspend fun getOrderItemsByOrderId(orderId: Long): List<OrderItem> = 
        orderDao.getOrderItemsByOrderId(orderId)
    
    fun getOrderItemsByOrderIdFlow(orderId: Long): Flow<List<OrderItem>> = 
        orderDao.getOrderItemsByOrderIdFlow(orderId)
    
    suspend fun insertOrderItem(orderItem: OrderItem): Long = 
        orderDao.insertOrderItem(orderItem)
    
    suspend fun insertOrderItems(orderItems: List<OrderItem>): List<Long> = 
        orderDao.insertOrderItems(orderItems)
    
    suspend fun updateOrderItem(orderItem: OrderItem) = orderDao.updateOrderItem(orderItem)
    
    suspend fun deleteOrderItem(orderItem: OrderItem) = orderDao.deleteOrderItem(orderItem)
    
    suspend fun deleteOrderItemsByOrderId(orderId: Long) = 
        orderDao.deleteOrderItemsByOrderId(orderId)
    
    // Create a complete order with items
    suspend fun createOrder(
        userId: String?,
        deliveryAddress: String,
        paymentMethod: String,
        cardNumber: String?,
        promoCode: String?,
        subtotal: Double,
        vat: Double,
        shippingFee: Double,
        total: Double,
        items: List<OrderItem>
    ): Long {
        // Generate unique order number
        val orderNumber = "ORD-${System.currentTimeMillis()}-${UUID.randomUUID().toString().take(8).uppercase()}"
        
        val order = Order(
            orderNumber = orderNumber,
            userId = userId,
            deliveryAddress = deliveryAddress,
            paymentMethod = paymentMethod,
            cardNumber = cardNumber,
            promoCode = promoCode,
            subtotal = subtotal,
            vat = vat,
            shippingFee = shippingFee,
            total = total,
            status = "Pending"
        )
        
        val orderId = insertOrder(order)
        
        // Insert order items with the order ID
        val orderItemsWithId = items.map { it.copy(orderId = orderId) }
        insertOrderItems(orderItemsWithId)
        
        return orderId
    }
}

