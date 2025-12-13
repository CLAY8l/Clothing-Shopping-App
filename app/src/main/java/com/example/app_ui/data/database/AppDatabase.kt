package com.example.app_ui.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app_ui.data.dao.FavoriteProductDao
import com.example.app_ui.data.dao.NotificationDao
import com.example.app_ui.data.dao.OrderDao
import com.example.app_ui.data.dao.ProductCartDao
import com.example.app_ui.data.dao.UserDao
import com.example.app_ui.data.entity.FavoriteProduct
import com.example.app_ui.data.entity.Notification
import com.example.app_ui.data.entity.Order
import com.example.app_ui.data.entity.OrderItem
import com.example.app_ui.data.entity.ProductCart
import com.example.app_ui.data.entity.User

@Database(
    entities = [User::class, ProductCart::class, FavoriteProduct::class, Order::class, OrderItem::class, Notification::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productCartDao(): ProductCartDao
    abstract fun favoriteProductDao(): FavoriteProductDao
    abstract fun orderDao(): OrderDao
    abstract fun notificationDao(): NotificationDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

