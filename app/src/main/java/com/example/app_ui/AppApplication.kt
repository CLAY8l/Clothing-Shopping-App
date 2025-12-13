package com.example.app_ui

import android.app.Application
import com.google.firebase.FirebaseApp
import com.example.app_ui.data.database.AppDatabase
import com.example.app_ui.data.repository.CartRepository
import com.example.app_ui.data.repository.FavoriteRepository
import com.example.app_ui.data.repository.NotificationRepository
import com.example.app_ui.data.repository.OrderRepository
import com.example.app_ui.data.repository.ProductRepository
import com.example.app_ui.data.repository.UserRepository

class AppApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val cartRepository by lazy { CartRepository(database.productCartDao()) }
    val favoriteRepository by lazy { FavoriteRepository(database.favoriteProductDao()) }
    val orderRepository by lazy { OrderRepository(database.orderDao()) }
    val notificationRepository by lazy { NotificationRepository(database.notificationDao()) }
    val productRepository by lazy { ProductRepository() }
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase (usually automatic, but explicit is better)
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
    }
}

