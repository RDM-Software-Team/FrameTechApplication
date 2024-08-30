package com.example.frametechapp.Controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SessionViewModelFactory(private val repository: SessionManager ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SessionViewModel(sessionManager = repository, NetworkManager()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
