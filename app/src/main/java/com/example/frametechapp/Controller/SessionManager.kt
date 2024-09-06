package com.example.frametechapp.Controller

import android.content.Context
import androidx.activity.ComponentActivity

class SessionManager(private val context: Context)  {
    private val prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {//this function will be saving the token in the application to be used
        prefs.edit().putString("token", token).apply()
    }
    fun getToken(): String? {//this will be getting the save token
        return prefs.getString("token", null)
    }
    fun clearSession() {//this will be used for the when the user is logging out
        prefs.edit().remove("token").apply()
    }
}