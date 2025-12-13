package com.example.app_ui.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.app_ui.service.AppNotification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object NotificationReader {
    
    /**
     * Read all saved notifications from SharedPreferences
     * @param context Application context
     * @return List of AppNotification objects
     */
    fun getAllNotifications(context: Context): List<AppNotification> {
        val sharedPreferences: SharedPreferences = 
            context.getSharedPreferences("app_notifications", Context.MODE_PRIVATE)
        
        val notificationsJson = sharedPreferences.getString("app_notifications", "[]") ?: "[]"
        
        return try {
            val type = object : TypeToken<List<AppNotification>>() {}.type
            Gson().fromJson<List<AppNotification>>(notificationsJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Get notifications sorted by date (newest first)
     * @param context Application context
     * @return List of AppNotification objects sorted by timestamp descending
     */
    fun getNotificationsSortedByDate(context: Context): List<AppNotification> {
        return getAllNotifications(context).sortedByDescending { it.timestamp }
    }
    
    /**
     * Get unread notification count
     * @param context Application context
     * @return Total count of saved notifications
     */
    fun getNotificationCount(context: Context): Int {
        return getAllNotifications(context).size
    }
    
    /**
     * Save a notification to SharedPreferences
     * @param context Application context
     * @param notification The notification to save
     */
    fun saveNotification(context: Context, notification: AppNotification) {
        val sharedPreferences: SharedPreferences = 
            context.getSharedPreferences("app_notifications", Context.MODE_PRIVATE)
        
        val notificationsJson = sharedPreferences.getString("app_notifications", "[]") ?: "[]"
        
        try {
            val type = object : TypeToken<List<AppNotification>>() {}.type
            val notifications = Gson().fromJson<List<AppNotification>>(notificationsJson, type) ?: emptyList()
            
            val updatedNotifications = notifications.toMutableList()
            updatedNotifications.add(notification)
            
            // Keep only last 100 notifications to prevent storage issues
            val trimmedNotifications = if (updatedNotifications.size > 100) {
                updatedNotifications.takeLast(100)
            } else {
                updatedNotifications
            }
            
            val updatedNotificationsJson = Gson().toJson(trimmedNotifications)
            sharedPreferences.edit()
                .putString("app_notifications", updatedNotificationsJson)
                .apply()
        } catch (e: Exception) {
            // Handle error silently
        }
    }
    
    /**
     * Delete all notifications
     * @param context Application context
     */
    fun clearAllNotifications(context: Context) {
        val sharedPreferences: SharedPreferences = 
            context.getSharedPreferences("app_notifications", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("app_notifications", "[]")
            .apply()
    }
}

