package com.shreeramarchakaru.gayatrijapa.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shreeramarchakaru.gayatrijapa.data.database.JapaDatabase
import com.shreeramarchakaru.gayatrijapa.data.models.User
import com.shreeramarchakaru.gayatrijapa.data.repository.JapaRepository
import com.shreeramarchakaru.gayatrijapa.utils.LanguageManager
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JapaRepository
    private val languageManager = LanguageManager.getInstance()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _japaIncrement = MutableLiveData<Int>()
    val japaIncrement: LiveData<Int> = _japaIncrement

    private val _currentLanguage = MutableLiveData<String>()
    val currentLanguage: LiveData<String> = _currentLanguage

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> = _logoutSuccess

    private val _showIncrementDialog = MutableLiveData<Boolean>()
    val showIncrementDialog: LiveData<Boolean> = _showIncrementDialog

    private val _showLanguageDialog = MutableLiveData<Boolean>()
    val showLanguageDialog: LiveData<Boolean> = _showLanguageDialog

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        val database = JapaDatabase.getDatabase(application)
        repository = JapaRepository(database.japaDao())

        loadUserData()
        loadJapaIncrement()
        loadCurrentLanguage()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val userData = repository.getLoggedInUser()
            userData?.let {
                _user.value = it
            }
        }
    }

    private fun loadJapaIncrement() {
        viewModelScope.launch {
            val increment = repository.getJapaIncrement()
            _japaIncrement.value = increment
        }
    }

    private fun loadCurrentLanguage() {
        val languageCode = languageManager.getLanguage(getApplication())
        val displayName = languageManager.getLanguageDisplayName(languageCode)
        _currentLanguage.value = displayName
    }

    fun onChangeIncrementClicked() {
        _showIncrementDialog.value = true
    }

    fun onIncrementDialogShown() {
        _showIncrementDialog.value = false
    }

    fun onChangeLanguageClicked() {
        _showLanguageDialog.value = true
    }

    fun onLanguageDialogShown() {
        _showLanguageDialog.value = false
    }

    fun updateJapaIncrement(increment: Int) {
        viewModelScope.launch {
            repository.setJapaIncrement(increment)
            _japaIncrement.value = increment
            _message.value = "Japa increment updated to $increment"
        }
    }

    fun updateLanguage(languageCode: String) {
        languageManager.setLanguage(getApplication(), languageCode)
        val displayName = languageManager.getLanguageDisplayName(languageCode)
        _currentLanguage.value = displayName
        _message.value = "Language changed successfully"
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _logoutSuccess.value = true
        }
    }
}