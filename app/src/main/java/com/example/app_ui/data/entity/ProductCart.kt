package com.example.app_ui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "product_cart")
data class ProductCart(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: String,
    val name: String,
    val description: String? = null,
    val price: Double,
    var quantity: Int = 1,
    val imageUrl: String? = null,
    val addedAt: Long = System.currentTimeMillis()
)

