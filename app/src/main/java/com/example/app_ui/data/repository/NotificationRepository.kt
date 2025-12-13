package com.example.app_ui.data.repository

import com.example.app_ui.data.dao.NotificationDao
import com.example.app_ui.data.entity.Notification
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val notificationDao: NotificationDao) {
    fun getAllNotifications(): Flow<List<Notification>> = notificationDao.getAllNotifications()
    
    fun getUnreadNotifications(): Flow<List<Notification>> = notificationDao.getUnreadNotifications()
    
    fun getUnreadCount(): Flow<Int> = notificationDao.getUnreadCount()
    
    suspend fun getNotificationById(notificationId: Long): Notification? = 
        notificationDao.getNotificationById(notificationId)
    
    suspend fun insertNotification(notification: Notification): Long = 
        notificationDao.insertNotification(notification)
    
    suspend fun updateNotification(notification: Notification) = 
        notificationDao.updateNotification(notification)
    
    suspend fun deleteNotification(notification: Notification) = 
        notificationDao.deleteNotification(notification)
    
    suspend fun deleteNotificationById(notificationId: Long) = 
        notificationDao.deleteNotificationById(notificationId)
    
    suspend fun deleteAllNotifications() = notificationDao.deleteAllNotifications()
    
    suspend fun markAsRead(notificationId: Long) = notificationDao.markAsRead(notificationId)
    
    suspend fun markAllAsRead() = notificationDao.markAllAsRead()
}

