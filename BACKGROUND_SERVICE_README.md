# Background Service Implementation - Order Processing

## Overview

This document provides a comprehensive overview of the background service implementation for processing and saving orders in the clothing app. The service handles order processing, notification management, and persistent storage using SharedPreferences.

## Architecture

### Components

1. **SendOrderService** - A foreground service that processes orders in the background
2. **OrderModels** - Data classes for order structure (SavedOrder, OrderItemData)
3. **NotificationModel** - Data class for notification structure (AppNotification)
4. **OrderReader** - Utility class for reading saved orders from SharedPreferences
5. **NotificationReader** - Utility class for managing notifications in SharedPreferences
6. **CheckoutScreen Integration** - UI component that triggers the service
7. **NotificationsScreen** - UI component that displays saved notifications

## Service Features

### Order Processing Flow

1. **Service Start**: The service is started from the CheckoutScreen when user places an order
2. **Data Reception**: Service receives order data via Intent extras:
   - Items list (JSON format)
   - Total amount
   - Item count
3. **Foreground Notification**: Service immediately shows a "Processing order..." notification
4. **Order Processing**: 
   - Simulates processing delay (2 seconds)
   - Generates unique order number (format: CLO + 5 random digits)
   - Creates order object with all details
5. **Data Persistence**: Saves order to SharedPreferences under key "clothing_orders"
6. **Notification Saving**: Automatically saves both notifications to SharedPreferences
7. **Confirmation Notification**: Shows "Order confirmed! #CLO12345" notification
8. **Auto-Stop**: Service automatically stops after completing the task

### Order Data Structure

Each saved order contains:
- **Order Number**: Unique identifier (e.g., CLO12345)
- **Total Amount**: Final order total
- **Item Count**: Total quantity of items
- **Items Array**: List of all order items with details
- **Date**: Timestamp when order was placed
- **Status**: Order status (e.g., "Confirmed")

### Order Item Structure

Each item in an order contains:
- Product ID
- Product Name
- Description
- Price
- Quantity
- Image URL (for API products) or Resource ID (for local products)

## Storage Mechanism

### SharedPreferences

- **Key**: "clothing_orders"
- **Format**: JSON array of SavedOrder objects
- **Location**: App's private SharedPreferences storage
- **Persistence**: Data persists across app restarts

### Data Management

- Orders are stored as a JSON array
- New orders are appended to existing orders
- All orders are preserved (no deletion on app restart)
- Orders can be retrieved, filtered, and sorted
- Orders can be deleted via swipe-to-delete in SavedOrdersScreen

### Notification Storage

- **Key**: "app_notifications"
- **Format**: JSON array of AppNotification objects
- **Location**: App's private SharedPreferences storage
- **Persistence**: Notifications persist across app restarts
- **Limit**: Automatically keeps only last 100 notifications
- **Auto-Save**: All system notifications are automatically saved

## Notification System

### Notification Channel

- **Channel ID**: "order_processing_channel"
- **Channel Name**: "Order Processing"
- **Importance**: High priority
- **Description**: Notifications for order processing

### Notification Types

1. **Processing Notification** (ID: 1001)
   - Title: "Processing order..."
   - Content: "Your order is being processed"
   - Type: Ongoing (cannot be dismissed)
   - Shows immediately when service starts
   - **Saved to app**: Yes - appears in NotificationsScreen

2. **Confirmation Notification** (ID: 1002)
   - Title: "Order confirmed! #CLO12345"
   - Content: "Your order has been successfully placed"
   - Type: Auto-dismissible
   - Shows after order is processed (2 seconds delay)
   - **Saved to app**: Yes - appears in NotificationsScreen

### Notification Storage

All notifications shown by the service are automatically saved to SharedPreferences and displayed in the app's NotificationsScreen:

- **Storage Key**: "app_notifications"
- **Format**: JSON array of AppNotification objects
- **Location**: App's private SharedPreferences storage
- **Persistence**: Notifications persist across app restarts
- **Limit**: Keeps last 100 notifications to prevent storage issues

### Notification Data Structure

Each saved notification contains:
- **ID**: Unique identifier (UUID)
- **Title**: Notification title
- **Message**: Notification content/message
- **Timestamp**: When notification was created
- **Type**: Notification type ("processing", "confirmation", etc.)

## Integration Points

### CheckoutScreen

- User clicks "Place Order" button
- Service is started with order data
- Toast message confirms service start
- User sees processing notification
- After 2 seconds, confirmation notification appears

### OrderAcceptedScreen

- "View My Orders" button navigates to SavedOrdersScreen
- Allows users to view all their saved orders

### SavedOrdersScreen

- Displays all saved orders from SharedPreferences
- Shows order details: number, date, total, items, status
- Orders sorted by date (newest first)
- Swipe-to-delete functionality for removing orders

### NotificationsScreen

- Displays all saved notifications from SharedPreferences
- Shows notification details: title, message, timestamp
- Notifications sorted by date (newest first)
- Color-coded icons (green for confirmation, olive for processing)
- Empty state shown when no notifications exist

## Utility Functions

### OrderReader Class

Provides multiple methods to read orders:

1. **getAllOrders()**: Retrieves all saved orders
2. **getOrderByNumber()**: Finds specific order by order number
3. **getOrdersSortedByDate()**: Returns orders sorted by date (newest first)
4. **getOrdersByStatus()**: Filters orders by status
5. **getOrderCount()**: Returns total number of saved orders
6. **deleteOrder()**: Deletes an order by order number

### NotificationReader Class

Provides methods to manage notifications:

1. **getAllNotifications()**: Retrieves all saved notifications
2. **getNotificationsSortedByDate()**: Returns notifications sorted by date (newest first)
3. **getNotificationCount()**: Returns total number of saved notifications
4. **saveNotification()**: Saves a new notification to SharedPreferences
5. **clearAllNotifications()**: Deletes all saved notifications

## Permissions Required

### AndroidManifest.xml

- **POST_NOTIFICATIONS**: Required for Android 13+ to show notifications
- **FOREGROUND_SERVICE**: Required for foreground service execution
- **Service Registration**: SendOrderService registered in manifest

## Service Lifecycle

1. **onCreate()**: Creates notification channel
2. **onStartCommand()**: 
   - Receives order data
   - Starts foreground service
   - Launches background processing coroutine
   - Returns START_NOT_STICKY (doesn't restart if killed)
3. **processOrder()**: 
   - Processes order data
   - Generates order number
   - Saves to SharedPreferences
   - Shows confirmation notification
4. **stopSelf()**: Service stops automatically after completion

## Error Handling

- Null intent handling: Service stops if intent is null
- JSON parsing errors: Catches and logs parsing exceptions
- SharedPreferences errors: Logs save failures
- Try-catch blocks: Prevents crashes during processing

## Logging

Service includes comprehensive logging:
- Service start/stop events
- Order data reception
- Order number generation
- Save operations
- Notification display
- Error conditions

Log tag: "SendOrderService"

## Testing

### How to Verify Service is Working

1. **Place an Order**: Go to Cart → Checkout → Place Order
2. **Check Toast**: Should see "Processing your order..." toast
3. **Check Notifications**: 
   - First: "Processing order..." (immediate)
   - Second: "Order confirmed! #CLO12345" (after 2 seconds)
4. **View Orders**: Navigate to "View My Orders" to see saved order
5. **Check Logcat**: Filter by "SendOrderService" to see service logs

### Expected Behavior

- Service starts immediately when order is placed
- Processing notification appears right away
- Confirmation notification appears after 2 seconds
- Order is saved to SharedPreferences
- Order appears in SavedOrdersScreen
- Service stops automatically

## Benefits

1. **Non-Blocking**: Order processing happens in background
2. **User Feedback**: Notifications keep user informed
3. **Data Persistence**: Orders saved even if app closes
4. **Reliable**: Foreground service ensures processing completes
5. **Scalable**: Can handle multiple orders
6. **Traceable**: Each order has unique number

## Future Enhancements

Potential improvements:
- Order status updates (Processing → Shipped → Delivered)
- Order cancellation functionality
- Order history with filtering and search
- Sync with backend server
- Order tracking integration
- Email/SMS notifications

## File Locations

- **Service**: `app/src/main/java/com/example/app_ui/service/SendOrderService.kt`
- **Order Models**: `app/src/main/java/com/example/app_ui/service/OrderModels.kt`
- **Notification Model**: `app/src/main/java/com/example/app_ui/service/NotificationModel.kt`
- **Order Reader Utility**: `app/src/main/java/com/example/app_ui/utils/OrderReader.kt`
- **Notification Reader Utility**: `app/src/main/java/com/example/app_ui/utils/NotificationReader.kt`
- **Integration**: `app/src/main/java/com/example/app_ui/screens/CheckoutScreen.kt`
- **Orders Display**: `app/src/main/java/com/example/app_ui/screens/SavedOrdersScreen.kt`
- **Notifications Display**: `app/src/main/java/com/example/app_ui/screens/NotificationsScreen.kt`
- **Manifest**: `app/src/main/AndroidManifest.xml`

## Notification Integration

### Automatic Notification Saving

When the service displays system notifications, it automatically saves them to the app's notification storage:

1. **Processing Notification**: Saved when order processing starts
2. **Confirmation Notification**: Saved when order is confirmed

### Notification Display

- All saved notifications appear in the **NotificationsScreen**
- Users can view their complete notification history
- Notifications are sorted by date (newest first)
- Each notification shows:
  - Icon (color-coded by type)
  - Title
  - Message/Content
  - Timestamp

### Benefits

- **Complete History**: Users can see all past notifications
- **Persistent Storage**: Notifications saved even if app is closed
- **User-Friendly**: Easy access to notification history
- **Automatic**: No manual action required - notifications auto-save

## Summary

The background service provides a robust solution for processing orders asynchronously while keeping users informed through notifications. Orders are persistently stored in SharedPreferences and can be retrieved and displayed at any time. Additionally, all system notifications are automatically saved and displayed in the app's NotificationsScreen, providing users with a complete notification history. The implementation follows Android best practices for foreground services and notification management.

