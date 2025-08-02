package com.shreeramarchakaru.gayatrijapa.ui.selection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shreeramarchakaru.gayatrijapa.data.database.JapaDatabase
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.data.repository.JapaRepository
import kotlinx.coroutines.launch

class JapaSelectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JapaRepository

    val japas: LiveData<List<Japa>>

    private val _showAddJapaDialog = MutableLiveData<Boolean>()
    val showAddJapaDialog: LiveData<Boolean> = _showAddJapaDialog

    private val _gayatriJapaStatus = MutableLiveData<Japa?>()
    val gayatriJapaStatus: LiveData<Japa?> = _gayatriJapaStatus

    init {
        val database = JapaDatabase.getDatabase(application)
        repository = JapaRepository(database.japaDao())
        japas = repository.getAllJapas()
    }

    fun onAddCustomJapaClicked() {
        _showAddJapaDialog.value = true
    }

    fun onAddJapaDialogShown() {
        _showAddJapaDialog.value = false
    }

    fun addCustomJapa(name: String, mantra: String, targetCount: Int) {
        viewModelScope.launch {
            val japa = Japa(
                name = name,
                mantra = mantra,
                targetCount = targetCount
            )
            repository.addJapa(japa)
        }
    }

    fun checkGayatriJapaStatus() {
        viewModelScope.launch {
            val gayatriJapa = repository.getJapaById(1L)
            _gayatriJapaStatus.value = gayatriJapa
        }
    }

    fun restartJapa(japaId: Long) {
        viewModelScope.launch {
            // Reset japa to initial state
            val japa = repository.getJapaById(japaId)
            japa?.let {
                val restartedJapa = it.copy(
                    currentCount = 0,
                    isStarted = false,
                    isCompleted = false,
                    completedAt = null,
                    updatedAt = System.currentTimeMillis()
                )
                repository.updateJapa(restartedJapa)
            }
        }
    }

    fun createNewJapaFromTemplate(templateJapa: Japa) {
        viewModelScope.launch {
            val newJapa = templateJapa.copy(
                id = 0, // Auto-generate new ID
                currentCount = 0,
                isStarted = false,
                isCompleted = false,
                completedAt = null,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            repository.addJapa(newJapa)
        }
    }
}