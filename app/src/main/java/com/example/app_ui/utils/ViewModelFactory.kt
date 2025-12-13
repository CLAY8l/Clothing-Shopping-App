package com.example.app_ui.utils

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.app_ui.AppApplication
import com.example.app_ui.viewmodel.CartViewModel
import com.example.app_ui.viewmodel.CartViewModelFactory
import com.example.app_ui.viewmodel.FavoriteViewModel
import com.example.app_ui.viewmodel.FavoriteViewModelFactory
import com.example.app_ui.viewmodel.NotificationViewModel
import com.example.app_ui.viewmodel.NotificationViewModelFactory
import com.example.app_ui.viewmodel.OrderViewModel
import com.example.app_ui.viewmodel.OrderViewModelFactory
import com.example.app_ui.viewmodel.ProductViewModel
import com.example.app_ui.viewmodel.ProductViewModelFactory
import com.example.app_ui.viewmodel.UserViewModel
import com.example.app_ui.viewmodel.UserViewModelFactory

fun getCartViewModelFactory(application: Application): CartViewModelFactory {
    val app = application as AppApplication
    return CartViewModelFactory(app.cartRepository, app.notificationRepository)
}

fun getNotificationViewModelFactory(application: Application): NotificationViewModelFactory {
    val app = application as AppApplication
    return NotificationViewModelFactory(app.notificationRepository)
}

fun getFavoriteViewModelFactory(application: Application): FavoriteViewModelFactory {
    val app = application as AppApplication
    return FavoriteViewModelFactory(app.favoriteRepository)
}

fun getUserViewModelFactory(application: Application): UserViewModelFactory {
    val app = application as AppApplication
    return UserViewModelFactory(app.userRepository, application)
}

fun getProductViewModelFactory(application: Application): ProductViewModelFactory {
    val app = application as AppApplication
    return ProductViewModelFactory(app.productRepository)
}

fun getOrderViewModelFactory(application: Application): OrderViewModelFactory {
    val app = application as AppApplication
    return OrderViewModelFactory(app.orderRepository)
}

