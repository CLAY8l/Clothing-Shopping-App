# 👕 Clothing Shopping App

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)
![Room Database](https://img.shields.io/badge/Room-4285F4?style=for-the-badge&logo=android&logoColor=white)

A modern, feature-rich Android shopping application built with Jetpack Compose, Room Database, and MVVM architecture. Browse clothing items, manage your cart, save favorites, and complete purchases seamlessly.

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Screens](#-screens)
- [Database Schema](#-database-schema)
- [API Integration](#-api-integration)
- [Project Structure](#-project-structure)
- [Setup Instructions](#-setup-instructions)
- [Key Components](#-key-components)
- [Services](#-services)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

This is a fully functional e-commerce Android application designed for clothing shopping. The app provides a seamless shopping experience with features like product browsing, cart management, favorites, order processing, and user account management. Built with modern Android development practices, it demonstrates best practices in Jetpack Compose, Room Database, MVVM architecture, and API integration.

### Key Highlights

- ✨ **Modern UI/UX** - Beautiful, responsive design built with Jetpack Compose
- 🗄️ **Local Database** - Room database for offline data persistence
- 🌐 **API Integration** - Fetches products from FakeStore API
- 🔐 **User Authentication** - Secure login and signup with Room database
- 🛒 **Shopping Cart** - Full cart management with quantity updates
- ❤️ **Favorites** - Save and manage favorite products
- 📦 **Order Management** - Complete order processing with background service
- 🔔 **Notifications** - In-app notification system
- 🔍 **Search** - Real-time product search functionality

---

## ✨ Features

### 🔐 Authentication
- **Onboarding Screen** - Welcome experience for new users
- **Login** - Secure user authentication with email and password
- **Sign Up** - New user registration with validation
- **Session Management** - Persistent login sessions

### 🏠 Home & Discovery
- **Product Grid** - Display local and API products in a beautiful grid layout
- **Category Filtering** - Dynamic category buttons (T-Shirt, Jeans, Shoes, Jacket, etc.)
- **Wishlist Toggle** - Quick add/remove products to favorites
- **Product Details** - Comprehensive product information with size and color selection

### 🔍 Search
- **Real-time Search** - Instant product search as you type
- **Smart Filtering** - Search by product name or category
- **Visual Results** - Product images and details in search results

### ❤️ Saved Products
- **Favorites Management** - View all saved products
- **Grid Layout** - Beautiful 2-column grid display
- **Quick Navigation** - Tap to view product details
- **Remove Favorites** - Easy removal of saved items

### 🛒 Shopping Cart
- **Cart Management** - Add, remove, and update quantities
- **Price Calculation** - Automatic subtotal, shipping, and total calculation
- **Item Count** - Real-time cart item count
- **Checkout Flow** - Seamless transition to checkout

### 💳 Checkout & Orders
- **Order Processing** - Complete checkout flow
- **Background Service** - Order processing with foreground service
- **Order Notifications** - Real-time order status updates
- **Order History** - View all past orders
- **Swipe to Delete** - Easy order management

### 🔔 Notifications
- **In-App Notifications** - Centralized notification center
- **Multiple Types** - Cart additions, order processing, confirmations
- **Swipe to Delete** - Remove notifications with swipe gesture
- **Real-time Updates** - Instant notification display

### 👤 Account Management
- **My Orders** - View order history
- **My Details** - User profile information
- **Address Book** - Manage delivery addresses
- **Payment Methods** - Saved payment options
- **Notifications Settings** - Manage notification preferences
- **Help Center** - FAQs and support
- **Logout** - Secure session termination

---

## 🛠️ Tech Stack

### Core Technologies
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Persistence Library
- **Dependency Injection**: Manual ViewModel Factories

### Libraries & Dependencies

#### UI & Navigation
- `androidx.compose.material3` - Material Design 3 components
- `androidx.navigation:navigation-compose` - Navigation between screens
- `io.coil-kt:coil-compose` - Image loading library

#### Database & Persistence
- `androidx.room:room-runtime` - Room database runtime
- `androidx.room:room-ktx` - Room Kotlin extensions
- `androidx.room:room-compiler` - Room annotation processor

#### Architecture Components
- `androidx.lifecycle:lifecycle-viewmodel-compose` - ViewModel for Compose
- `androidx.lifecycle:lifecycle-runtime-compose` - Lifecycle-aware components
- `androidx.lifecycle:lifecycle-livedata-ktx` - LiveData support

#### Networking
- `com.squareup.retrofit2:retrofit` - HTTP client
- `com.squareup.retrofit2:converter-gson` - JSON converter
- `com.squareup.okhttp3:okhttp` - HTTP client implementation
- `com.squareup.okhttp3:logging-interceptor` - Network logging

#### Coroutines
- `org.jetbrains.kotlinx:kotlinx-coroutines-android` - Asynchronous programming

### Minimum Requirements
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 36
- **Compile SDK**: 36

---

## 🏗️ Architecture

The application follows the **MVVM (Model-View-ViewModel)** architecture pattern, ensuring separation of concerns and testability.

```
┌─────────────────────────────────────────────────────────┐
│                      UI Layer                           │
│  (Compose Screens, Components, Navigation)             │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                   ViewModel Layer                       │
│  (CartViewModel, UserViewModel, ProductViewModel, etc.)│
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                  Repository Layer                       │
│  (CartRepository, UserRepository, ProductRepository)    │
└────────────────────┬────────────────────────────────────┘
                     │
        ┌────────────┴────────────┐
        ▼                         ▼
┌──────────────┐         ┌──────────────┐
│ Room Database│         │  API Service │
│  (Local Data)│         │ (Remote Data)│
└──────────────┘         └──────────────┘
```

### Architecture Components

#### **View (Compose Screens)**
- Displays UI and handles user interactions
- Observes ViewModel state using `collectAsState()`
- No business logic, only UI rendering

#### **ViewModel**
- Manages UI-related data
- Exposes state using `StateFlow` and `Flow`
- Handles user actions and delegates to repositories
- Survives configuration changes

#### **Repository**
- Single source of truth for data
- Combines data from multiple sources (Room, API)
- Provides clean API for ViewModels
- Handles data transformation

#### **Data Sources**
- **Room Database**: Local data persistence
- **FakeStore API**: Remote product data
- **SharedPreferences**: Order storage (legacy)

---

## 📱 Screens

### 1. **Onboarding Screen**
- Welcome screen for first-time users
- Smooth transition to authentication

### 2. **Login Screen**
- Email and password authentication
- Form validation
- Error handling
- Navigation to signup

### 3. **Sign Up Screen**
- User registration
- Password visibility toggle
- Terms and conditions
- Email validation

### 4. **Home Screen**
- Product grid with local and API products
- Category filtering (All Items, T-Shirt, Jeans, Shoes, etc.)
- Search bar
- Wishlist toggle
- Bottom navigation

### 5. **Search Screen**
- Real-time search functionality
- Product filtering
- Search results display
- Empty state handling

### 6. **Product Detail Screen**
- Product images (local and remote)
- Product information
- Size selection
- Color selection
- Quantity selector
- Add to cart
- Save to favorites

### 7. **Saved Screen**
- Grid layout of favorite products
- Product cards with images
- Remove from favorites
- Navigation to product details

### 8. **Cart Screen**
- Cart items list
- Quantity adjustment
- Item removal
- Price calculation (subtotal, shipping, total)
- Checkout button

### 9. **Checkout Screen**
- Order summary
- Delivery information
- Payment method selection
- Place order functionality

### 10. **Order Accepted Screen**
- Order confirmation
- Order tracking option
- View orders option
- Return to home

### 11. **My Orders Screen**
- Order history display
- Order details (items, total, date, status)
- Swipe to delete orders
- Order cards with contrasting colors

### 12. **Notifications Screen**
- Notification list
- Notification types (cart, order, etc.)
- Swipe to delete
- Empty state

### 13. **Account Screen**
- User profile menu
- My Orders navigation
- My Details
- Address Book
- Payment Methods
- Notifications settings
- Help Center
- Logout

---

## 🗄️ Database Schema

The application uses **Room Database** for local data persistence. The database includes the following entities:

### **User Entity**
```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val name: String,
    val password: String
)
```
- Stores user authentication information
- Email as primary key
- Password for login verification

### **ProductCart Entity**
```kotlin
@Entity(tableName = "cart_items")
data class ProductCart(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String?,
    val imageRes: Int?
)
```
- Shopping cart items
- Product information with quantity
- Supports both local (imageRes) and API (imageUrl) products

### **FavoriteProduct Entity**
```kotlin
@Entity(tableName = "favorite_products")
data class FavoriteProduct(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Int,
    val name: String,
    val price: String,
    val imageUrl: String?,
    val description: String?
)
```
- Saved favorite products
- Product details for quick access

### **Order Entity**
```kotlin
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val total: Double,
    val itemCount: Int,
    val date: Long,
    val status: String
)
```
- Order information
- Order number, total, item count, date, status

### **OrderItem Entity**
```kotlin
@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int
)
```
- Individual items within an order
- Foreign key relationship with Order

### **Notification Entity**
```kotlin
@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val type: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
```
- In-app notifications
- Read/unread status
- Timestamp for sorting

### Database Version
- **Current Version**: 4
- **Migration Strategy**: Manual migration handling

---

## 🌐 API Integration

### FakeStore API
The application integrates with [FakeStore API](https://fakestoreapi.com/) to fetch men's clothing products.

#### API Service
```kotlin
interface FakeStoreApiService {
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String
    ): List<ApiProduct>
}
```

#### Product Mapping
- API products are mapped to `ProductUi` format
- Product IDs are offset by 1000 to avoid conflicts with local products
- Dynamic category extraction from product types
- Image URLs are handled with Coil for async loading

#### Categories Supported
- Men's Clothing
- Product types: T-Shirt, Jeans, Shoes, Jacket, Sack, etc.

---

## 📁 Project Structure

```
app/src/main/java/com/example/app_ui/
│
├── data/
│   ├── api/
│   │   ├── FakeStoreApiService.kt      # Retrofit API interface
│   │   ├── RetrofitClient.kt           # Retrofit singleton
│   │   └── model/
│   │       └── ApiProduct.kt          # API product data class
│   │
│   ├── dao/
│   │   ├── UserDao.kt                 # User database operations
│   │   ├── ProductCartDao.kt           # Cart database operations
│   │   ├── FavoriteProductDao.kt      # Favorites database operations
│   │   ├── OrderDao.kt                # Order database operations
│   │   └── NotificationDao.kt        # Notification database operations
│   │
│   ├── database/
│   │   └── AppDatabase.kt             # Room database instance
│   │
│   ├── entity/
│   │   ├── User.kt                    # User entity
│   │   ├── ProductCart.kt              # Cart item entity
│   │   ├── FavoriteProduct.kt         # Favorite product entity
│   │   ├── Order.kt                    # Order entity
│   │   ├── OrderItem.kt                # Order item entity
│   │   └── Notification.kt             # Notification entity
│   │
│   └── repository/
│       ├── UserRepository.kt           # User data repository
│       ├── CartRepository.kt          # Cart data repository
│       ├── FavoriteRepository.kt       # Favorites repository
│       ├── ProductRepository.kt        # Product API repository
│       ├── OrderRepository.kt          # Order repository
│       └── NotificationRepository.kt   # Notification repository
│
├── navigation/
│   ├── NavGraph.kt                     # Navigation graph
│   └── Screen.kt                       # Screen route definitions
│
├── screens/
│   ├── OnBordingScreen.kt             # Onboarding screen
│   ├── LoginScreen.kt                  # Login screen
│   ├── SignInScreen.kt                 # Sign up screen
│   ├── HomeScreen.kt                   # Home/product listing
│   ├── SearchScreen.kt                 # Search screen
│   ├── ProductDetailScreen.kt         # Product details
│   ├── SavedScreen.kt                  # Favorites screen
│   ├── CartScreen.kt                   # Shopping cart
│   ├── CheckoutScreen.kt               # Checkout screen
│   ├── OrderAcceptedScreen.kt          # Order confirmation
│   ├── SavedOrdersScreen.kt            # Order history
│   ├── NotificationsScreen.kt          # Notifications
│   └── AccountScreen.kt                 # Account management
│
├── service/
│   ├── SendOrderService.kt             # Order processing service
│   └── OrderModels.kt                  # Order data models
│
├── utils/
│   ├── ViewModelFactory.kt             # ViewModel factory helpers
│   └── OrderReader.kt                  # Order reading utilities
│
├── viewmodel/
│   ├── UserViewModel.kt                # User authentication ViewModel
│   ├── ProductViewModel.kt             # Product ViewModel
│   ├── CartViewModel.kt                # Cart ViewModel
│   ├── FavoriteViewModel.kt            # Favorites ViewModel
│   ├── OrderViewModel.kt               # Order ViewModel
│   └── NotificationViewModel.kt        # Notification ViewModel
│
├── ui/
│   └── theme/
│       ├── Color.kt                    # Color definitions
│       ├── Theme.kt                    # App theme
│       └── Type.kt                      # Typography
│
├── AppApplication.kt                   # Application class
└── MainActivity.kt                     # Main activity
```

---

## 🚀 Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK 26 or higher
- Kotlin plugin

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd APP_UI
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the project directory

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

4. **Build the project**
   ```bash
   ./gradlew build
   ```

5. **Run the app**
   - Connect an Android device or start an emulator
   - Click "Run" or press `Shift + F10`
   - Minimum Android version: 8.0 (API 26)

### Configuration

#### Permissions
The app requires the following permissions (already configured):
- `INTERNET` - For API calls
- `ACCESS_NETWORK_STATE` - Check network connectivity
- `POST_NOTIFICATIONS` - Show notifications
- `FOREGROUND_SERVICE` - Background order processing

#### API Configuration
- The app uses FakeStore API (no API key required)
- Base URL: `https://fakestoreapi.com/`
- Category: `men's clothing`

---

## 🔧 Key Components

### ViewModels

#### **UserViewModel**
- Handles user authentication
- Sign up and login functionality
- Session management
- Error handling

#### **ProductViewModel**
- Fetches products from API
- Combines local and API products
- Product filtering
- Loading states

#### **CartViewModel**
- Cart item management
- Quantity updates
- Price calculations
- Cart notifications

#### **FavoriteViewModel**
- Favorite product management
- Add/remove favorites
- Favorite count tracking

#### **OrderViewModel**
- Order creation
- Order history
- Order status management

#### **NotificationViewModel**
- Notification management
- Read/unread status
- Notification deletion

### Repositories

All repositories follow the same pattern:
- Provide clean API for ViewModels
- Handle data transformation
- Combine multiple data sources
- Use Kotlin Coroutines for async operations

### Services

#### **SendOrderService**
- Foreground service for order processing
- Generates unique order numbers
- Shows processing notifications
- Saves orders to SharedPreferences
- Auto-stops when complete

---

## 🔔 Services

### Background Order Service

The app includes a foreground service (`SendOrderService`) that processes orders in the background:

#### Features
- Receives order data (items, total, count)
- Generates unique order numbers (format: CLO + random numbers)
- Shows two notifications:
  1. "Processing order..." (immediate)
  2. "Order confirmed! #12345" (after delay)
- Saves orders to SharedPreferences
- Auto-stops when finished

#### Usage
```kotlin
val serviceIntent = Intent(context, SendOrderService::class.java).apply {
    val gson = Gson()
    putExtra(SendOrderService.EXTRA_ITEMS, gson.toJson(items))
    putExtra(SendOrderService.EXTRA_TOTAL, total)
    putExtra(SendOrderService.EXTRA_ITEM_COUNT, itemCount)
}

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    context.startForegroundService(serviceIntent)
} else {
    context.startService(serviceIntent)
}
```

---

## 🎨 Design System

### Colors
- **Primary**: `#434819` (Olive Green)
- **Text Primary**: `#1A1A1A` (Neutral 900)
- **Text Secondary**: `#999999` (Neutral 400)
- **Background**: `#FFFFFF` (White)
- **Error**: `#ED1010` (Red)
- **Grey**: `#E6E6E6` (Grey 100)

### Typography
- **Font Family**: General Sans
- **Weights**: Regular, Medium, SemiBold
- **Sizes**: 10sp - 24sp

### Components
- Material Design 3 components
- Custom card designs
- Rounded corners (10-12dp)
- Consistent spacing (8dp, 12dp, 16dp, 20dp, 24dp)

---

## 🔮 Future Enhancements

### Planned Features
- [ ] Payment gateway integration
- [ ] Address management UI
- [ ] Product reviews and ratings
- [ ] Order tracking with maps
- [ ] Push notifications
- [ ] Social login (Google, Facebook)
- [ ] Product recommendations
- [ ] Wishlist sharing
- [ ] Multi-language support
- [ ] Dark mode
- [ ] Image zoom in product details
- [ ] Product comparison
- [ ] Coupon/discount codes
- [ ] Order cancellation
- [ ] Refund management

### Technical Improvements
- [ ] Dependency injection with Hilt/Dagger
- [ ] Unit tests
- [ ] UI tests
- [ ] CI/CD pipeline
- [ ] Analytics integration
- [ ] Crash reporting
- [ ] Performance optimization
- [ ] Code obfuscation for release

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable names
- Add comments for complex logic
- Keep functions small and focused
- Follow MVVM architecture pattern

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 👨‍💻 Author

Developed with ❤️ using modern Android development practices.

---

## 🙏 Acknowledgments

- [FakeStore API](https://fakestoreapi.com/) for providing test product data
- Jetpack Compose team for the amazing UI framework
- Android developers community for best practices and guidance

---

<div align="center">

**Made with ❤️ using Jetpack Compose**

⭐ Star this repo if you find it helpful!

</div>

