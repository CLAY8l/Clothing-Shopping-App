package com.example.app_ui.utils

import android.content.Context
import android.content.SharedPreferences

object FirebaseUserHelper {
    private const val PREFS_NAME = "firebase_user_prefs"
    private const val KEY_USER_ID = "current_user_id"
    private const val KEY_USER_EMAIL = "current_user_email"
    
    /**
     * Get current user ID from SharedPreferences
     * This can be replaced with Firebase Auth later
     */
    fun getCurrentUserId(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_ID, null)
    }
    
    /**
     * Save current user ID to SharedPreferences
     */
    fun setCurrentUserId(context: Context, userId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }
    
    /**
     * Get current user email
     */
    fun getCurrentUserEmail(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_EMAIL, null)
    }
    
    /**
     * Save current user email
     */
    fun setCurrentUserEmail(context: Context, email: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_EMAIL, email).apply()
    }
    
    /**
     * Clear user data (on logout)
     */
    fun clearUserData(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
    
    /**
     * Check if user is logged in
     */
    fun isUserLoggedIn(context: Context): Boolean {
        return getCurrentUserId(context) != null
    }
}

