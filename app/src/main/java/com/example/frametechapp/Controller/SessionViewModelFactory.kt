package com.example.frametechapp.Controller

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SessionViewModelFactory(private val repository: SessionManager,private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SessionViewModel(sessionManager = repository, NetworkManager(), context =context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
