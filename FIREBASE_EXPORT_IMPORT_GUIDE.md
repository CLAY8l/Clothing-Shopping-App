# How to Export/Import Firebase Data

This guide explains how to export (backup) and import (restore) your Firebase data.

---

## 📤 Export Data from Firebase

### Method 1: Export from Firebase Console (Easiest)

#### Export Firestore Data

1. **Go to Firebase Console**
   - Visit: https://console.firebase.google.com/
   - Select your project: `clothing-shopping-app-ff58a`

2. **Open Firestore Database**
   - Click on **"Firestore Database"** in the left menu

3. **Export Data**
   - Click on the **three dots (⋮)** menu at the top
   - Select **"Export"** or **"Export collection"**
   - Choose export format (JSON or CSV)
   - Click **"Export"**
   - The file will download to your computer

#### Export Storage Files

1. **Open Storage**
   - Click on **"Storage"** in the left menu

2. **Download Files**
   - Navigate to the folder you want (e.g., `product_images/`)
   - Click on individual files to download them
   - Or use the **"Download"** button for folders

### Method 2: Using Firebase CLI (Advanced)

#### Install Firebase CLI

```bash
# Install Node.js first (if not installed)
# Then install Firebase CLI
npm install -g firebase-tools

# Login to Firebase
firebase login

# Initialize Firebase in your project
firebase init
```

#### Export Firestore Data

```bash
# Export all Firestore data
firebase firestore:export gs://your-bucket-name/backup

# Or export to local directory
firebase firestore:export ./backup
```

#### Export Storage Files

```bash
# Download all storage files
gsutil -m cp -r gs://clothing-shopping-app-ff58a.firebasestorage.app ./storage-backup
```

---

## 📥 Import Data to Firebase

### Method 1: Import via Firebase Console

#### Import Firestore Data

1. **Go to Firebase Console**
   - Visit: https://console.firebase.google.com/
   - Select your project

2. **Open Firestore Database**
   - Click on **"Firestore Database"**

3. **Import Data**
   - Click on the **three dots (⋮)** menu
   - Select **"Import"**
   - Choose your exported JSON file
   - Click **"Import"**

⚠️ **Warning**: Importing will **overwrite** existing data with the same document IDs!

### Method 2: Using Firebase CLI

#### Import Firestore Data

```bash
# Import from backup
firebase firestore:import gs://your-bucket-name/backup

# Or import from local directory
firebase firestore:import ./backup
```

#### Import Storage Files

```bash
# Upload files to Storage
gsutil -m cp -r ./storage-backup/* gs://clothing-shopping-app-ff58a.firebasestorage.app/
```

---

## 🔄 Backup and Restore Complete Project

### Create Full Backup

1. **Export Firestore**
   - Use Method 1 or 2 above to export Firestore data

2. **Export Storage**
   - Download all files from Storage

3. **Save Configuration**
   - Save your `google-services.json` file
   - Note your project ID and settings

### Restore Full Backup

1. **Import Firestore**
   - Import your exported Firestore data

2. **Upload Storage Files**
   - Upload all files back to Storage

3. **Verify Data**
   - Check that all collections and documents are restored
   - Verify images are accessible

---

## 📋 Step-by-Step: Export Your App Data

### Step 1: Export Users Collection

1. Go to Firebase Console → Firestore Database
2. Click on `users` collection
3. Click **"Export"** (three dots menu)
4. Save as `users_backup.json`

### Step 2: Export Cart Data

1. Click on `cart` collection
2. For each user document, click on `items` subcollection
3. Export each subcollection
4. Save as `cart_backup.json`

### Step 3: Export Favorites

1. Click on `favorites` collection
2. Export each user's `products` subcollection
3. Save as `favorites_backup.json`

### Step 4: Export Orders

1. Click on `orders` collection
2. Export each user's `orders` subcollection
3. Save as `orders_backup.json`

### Step 5: Export Notifications

1. Click on `notifications` collection
2. Export each user's `notifications` subcollection
3. Save as `notifications_backup.json`

### Step 6: Download Storage Files

1. Go to Storage
2. Navigate to `product_images/` folder
3. Download all images
4. Navigate to `user_profiles/` folder
5. Download all profile images

---

## 🛠️ Using Firebase CLI (Detailed)

### Install Firebase CLI

**Windows:**
```powershell
# Install Node.js first from nodejs.org
# Then install Firebase CLI
npm install -g firebase-tools
```

**Mac/Linux:**
```bash
npm install -g firebase-tools
```

### Login and Initialize

```bash
# Login to Firebase
firebase login

# Navigate to your project directory
cd D:\APP_UI

# Initialize Firebase (if not already done)
firebase init firestore
firebase init storage
```

### Export Commands

```bash
# Export Firestore to Google Cloud Storage
firebase firestore:export gs://clothing-shopping-app-ff58a.appspot.com/backup-$(date +%Y%m%d)

# Export to local directory
firebase firestore:export ./firestore-backup
```

### Import Commands

```bash
# Import Firestore from backup
firebase firestore:import gs://clothing-shopping-app-ff58a.appspot.com/backup-20240115

# Import from local directory
firebase firestore:import ./firestore-backup
```

---

## 📦 Export as JSON File

### Manual Export (One Collection at a Time)

1. **In Firebase Console:**
   - Go to Firestore Database
   - Click on a collection (e.g., `users`)
   - Click on a document
   - Copy the JSON structure
   - Save to a `.json` file

2. **Or use Export Feature:**
   - Click three dots (⋮) → Export
   - Choose JSON format
   - Download the file

### Example JSON Structure

```json
{
  "users": {
    "user-123": {
      "id": "user-123",
      "email": "user@example.com",
      "name": "John Doe",
      "password": "hashed-password"
    }
  },
  "cart": {
    "user-123": {
      "items": {
        "item-456": {
          "id": "item-456",
          "productId": "product-789",
          "name": "T-Shirt",
          "price": 29.99,
          "quantity": 1
        }
      }
    }
  }
}
```

---

## 🔐 Backup Security Rules

### Export Security Rules

```bash
# Export Firestore rules
firebase firestore:rules:get > firestore.rules

# Export Storage rules
firebase storage:rules:get > storage.rules
```

### Import Security Rules

```bash
# Import Firestore rules
firebase firestore:rules:set firestore.rules

# Import Storage rules
firebase storage:rules:set storage.rules
```

---

## 📱 Export for Development/Testing

### Export Test Data

1. **Create Test Collection:**
   - In Firestore, create a collection called `test_data`
   - Add sample documents

2. **Export Test Collection:**
   - Export only the `test_data` collection
   - Use this for testing imports

3. **Import to Another Project:**
   - Create a new Firebase project for testing
   - Import the test data
   - Verify everything works

---

## ⚠️ Important Warnings

1. **Backup Before Import:**
   - Always export current data before importing
   - Importing will overwrite existing data

2. **Document IDs:**
   - If document IDs match, they will be overwritten
   - Use unique IDs to avoid conflicts

3. **Storage Files:**
   - Download Storage files separately
   - They are not included in Firestore exports

4. **Large Datasets:**
   - For large datasets, use Firebase CLI
   - Console export may timeout for very large collections

---

## 🎯 Quick Reference

| Action | Method | Command/Location |
|--------|--------|------------------|
| **Export Firestore** | Console | Firestore → Three dots → Export |
| **Export Firestore** | CLI | `firebase firestore:export` |
| **Import Firestore** | Console | Firestore → Three dots → Import |
| **Import Firestore** | CLI | `firebase firestore:import` |
| **Download Storage** | Console | Storage → Select files → Download |
| **Upload Storage** | Console | Storage → Upload files |
| **Backup Rules** | CLI | `firebase firestore:rules:get` |

---

## 💡 Tips

1. **Regular Backups:**
   - Export data regularly (weekly/monthly)
   - Store backups in a safe location

2. **Version Control:**
   - Name backups with dates: `backup-2024-01-15.json`
   - Keep multiple backup versions

3. **Test Imports:**
   - Test imports on a development project first
   - Verify data integrity before production import

4. **Automate Backups:**
   - Use Firebase CLI with cron jobs (Linux/Mac)
   - Or Windows Task Scheduler for automated backups

---

## 📞 Need Help?

- **Firebase Console**: https://console.firebase.google.com/
- **Firebase CLI Docs**: https://firebase.google.com/docs/cli
- **Firestore Export**: https://firebase.google.com/docs/firestore/manage-data/export-import

---

**Your Project Details:**
- **Project ID**: `clothing-shopping-app-ff58a`
- **Storage Bucket**: `clothing-shopping-app-ff58a.firebasestorage.app`

