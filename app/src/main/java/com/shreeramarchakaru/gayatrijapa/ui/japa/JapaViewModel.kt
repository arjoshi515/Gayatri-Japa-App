package com.shreeramarchakaru.gayatrijapa.ui.japa

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.data.database.JapaDatabase
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.data.models.JapaSession
import com.shreeramarchakaru.gayatrijapa.data.repository.JapaRepository
import kotlinx.coroutines.launch

class JapaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JapaRepository

    private val _japa = MutableLiveData<Japa>()
    val japa: LiveData<Japa> = _japa

    private val _currentCount = MutableLiveData<Int>()
    val currentCount: LiveData<Int> = _currentCount

    private val _showCompletionDialog = MutableLiveData<Boolean>()
    val showCompletionDialog: LiveData<Boolean> = _showCompletionDialog

    private val _showQuickCountDialog = MutableLiveData<Boolean>()
    val showQuickCountDialog: LiveData<Boolean> = _showQuickCountDialog

    private val _japaIncrement = MutableLiveData<Int>()
    val japaIncrement: LiveData<Int> = _japaIncrement

    private val _navigateToHistory = MutableLiveData<Boolean>()
    val navigateToHistory: LiveData<Boolean> = _navigateToHistory

    private val _isJapaCompleted = MutableLiveData<Boolean>()
    val isJapaCompleted: LiveData<Boolean> = _isJapaCompleted

    private var currentSession: JapaSession? = null
    private var sessionId: Long = 0
    private var hasShownCompletionDialog = false

    init {
        val database = JapaDatabase.getDatabase(application)
        repository = JapaRepository(database.japaDao())

        loadJapaIncrement()
        checkForActiveSession()
    }

    fun loadJapa(japaId: Long) {
        viewModelScope.launch {
            val japaData = repository.getJapaById(japaId)
            japaData?.let {
                _japa.value = it
                _isJapaCompleted.value = it.isCompleted

                // Check if target is reached and show completion dialog only once
                if (it.currentCount >= it.targetCount && !it.isCompleted && !hasShownCompletionDialog) {
                    _showCompletionDialog.value = true
                    hasShownCompletionDialog = true
                }

                // Start new session if no active session and japa is not completed
                if (currentSession == null && !it.isCompleted) {
                    sessionId = repository.startJapaSession(japaId)
                }
            }
        }
    }

    private fun loadJapaIncrement() {
        viewModelScope.launch {
            val increment = repository.getJapaIncrement()
            _japaIncrement.value = increment
        }
    }

    private fun checkForActiveSession() {
        viewModelScope.launch {
            currentSession = repository.getActiveSession()
        }
    }

    fun onJapaImageClicked() {
        val increment = _japaIncrement.value ?: 10
        val newCount = (_currentCount.value ?: 0) + increment
        updateCount(newCount)
    }

    fun increaseCount() {
        val newCount = (_currentCount.value ?: 0) + 1
        updateCount(newCount)
    }

    fun decreaseCount() {
        val currentValue = _currentCount.value ?: 0
        if (currentValue > 0) {
            val newCount = currentValue - 1
            updateCount(newCount)
        }
    }

    fun setQuickCount(count: Int) {
        updateCount(count)
    }

    fun addQuickCount(count: Int) {
        val newCount = (_currentCount.value ?: 0) + count
        updateCount(newCount)
    }

    private fun updateCount(newCount: Int) {
        _currentCount.value = newCount

        _japa.value?.let { japa ->
            viewModelScope.launch {
                repository.updateJapaCount(japa.id, newCount)

                // Update session
                currentSession?.let { session ->
                    val updatedSession = session.copy(sessionCount = newCount)
                    repository.updateJapaSession(updatedSession)
                    currentSession = updatedSession
                }

                // Check if target reached and show completion dialog only once
                if (newCount >= japa.targetCount && !japa.isCompleted && !hasShownCompletionDialog) {
                    _showCompletionDialog.value = true
                    hasShownCompletionDialog = true
                }
            }
        }
    }

    fun completeJapa() {
        _japa.value?.let { japa ->
            viewModelScope.launch {
                // Mark japa as completed
                repository.markJapaAsCompleted(japa.id)

                // Complete session
                currentSession?.let { session ->
                    val completedSession = session.copy(
                        endTime = System.currentTimeMillis(),
                        isCompleted = true
                    )
                    repository.updateJapaSession(completedSession)
                }

                _isJapaCompleted.value = true
                _navigateToHistory.value = true
            }
        }
    }

    fun onSkipCompletion() {
        // User chose to skip completion, they can continue japa
        _showCompletionDialog.value = false
        hasShownCompletionDialog = true // Don't show again in this session
    }

    fun saveCurrentState() {
        // Save current state when fragment is paused
        _japa.value?.let { japa ->
            val currentValue = _currentCount.value ?: 0
            viewModelScope.launch {
                repository.updateJapaCount(japa.id, currentValue)
            }
        }
    }

    fun saveCompletionRemarks(remarks: String) {
        currentSession?.let { session ->
            viewModelScope.launch {
                val updatedSession = session.copy(
                    remarks = remarks,
                    endTime = System.currentTimeMillis(),
                    isCompleted = true
                )
                repository.updateJapaSession(updatedSession)
            }
        }
    }

    fun onCompletionDialogShown() {
        _showCompletionDialog.value = false
    }

    fun onQuickCountDialogShown() {
        _showQuickCountDialog.value = false
    }

    fun onNavigatedToHistory() {
        _navigateToHistory.value = false
    }

    fun onScreenTimeout() {
        // Handle screen timeout - could show a gentle reminder
    }

    // In JapaViewModel or Fragment
/*    fun getMotivationalMessage(progress: Int): String {
        return when {
            progress >= 90 -> application.getString(R.string.milestone_90_percent)
            progress >= 75 -> application.getString(R.string.milestone_75_percent)
            progress >= 50 -> application.getString(R.string.milestone_50_percent)
            progress >= 25 -> application.getString(R.string.milestone_25_percent)
            else -> application.getString(R.string.keep_going)
        }
    }

    fun getStreakMessage(days: Int): String {
        return application.getString(R.string.streak_message, days)
    }*/
}