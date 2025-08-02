package com.shreeramarchakaru.gayatrijapa.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shreeramarchakaru.gayatrijapa.data.database.JapaDatabase
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.data.repository.JapaRepository
import com.shreeramarchakaru.gayatrijapa.utils.TraceUtils
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JapaRepository

    private val _japas = MutableLiveData<List<Japa>>()
    val japas: LiveData<List<Japa>> = _japas

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateToJapa = MutableLiveData<Long?>()
    val navigateToJapa: LiveData<Long?> = _navigateToJapa

    init {
        val database = JapaDatabase.getDatabase(application)
        repository = JapaRepository(database.japaDao())
        loadHistory()
    }

    private fun loadHistory() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val allJapas = repository.getAllJapas()
                // Sort by most recent activity (completed first, then by updated time)
                val sortedJapas = allJapas.sortedWith(
                    compareByDescending<Japa> { it.completedAt ?: 0 }
                        .thenByDescending { it.startedAt ?: 0 }
                        .thenByDescending { it.updatedAt }
                )
                _japas.value = sortedJapas
                TraceUtils.logE("HistoryViewModel", "Loaded ${sortedJapas.size} japas")
            } catch (e: Exception) {
                TraceUtils.logException(e)
                _japas.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onJapaClicked(japa: Japa) {
        if (!japa.isCompleted) {
            // Navigate to continue the japa
            _navigateToJapa.value = japa.id
        } else {
            // Show japa details or statistics
            showJapaDetails(japa)
        }
    }

    private fun showJapaDetails(japa: Japa) {
        // TODO: Implement japa details dialog or navigation
        TraceUtils.logE("HistoryViewModel", "Show details for completed japa: ${japa.name}")
    }

    fun onNavigatedToJapa() {
        _navigateToJapa.value = null
    }

    fun refreshHistory() {
        loadHistory()
    }

    fun deleteJapa(japa: Japa) {
        viewModelScope.launch {
            try {
                repository.deleteJapa(japa.id)
                loadHistory() // Refresh the list
                TraceUtils.logE("HistoryViewModel", "Deleted japa: ${japa.name}")
            } catch (e: Exception) {
                TraceUtils.logException(e)
            }
        }
    }

    fun getCompletedJapasCount(): LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch {
            try {
                val count = repository.getCompletedJapasCount()
                result.postValue(count)
            } catch (e: Exception) {
                TraceUtils.logException(e)
                result.postValue(0)
            }
        }
        return result
    }

    fun getTotalJapaCount(): LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch {
            try {
                val count = repository.getTotalJapaCount()
                result.postValue(count)
            } catch (e: Exception) {
                TraceUtils.logException(e)
                result.postValue(0)
            }
        }
        return result
    }
}