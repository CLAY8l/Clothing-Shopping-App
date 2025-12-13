package com.example.app_ui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String, // Unique order number
    val userId: String? = null, // Link to user if needed
    val deliveryAddress: String,
    val paymentMethod: String, // "Card", "Cash", "Pay"
    val cardNumber: String? = null, // Last 4 digits if card payment
    val promoCode: String? = null,
    val subtotal: Double,
    val vat: Double,
    val shippingFee: Double,
    val total: Double,
    val status: String = "Pending", // "Pending", "Processing", "Shipped", "Delivered", "Cancelled"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

