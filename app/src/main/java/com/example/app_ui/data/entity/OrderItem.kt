package com.example.app_ui.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["orderId"])]
)
data class OrderItem(
    @androidx.room.PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    val orderId: Long,
    val productId: String,
    val name: String,
    val description: String? = null,
    val price: Double,
    val quantity: Int,
    val imageUrl: String? = null
)

