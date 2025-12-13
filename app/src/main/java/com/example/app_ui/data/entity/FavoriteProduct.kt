package com.example.app_ui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProduct(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: String,
    val name: String,
    val description: String? = null,
    val price: Double,
    val imageUrl: String? = null,
    val addedAt: Long = System.currentTimeMillis()
)

