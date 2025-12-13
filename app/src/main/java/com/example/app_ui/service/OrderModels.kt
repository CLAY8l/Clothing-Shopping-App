package com.example.app_ui.service

data class SavedOrder(
    val orderNumber: String,
    val total: Double,
    val itemCount: Int,
    val items: List<OrderItemData>,
    val date: Long,
    val status: String
)

data class OrderItemData(
    val productId: String,
    val name: String,
    val description: String?,
    val price: Double,
    val quantity: Int,
    val imageUrl: String?
)

