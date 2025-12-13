package com.example.app_ui.service

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val type: String // "processing", "confirmation", etc.
)

