package com.example.app_ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.app_ui.MainActivity
import com.example.app_ui.R
import com.example.app_ui.data.database.AppDatabase
import com.example.app_ui.data.entity.Notification
import com.example.app_ui.data.repository.NotificationRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log

class SendOrderService : Service() {
    
    private val TAG = "SendOrderService"
    
    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("clothing_orders", Context.MODE_PRIVATE)
    }
    
    private val database by lazy {
        AppDatabase.getDatabase(this)
    }
    
    private val notificationRepository by lazy {
        NotificationRepository(database.notificationDao())
    }
    
    private val gson = Gson()
    
    companion object {
        private const val CHANNEL_ID = "order_processing_channel"
        private const val NOTIFICATION_ID_PROCESSING = 1001
        private const val NOTIFICATION_ID_CONFIRMED = 1002
        
        const val EXTRA_ITEMS = "extra_items"
        const val EXTRA_TOTAL = "extra_total"
        const val EXTRA_ITEM_COUNT = "extra_item_count"
        
        private fun generateOrderNumber(): String {
            val randomNumbers = (10000..99999).random()
            return "CLO$randomNumbers"
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            Log.e(TAG, "Service started with null intent")
            stopSelf()
            return START_NOT_STICKY
        }
        
        // Get order data from intent
        val itemsJson = intent.getStringExtra(EXTRA_ITEMS) ?: ""
        val total = intent.getDoubleExtra(EXTRA_TOTAL, 0.0)
        val itemCount = intent.getIntExtra(EXTRA_ITEM_COUNT, 0)
        
        Log.d(TAG, "Service started with order data: total=$total, itemCount=$itemCount")
        
        // Parse items
        val items = try {
            val type = object : TypeToken<List<OrderItemData>>() {}.type
            gson.fromJson<List<OrderItemData>>(itemsJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
        
        // Start foreground service with processing notification
        startForeground(NOTIFICATION_ID_PROCESSING, createProcessingNotification())
        
        // Process order in background
        CoroutineScope(Dispatchers.IO).launch {
            processOrder(items, total, itemCount)
        }
        
        return START_NOT_STICKY
    }
    
    private suspend fun processOrder(
        items: List<OrderItemData>,
        total: Double,
        itemCount: Int
    ) {
        Log.d(TAG, "Processing order...")
        
        // Simulate processing delay (2-3 seconds)
        delay(2000)
        
        // Generate unique order number
        val orderNumber = generateOrderNumber()
        Log.d(TAG, "Generated order number: $orderNumber")
        
        // Create order data
        val order = SavedOrder(
            orderNumber = orderNumber,
            total = total,
            itemCount = itemCount,
            items = items,
            date = System.currentTimeMillis(),
            status = "Confirmed"
        )
        
        // Save to SharedPreferences
        saveOrderToSharedPreferences(order)
        Log.d(TAG, "Order saved to SharedPreferences: $orderNumber")
        
        // Show confirmation notification
        showConfirmationNotification(orderNumber)
        Log.d(TAG, "Confirmation notification shown")
        
        // Stop service
        stopForeground(true)
        stopSelf()
        Log.d(TAG, "Service stopped")
    }
    
    private fun saveOrderToSharedPreferences(order: SavedOrder) {
        val existingOrdersJson = sharedPreferences.getString("clothing_orders", "[]") ?: "[]"
        val type = object : TypeToken<List<SavedOrder>>() {}.type
        val existingOrders = try {
            gson.fromJson<List<SavedOrder>>(existingOrdersJson, type) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing existing orders: ${e.message}")
            emptyList()
        }
        
        val updatedOrders = existingOrders.toMutableList()
        updatedOrders.add(order)
        
        val updatedOrdersJson = gson.toJson(updatedOrders)
        val success = sharedPreferences.edit()
            .putString("clothing_orders", updatedOrdersJson)
            .commit()
        
        if (success) {
            Log.d(TAG, "Order saved successfully. Total orders: ${updatedOrders.size}")
        } else {
            Log.e(TAG, "Failed to save order to SharedPreferences")
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Order Processing",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for order processing"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createProcessingNotification(): android.app.Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Save notification to Room database
        CoroutineScope(Dispatchers.IO).launch {
            val notification = Notification(
                title = "Processing order...",
                message = "Your order is being processed",
                type = "processing"
            )
            notificationRepository.insertNotification(notification)
        }
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Processing order...")
            .setContentText("Your order is being processed")
            .setSmallIcon(R.drawable.ic_bell)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }
    
    private fun showConfirmationNotification(orderNumber: String) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Save notification to Room database
        CoroutineScope(Dispatchers.IO).launch {
            val appNotification = Notification(
                title = "Order confirmed! #$orderNumber",
                message = "Your order has been successfully placed",
                type = "confirmation"
            )
            notificationRepository.insertNotification(appNotification)
        }
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Order confirmed! #$orderNumber")
            .setContentText("Your order has been successfully placed")
            .setSmallIcon(R.drawable.ic_bell)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_CONFIRMED, notification)
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}

