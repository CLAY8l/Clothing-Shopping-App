package com.example.app_ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Search : Screen("search")
    object Saved : Screen("saved")
    object Detail : Screen("detail")
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object OrderAccepted : Screen("order_accepted")
    object Notifications : Screen("notifications")
    object SavedOrders : Screen("saved_orders")
    object Account : Screen("account")
}