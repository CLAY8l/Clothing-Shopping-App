# How to Test if the Order Service is Working

## Quick Test Steps:

### 1. **Place an Order**
   - Go to Cart Screen
   - Click "Checkout"
   - Fill in the checkout form
   - Click "Place Order" button
   - You should see a toast message: "Processing your order..."

### 2. **Check Notifications**
   - After clicking "Place Order", you should see **2 notifications**:
     - **First**: "Processing order..." (appears immediately)
     - **Second**: "Order confirmed! #CLO12345" (appears after 2 seconds)
   - The order number format is: `CLO` + 5 random digits (e.g., CLO12345)

### 3. **View Saved Orders**
   - After placing an order, you'll be on the "Order Accepted" screen
   - Click **"View My Orders"** button
   - You should see your order listed with:
     - Order number
     - Date and time
     - Total amount
     - Item count
     - Status (Confirmed)

### 4. **Check Logcat (For Developers)**
   Open Android Studio Logcat and filter by `SendOrderService`:
   ```
   Tag: SendOrderService
   ```
   
   You should see logs like:
   - "Service started with order data: total=XXX, itemCount=X"
   - "Processing order..."
   - "Generated order number: CLO12345"
   - "Order saved to SharedPreferences: CLO12345"
   - "Order saved successfully. Total orders: X"
   - "Confirmation notification shown"
   - "Service stopped"

### 5. **Verify SharedPreferences**
   The orders are saved in SharedPreferences with key: `"clothing_orders"`
   
   You can verify using Android Studio's Device File Explorer:
   - Navigate to: `/data/data/com.example.app_ui/shared_prefs/clothing_orders.xml`
   - Or use ADB command:
     ```bash
     adb shell run-as com.example.app_ui cat shared_prefs/clothing_orders.xml
     ```

## What to Look For:

✅ **Service is Working if:**
- Toast appears when clicking "Place Order"
- Two notifications appear (Processing → Confirmed)
- Order appears in "View My Orders" screen
- Logcat shows service logs
- Order number is generated (format: CLO + 5 digits)

❌ **Service is NOT Working if:**
- No toast appears
- No notifications appear
- Order doesn't appear in saved orders
- App crashes when clicking "Place Order"
- Logcat shows errors

## Troubleshooting:

1. **No Notifications?**
   - Check if notification permission is granted (Android 13+)
   - Go to Settings → Apps → Your App → Notifications → Enable

2. **Service Not Starting?**
   - Check Logcat for errors
   - Verify service is registered in AndroidManifest.xml
   - Check if foreground service permission is granted

3. **Orders Not Saving?**
   - Check Logcat for "Order saved successfully" message
   - Verify SharedPreferences key is correct: "clothing_orders"
   - Check if there are any JSON parsing errors in logs

4. **Can't See Orders Screen?**
   - Navigate to: Order Accepted Screen → Click "View My Orders"
   - Or manually navigate to route: "saved_orders"

## Testing Multiple Orders:

1. Place multiple orders (add items to cart, checkout, repeat)
2. Each order should have a unique order number
3. All orders should appear in "View My Orders" screen
4. Orders should be sorted by date (newest first)

## Code Locations:

- **Service**: `app/src/main/java/com/example/app_ui/service/SendOrderService.kt`
- **Checkout Button**: `app/src/main/java/com/example/app_ui/screens/CheckoutScreen.kt` (line 213-225)
- **Read Orders**: `app/src/main/java/com/example/app_ui/utils/OrderReader.kt`
- **View Orders Screen**: `app/src/main/java/com/example/app_ui/screens/SavedOrdersScreen.kt`

