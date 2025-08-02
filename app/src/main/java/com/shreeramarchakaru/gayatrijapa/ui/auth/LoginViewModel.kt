package com.shreeramarchakaru.gayatrijapa.ui.auth

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shreeramarchakaru.gayatrijapa.data.database.JapaDatabase
import com.shreeramarchakaru.gayatrijapa.data.repository.JapaRepository
import com.shreeramarchakaru.gayatrijapa.utils.TraceUtils
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JapaRepository

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // Use ObservableBoolean for data binding
    val isLoading = ObservableBoolean(false)

    init {
        val database = JapaDatabase.getDatabase(application)
        repository = JapaRepository(database.japaDao())
    }

    fun login(name: String, mobileNumber: String) {
        // Clear previous error
        _errorMessage.value = ""

        if (validateInput(name, mobileNumber)) {
            isLoading.set(true)
            viewModelScope.launch {
                try {
                    TraceUtils.logE("LoginViewModel", "Starting login for: $name, $mobileNumber")

                    val user = repository.loginUser(name, mobileNumber)
                    TraceUtils.logE("LoginViewModel", "Login successful for user: ${user.id}")

                    _loginSuccess.value = true
                } catch (e: Exception) {
                    TraceUtils.logException(e)
                    _errorMessage.value = "Login failed: ${e.message}"
                } finally {
                    isLoading.set(false)
                }
            }
        }
    }

    private fun validateInput(name: String, mobileNumber: String): Boolean {
        return when {
            name.isEmpty() -> {
                _errorMessage.value = "Please enter your name"
                false
            }
            name.length < 2 -> {
                _errorMessage.value = "Name must be at least 2 characters"
                false
            }
            mobileNumber.isEmpty() -> {
                _errorMessage.value = "Please enter mobile number"
                false
            }
            mobileNumber.length != 10 -> {
                _errorMessage.value = "Please enter valid 10-digit mobile number"
                false
            }
            !mobileNumber.all { it.isDigit() } -> {
                _errorMessage.value = "Mobile number should contain only digits"
                false
            }
            else -> true
        }
    }
}