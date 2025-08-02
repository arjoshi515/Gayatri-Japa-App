package com.shreeramarchakaru.gayatrijapa.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shreeramarchakaru.gayatrijapa.data.database.JapaDatabase
import com.shreeramarchakaru.gayatrijapa.data.repository.JapaRepository

import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JapaRepository

    private val _navigationEvent = MutableLiveData<NavigationDestination>()
    val navigationEvent: LiveData<NavigationDestination> = _navigationEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        val database = JapaDatabase.getDatabase(application)
        repository = JapaRepository(database.japaDao())
    }

    fun checkUserLoginStatus() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val user = repository.getLoggedInUser()
                if (user != null) {
                    _navigationEvent.value = NavigationDestination.MAIN
                } else {
                    _navigationEvent.value = NavigationDestination.LOGIN
                }
            } catch (e: Exception) {
                _navigationEvent.value = NavigationDestination.LOGIN
            } finally {
                _isLoading.value = false
            }
        }
    }

    enum class NavigationDestination {
        LOGIN, MAIN
    }
}