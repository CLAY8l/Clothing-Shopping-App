package com.example.app_ui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val type: String, // "processing", "confirmation", "cart_added", etc.
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

