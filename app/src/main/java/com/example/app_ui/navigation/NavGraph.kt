package com.example.app_ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app_ui.screens.CartScreen
import com.example.app_ui.screens.CheckoutScreen
import com.example.app_ui.screens.DiscoverScreenWithState
import com.example.app_ui.screens.LoginScreen
import com.example.app_ui.screens.LoginUiState
import com.example.app_ui.screens.OnBordingScreen
import com.example.app_ui.screens.OrderAcceptedScreen
import com.example.app_ui.screens.SignUpScreen
import com.example.app_ui.screens.SignUpUiState
import com.example.app_ui.screens.SavedScreen
import com.example.app_ui.screens.SearchScreen
import com.example.app_ui.screens.NotificationsScreen
import com.example.app_ui.screens.ProductDetailScreen
import com.example.app_ui.screens.SavedOrdersScreen
import com.example.app_ui.screens.AccountScreen
import com.example.app_ui.screens.sampleProducts
import com.example.app_ui.utils.getProductViewModelFactory
import com.example.app_ui.utils.getUserViewModelFactory
import com.example.app_ui.viewmodel.ProductViewModel
import com.example.app_ui.viewmodel.UserViewModel
import com.example.clothingapp_frontend.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        // Onboarding Screen
        composable(route = "onboarding") {
            OnBordingScreen(
                onContinue = { /* Handle continue if needed */ },
                onGetStartedClicked = {
                    navController.navigate("login")
                }
            )
        }

        // Login Screen
        composable(route = "login") {
            val context = LocalContext.current
            val userViewModel: UserViewModel = viewModel(
                factory = getUserViewModelFactory(context.applicationContext as android.app.Application)
            )
            val authState by userViewModel.uiState.collectAsState()
            
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var isPasswordVisible by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            // Handle login success
            LaunchedEffect(authState.isLoginSuccess) {
                if (authState.isLoginSuccess) {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            }

            // Handle error messages
            LaunchedEffect(authState.errorMessage) {
                authState.errorMessage?.let {
                    errorMessage = it
                    userViewModel.clearError()
                }
            }

            LoginScreen(
                uiState = LoginUiState(
                    email = email,
                    password = password,
                    isPasswordVisible = isPasswordVisible
                ),
                onEmailChange = { email = it; errorMessage = null },
                onPasswordChange = { password = it; errorMessage = null },
                onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                onLogin = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in all fields"
                    } else {
                        userViewModel.login(email, password)
                    }
                },
                onJoin = {
                    navController.navigate("signup")
                },
                onForgotPassword = {
                    // You can add forgot password screen later
                },
                errorMessage = errorMessage,
                isLoading = authState.isLoading
            )
        }

        // Sign Up Screen
        composable(route = "signup") {
            val context = LocalContext.current
            val userViewModel: UserViewModel = viewModel(
                factory = getUserViewModelFactory(context.applicationContext as android.app.Application)
            )
            val authState by userViewModel.uiState.collectAsState()
            
            var fullName by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var isPasswordVisible by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            // Handle sign up success
            LaunchedEffect(authState.isSignUpSuccess) {
                if (authState.isSignUpSuccess) {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            }

            // Handle error messages
            LaunchedEffect(authState.errorMessage) {
                authState.errorMessage?.let {
                    errorMessage = it
                    userViewModel.clearError()
                }
            }

            SignUpScreen(
                uiState = SignUpUiState(
                    fullName = fullName,
                    email = email,
                    password = password,
                    isPasswordVisible = isPasswordVisible
                ),
                onFullNameChange = { fullName = it; errorMessage = null },
                onEmailChange = { email = it; errorMessage = null },
                onPasswordChange = { password = it; errorMessage = null },
                onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                onCreateAccount = {
                    if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in all fields"
                    } else if (password.length < 6) {
                        errorMessage = "Password must be at least 6 characters"
                    } else {
                        userViewModel.signUp(fullName, email, password)
                    }
                },
                onLogin = {
                    navController.navigate("login")
                },
                onTermsClick = { /* Handle terms */ },
                onPrivacyClick = { /* Handle privacy */ },
                onCookieClick = { /* Handle cookie */ },
                errorMessage = errorMessage,
                isLoading = authState.isLoading
            )
        }

        // Home Screen
        composable(route = "home") {
            DiscoverScreenWithState(
                onSneakerClick = { product ->
                    navController.navigate("detail/${product.id}")
                },
                onCartClick = {
                    navController.navigate("cart")
                },
                onSearchClick = {
                    navController.navigate("search")
                },
                onSavedClick = {
                    navController.navigate("saved")
                },
                onAccountClick = {
                    navController.navigate("account")
                },
                onBellClick = {
                    navController.navigate("notifications")
                }
            )
        }

        composable(route = "search") {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onNotification = { navController.navigate("notifications") },
                onProductClick = { product ->
                    navController.navigate("detail/${product.id}")
                }
            )
        }

        composable(route = "saved") {
            SavedScreen(
                onBack = { navController.popBackStack() },
                onNotification = { navController.navigate("notifications") },
                onHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onSearch = { navController.navigate("search") },
                onSaved = { /* already here */ },
                onCart = { navController.navigate("cart") },
                onAccount = { navController.navigate("account") },
                onProductClick = { productId ->
                    navController.navigate("detail/$productId")
                }
            )
        }

        // Detail Screen
        composable(route = "detail/{productId}") { backStackEntry ->
            val context = LocalContext.current
            val productViewModel: ProductViewModel = viewModel(
                factory = getProductViewModelFactory(context.applicationContext as android.app.Application)
            )
            val apiProducts by productViewModel.products.collectAsState()
            
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: 1
            
            // Combine local and API products to find the product
            val allProducts = remember(apiProducts) {
                val productMap = mutableMapOf<Int, com.example.app_ui.screens.ProductUi>()
                sampleProducts.forEach { productMap[it.id] = it }
                apiProducts.forEach { productMap[it.id] = it }
                productMap.values.toList()
            }
            
            val product = allProducts.find { it.id == productId } 
                ?: allProducts.firstOrNull()
                ?: sampleProducts.first()

            ProductDetailScreen(
                product = product,
                onBack = { navController.popBackStack() },
                onNotifications = { navController.navigate("notifications") }
            )
        }

        // Cart Screen
        composable(route = "cart") {
            CartScreen(
                onBack = { navController.popBackStack() },
                onCheckout = {
                    navController.navigate("checkout")
                },
                onNotification = { navController.navigate("notifications") },
                onHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onSearch = { navController.navigate("search") },
                onSaved = { navController.navigate("saved") },
                onCart = { /* already here */ },
                onAccount = { navController.navigate("account") }
            )
        }

        // Checkout Screen
        composable(route = "checkout") {
            CheckoutScreen(
                onBack = { navController.popBackStack() },
                onPlaceOrder = {
                    navController.navigate("order_accepted")
                },
                onNotification = { navController.navigate("notifications") }
            )
        }

        // Order Accepted Screen
        composable(route = "order_accepted") {
            OrderAcceptedScreen(
                onBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onTrackOrder = {
                    navController.navigate("saved_orders")
                }
            )
        }

        // Notifications Screen
        composable(route = "notifications") {
            NotificationsScreen(
                onBack = { navController.popBackStack() },
                onHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onSearch = { navController.navigate("search") },
                onSaved = { navController.navigate("saved") },
                onCart = { navController.navigate("cart") },
                onAccount = { /* handle account when added */ }
            )
        }

        // Saved Orders Screen (to view orders from SharedPreferences)
        composable(route = "saved_orders") {
            SavedOrdersScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Account Screen
        composable(route = "account") {
            val context = LocalContext.current
            val userViewModel: UserViewModel = viewModel(
                factory = getUserViewModelFactory(context.applicationContext as android.app.Application)
            )
            
            AccountScreen(
                onBack = { navController.popBackStack() },
                onNotification = { navController.navigate("notifications") },
                onHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onSearch = { navController.navigate("search") },
                onSaved = { navController.navigate("saved") },
                onCart = { navController.navigate("cart") },
                onAccount = { /* already here */ },
                onMyOrders = { navController.navigate("saved_orders") },
                onMyDetails = { /* Handle my details */ },
                onAddressBook = { /* Handle address book */ },
                onPaymentMethods = { /* Handle payment methods */ },
                onNotifications = { navController.navigate("notifications") },
                onFAQs = { /* Handle FAQs */ },
                onHelpCenter = { /* Handle help center */ },
                onLogout = {
                    // Call logout function to clear Firebase user data
                    userViewModel.logout()
                    // Navigate to login and clear back stack
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
    }
}