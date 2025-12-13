# Where Data is Stored in Firebase

This document explains where all your app data is stored in Firebase.

## 📍 Overview

Your app uses **two Firebase services** to store data:

1. **Firestore Database** - For structured data (users, cart, favorites, orders, notifications)
2. **Firebase Storage** - For files and images

---

## 🗄️ Firestore Database Structure

All structured data is stored in **Firestore** (Cloud Firestore). Here's the complete structure:

### Project Location
- **Project ID**: `clothing-shopping-app-ff58a`
- **Database**: Firestore Database

### Collections Structure

```
Firestore Database
│
├── 📁 users/
│   └── 📄 {userId}/
│       ├── id: "user-id-123"
│       ├── email: "user@example.com"
│       ├── name: "John Doe"
│       └── password: "hashed-password"
│
├── 📁 cart/
│   └── 📄 {userId}/
│       └── 📁 items/
│           └── 📄 {itemId}/
│               ├── id: "item-id"
│               ├── productId: "product-123"
│               ├── name: "Product Name"
│               ├── price: 29.99
│               ├── quantity: 2
│               ├── imageUrl: "https://..."
│               └── addedAt: timestamp
│
├── 📁 favorites/
│   └── 📄 {userId}/
│       └── 📁 products/
│           └── 📄 {productId}/
│               ├── id: "product-123"
│               ├── name: "Product Name"
│               ├── price: 29.99
│               ├── imageUrl: "https://..."
│               └── savedAt: timestamp
│
├── 📁 orders/
│   └── 📄 {userId}/
│       └── 📁 orders/
│           └── 📄 {orderId}/
│               ├── id: "order-123"
│               ├── orderNumber: "CLO12345"
│               ├── total: 99.99
│               ├── itemCount: 3
│               ├── date: timestamp
│               ├── status: "confirmed"
│               └── items: [array of items]
│
└── 📁 notifications/
    └── 📄 {userId}/
        └── 📁 notifications/
            └── 📄 {notificationId}/
                ├── id: "notif-123"
                ├── title: "Order Confirmed"
                ├── message: "Your order #12345 has been confirmed"
                ├── timestamp: timestamp
                └── isRead: false
```

---

## 📦 Firebase Storage Structure

All images and files are stored in **Firebase Storage**. Here's the structure:

### Storage Bucket
- **Bucket**: `clothing-shopping-app-ff58a.firebasestorage.app`

### Storage Paths

```
Firebase Storage
│
├── 📁 product_images/
│   └── 📁 {productId}/
│       └── 📄 {uuid}.jpg
│           (Product images uploaded by users)
│
└── 📁 user_profiles/
    └── 📁 {userId}/
        └── 📄 profile.jpg
            (User profile pictures)
```

---

## 🔍 How to View Your Data

### View Firestore Data

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project: **clothing-shopping-app-ff58a**
3. Click on **"Firestore Database"** in the left menu
4. You'll see all collections:
   - `users`
   - `cart`
   - `favorites`
   - `orders`
   - `notifications`

5. Click on any collection to see documents
6. Click on a document to see its fields and values

### View Storage Files

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project: **clothing-shopping-app-ff58a**
3. Click on **"Storage"** in the left menu
4. You'll see folders:
   - `product_images/`
   - `user_profiles/`
5. Navigate into folders to see uploaded files

---

## 📊 Data Flow

### When User Signs Up
```
App → FirestoreService.saveUser()
     ↓
Firestore: users/{userId}
```

### When User Adds to Cart
```
App → CartRepository.addToCart()
     ↓
Room Database (local) + Firestore: cart/{userId}/items/{itemId}
```

### When User Places Order
```
App → OrderRepository.saveOrder()
     ↓
Room Database (local) + Firestore: orders/{userId}/orders/{orderId}
```

### When Image is Uploaded
```
App → StorageService.uploadProductImage()
     ↓
Firebase Storage: product_images/{productId}/{uuid}.jpg
```

---

## 🔑 Important Notes

1. **User ID is the Key**: All user-specific data is organized under their `userId`
   - Each user has their own cart, favorites, orders, and notifications

2. **Dual Storage**: The app uses both:
   - **Room Database** (local) - for offline access and fast queries
   - **Firestore** (cloud) - for backup and sync across devices

3. **Data Migration**: When a user logs in, their local Room data is automatically migrated to Firestore

4. **Storage URLs**: When images are uploaded to Storage, the download URL is saved in Firestore documents

---

## 📝 Example: Complete Data Path

For a user with ID `user-123` who adds a product to cart:

**Firestore Path:**
```
cart/user-123/items/item-456
```

**Storage Path (if image uploaded):**
```
product_images/product-789/uuid-abc.jpg
```

**Firestore Document Fields:**
```json
{
  "id": "item-456",
  "productId": "product-789",
  "name": "T-Shirt",
  "price": 29.99,
  "quantity": 1,
  "imageUrl": "https://firebasestorage.googleapis.com/.../uuid-abc.jpg",
  "addedAt": "2024-01-15T10:30:00Z"
}
```

---

## 🛠️ Accessing Data Programmatically

### Get User Data
```kotlin
val userData = FirestoreService.getUser(userId)
```

### Get Cart Items
```kotlin
val cartItems = FirestoreService.getCartItems(userId)
```

### Get Orders
```kotlin
val orders = FirestoreService.getOrders(userId)
```

### Get Image URL
```kotlin
val imageUrl = StorageService.getProductImageUrl(productId, "image.jpg")
```

---

## 🔒 Security

- **Firestore Rules**: Control who can read/write data
- **Storage Rules**: Control who can upload/download files
- Currently using **test mode** (open access for development)

---

## 📱 Real-time Updates

Firestore automatically syncs data in real-time:
- If data changes in Firestore, your app can listen and update
- Changes made on one device appear on other devices (if logged in with same user)

---

## 💡 Summary

| Data Type | Storage Location | Collection/Path |
|-----------|-----------------|-----------------|
| User Info | Firestore | `users/{userId}` |
| Cart Items | Firestore | `cart/{userId}/items/{itemId}` |
| Favorites | Firestore | `favorites/{userId}/products/{productId}` |
| Orders | Firestore | `orders/{userId}/orders/{orderId}` |
| Notifications | Firestore | `notifications/{userId}/notifications/{notificationId}` |
| Product Images | Storage | `product_images/{productId}/{uuid}.jpg` |
| Profile Images | Storage | `user_profiles/{userId}/profile.jpg` |

---

**Need Help?**
- Check Firebase Console: https://console.firebase.google.com/
- View your project: `clothing-shopping-app-ff58a`
- All data is organized by `userId` for user-specific data

