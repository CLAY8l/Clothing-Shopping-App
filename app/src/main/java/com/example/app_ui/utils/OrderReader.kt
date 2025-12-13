package com.example.app_ui.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.app_ui.service.SavedOrder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object OrderReader {
    
    /**
     * Read all saved orders from SharedPreferences
     * @param context Application context
     * @return List of SavedOrder objects
     */
    fun getAllOrders(context: Context): List<SavedOrder> {
        val sharedPreferences: SharedPreferences = 
            context.getSharedPreferences("clothing_orders", Context.MODE_PRIVATE)
        
        val ordersJson = sharedPreferences.getString("clothing_orders", "[]") ?: "[]"
        
        return try {
            val type = object : TypeToken<List<SavedOrder>>() {}.type
            Gson().fromJson<List<SavedOrder>>(ordersJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Read a specific order by order number
     * @param context Application context
     * @param orderNumber The order number to search for
     * @return SavedOrder if found, null otherwise
     */
    fun getOrderByNumber(context: Context, orderNumber: String): SavedOrder? {
        return getAllOrders(context).firstOrNull { it.orderNumber == orderNumber }
    }
    
    /**
     * Get orders sorted by date (newest first)
     * @param context Application context
     * @return List of SavedOrder objects sorted by date descending
     */
    fun getOrdersSortedByDate(context: Context): List<SavedOrder> {
        return getAllOrders(context).sortedByDescending { it.date }
    }
    
    /**
     * Get orders by status
     * @param context Application context
     * @param status The status to filter by
     * @return List of SavedOrder objects with the specified status
     */
    fun getOrdersByStatus(context: Context, status: String): List<SavedOrder> {
        return getAllOrders(context).filter { it.status == status }
    }
    
    /**
     * Get total number of orders
     * @param context Application context
     * @return Total count of saved orders
     */
    fun getOrderCount(context: Context): Int {
        return getAllOrders(context).size
    }
    
    /**
     * Delete an order by order number
     * @param context Application context
     * @param orderNumber The order number to delete
     * @return true if order was deleted, false otherwise
     */
    fun deleteOrder(context: Context, orderNumber: String): Boolean {
        val sharedPreferences: SharedPreferences = 
            context.getSharedPreferences("clothing_orders", Context.MODE_PRIVATE)
        
        val ordersJson = sharedPreferences.getString("clothing_orders", "[]") ?: "[]"
        
        return try {
            val type = object : TypeToken<List<SavedOrder>>() {}.type
            val orders = Gson().fromJson<List<SavedOrder>>(ordersJson, type) ?: emptyList()
            
            val updatedOrders = orders.filter { it.orderNumber != orderNumber }
            val updatedOrdersJson = Gson().toJson(updatedOrders)
            
            sharedPreferences.edit()
                .putString("clothing_orders", updatedOrdersJson)
                .apply()
            
            true
        } catch (e: Exception) {
            false
        }
    }
}

