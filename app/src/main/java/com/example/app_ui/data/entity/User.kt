package com.example.app_ui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val email: String,
    val name: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)

