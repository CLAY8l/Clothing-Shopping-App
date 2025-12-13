# 🚀 Firebase Quick Start Guide

This guide will help you quickly start using Firebase in your app after the integration is complete.

## ✅ What Has Been Done

1. ✅ **Gradle Files Updated**
   - Added Firebase BOM and dependencies
   - Added Google Services plugin

2. ✅ **Firebase Services Created**
   - `FirestoreService.kt` - Database operations
   - `StorageService.kt` - Image storage operations
   - `FirebaseMigrationHelper.kt` - Data migration tool
   - `FirebaseUserHelper.kt` - User ID management
   - `FirebaseCartRepository.kt` - Example hybrid repository

3. ✅ **AppApplication Updated**
   - Firebase initialization added

## 📋 Next Steps

### 1. Add google-services.json

Make sure you have downloaded `google-services.json` from Firebase Console and placed it in:
```
app/google-services.json
```

### 2. Sync Gradle

In Android Studio:
- Click **File → Sync Project with Gradle Files**
- Wait for dependencies to download

### 3. Update UserViewModel to Save User ID

When user logs in, save their ID for Firestore operations:

```kotlin
// In UserViewModel.kt, after successful login
fun login(email: String, password: String) {
    viewModelScope.launch {
        // ... existing login code ...
        
        // After successful login, save user ID
        val userId = user.email // or use Firebase Auth UID
        FirebaseUserHelper.setCurrentUserId(context, userId)
        FirebaseUserHelper.setCurrentUserEmail(context, email)
        
        // Optionally migrate existing data to Firestore
        FirebaseMigrationHelper.migrateToFirebase(context, userId)
    }
}
```

### 4. Update AppApplication to Use Firebase Repository (Optional)

If you want to use the hybrid repository that syncs with Firestore:

```kotlin
// In AppApplication.kt
val cartRepository by lazy { 
    FirebaseCartRepository(database.productCartDao(), this) 
}
```

Or keep using the existing `CartRepository` and manually sync when needed.

### 5. Set Up Firestore Security Rules

Go to Firebase Console → Firestore Database → Rules and paste:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

**Note**: This is a basic rule. Update it based on your security needs (see FIREBASE_INTEGRATION_GUIDE.md for detailed rules).

### 6. Set Up Storage Rules

Go to Firebase Console → Storage → Rules and paste:

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

## 🧪 Testing

### Test Firestore Connection

1. Run the app
2. Login with a user
3. Add items to cart
4. Check Firebase Console → Firestore Database
5. You should see data in the `cart` collection

### Test Storage

```kotlin
// Example: Upload a product image
val imageUri = Uri.parse("your_image_path")
val downloadUrl = StorageService.uploadProductImage(imageUri, "product123")
println("Image URL: $downloadUrl")
```

## 📝 Usage Examples

### Save Cart Item to Firestore

```kotlin
// In CartViewModel or wherever you add items
viewModelScope.launch {
    // Save to Room (local)
    cartRepository.addOrUpdateCartItem(...)
    
    // If using FirebaseCartRepository, it syncs automatically
    // Otherwise, manually sync:
    val userId = FirebaseUserHelper.getCurrentUserId(context)
    if (userId != null) {
        val itemData = mapOf(
            "productId" to productId,
            "name" to name,
            "price" to price,
            "quantity" to quantity
        )
        FirestoreService.addCartItem(userId, itemId, itemData)
    }
}
```

### Get Cart Items from Firestore

```kotlin
val userId = FirebaseUserHelper.getCurrentUserId(context)
if (userId != null) {
    val items = FirestoreService.getCartItems(userId)
    // Process items
}
```

### Upload Product Image

```kotlin
val imageUri = Uri.fromFile(File("path/to/image.jpg"))
val downloadUrl = StorageService.uploadProductImage(imageUri, productId)
// Use downloadUrl in your product data
```

## 🔄 Migration Strategy

### Option 1: Hybrid Approach (Recommended)
- Keep Room for offline support
- Sync to Firestore when user is logged in
- Use `FirebaseCartRepository` as example

### Option 2: Full Migration
- Migrate all data to Firestore
- Use Firestore's offline persistence
- Keep Room only for initial migration

### Option 3: Gradual Migration
- Start with one feature (e.g., cart)
- Test thoroughly
- Gradually migrate other features

## 🐛 Troubleshooting

### Issue: "google-services.json not found"
- Make sure the file is in `app/` directory (not `app/src/main/`)
- Sync Gradle after adding

### Issue: "Permission denied" in Firestore
- Check Firestore Security Rules
- Make sure user is authenticated (if using auth)

### Issue: Build fails
- Check if `google-services` plugin is in project-level `build.gradle.kts`
- Ensure all dependencies are synced
- Clean and rebuild project

### Issue: Data not syncing
- Check if user ID is saved: `FirebaseUserHelper.getCurrentUserId(context)`
- Check Firebase Console for errors
- Verify internet connection

## 📚 Next Steps

1. **Enable Firebase Authentication** (optional but recommended)
   - See FIREBASE_INTEGRATION_GUIDE.md for details
   - Replace Room-based auth with Firebase Auth

2. **Add Real-time Listeners**
   - Use Firestore's real-time listeners for live updates
   - Example: `db.collection("cart").addSnapshotListener { ... }`

3. **Enable Offline Persistence**
   - Firestore has built-in offline support
   - Enable it: `FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true)`

4. **Add Error Handling**
   - Handle network errors gracefully
   - Show user-friendly error messages

## 📖 Documentation

- Full integration guide: `FIREBASE_INTEGRATION_GUIDE.md`
- Firebase Documentation: https://firebase.google.com/docs
- Firestore Guide: https://firebase.google.com/docs/firestore
- Storage Guide: https://firebase.google.com/docs/storage

---

**Ready to go!** 🎉 Your app is now connected to Firebase. Start by testing the cart sync functionality.

