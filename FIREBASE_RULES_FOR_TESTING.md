# 🔓 Firebase Security Rules for Testing (Without Firebase Auth)

Since you're currently using Room-based authentication (not Firebase Auth), you need to use **permissive rules** for testing. These rules allow read/write access without authentication.

## ⚠️ IMPORTANT: These are TEST rules only!

**DO NOT use these in production!** They allow anyone to read and write your data.

---

## 🔥 Firestore Rules (For Testing)

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project: **clothing-shopping-app-ff58a**
3. Click **Firestore Database** → **Rules** tab
4. Replace with:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

5. Click **Publish**

---

## 📦 Storage Rules (For Testing)

1. In Firebase Console, click **Storage** → **Rules** tab
2. Replace with:

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true;
    }
  }
}
```

3. Click **Publish**

---

## ✅ What This Allows

- ✅ **Read access**: Anyone can read your data
- ✅ **Write access**: Anyone can write to your database
- ✅ **Works without Firebase Auth**: Perfect for testing with Room-based auth

---

## 🚨 Security Warning

These rules are **ONLY for development/testing**. 

**Before going to production**, you should:
1. Implement Firebase Authentication
2. Update rules to require authentication
3. Use proper user-based access control

---

## 🔄 When You're Ready for Production

Once you implement Firebase Authentication, update the rules to:

### Firestore Rules (Production):
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    match /cart/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    match /favorites/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    match /orders/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    match /notifications/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

### Storage Rules (Production):
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /product_images/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null;
    }
    match /user_profiles/{userId}/{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

---

**For now, use the test rules above to get started!** 🚀

