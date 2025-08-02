package com.shreeramarchakaru.gayatrijapa.ui.selection.sankalp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.data.database.JapaDatabase
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.data.repository.JapaRepository
import kotlinx.coroutines.launch

class SankalpViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JapaRepository

    private val _japa = MutableLiveData<Japa>()
    val japa: LiveData<Japa> = _japa

    private val _navigateToJapa = MutableLiveData<Long?>()
    val navigateToJapa: LiveData<Long?> = _navigateToJapa

    private val _sankalpText = MutableLiveData<String>()
    val sankalpText: LiveData<String> = _sankalpText

    init {
        val database = JapaDatabase.getDatabase(application)
        repository = JapaRepository(database.japaDao())

        // Set default Sankalp text
        _sankalpText.value = application.getString(R.string.sankalpa_mantra)
    }

    fun loadJapa(japaId: Long) {
        viewModelScope.launch {
            val japaData = repository.getJapaById(japaId)
            japaData?.let {
                _japa.value = it
            }
        }
    }

    fun onSankalpDone() {
        _japa.value?.let { japa ->
            viewModelScope.launch {
                // Mark japa as started
                repository.markJapaAsStarted(japa.id)
                _navigateToJapa.value = japa.id
            }
        }
    }

    fun onNavigatedToJapa() {
        _navigateToJapa.value = null
    }
}