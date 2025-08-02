package com.shreeramarchakaru.gayatrijapa.ui.history


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shreeramarchakaru.gayatrijapa.data.database.JapaDatabase
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.data.repository.JapaRepository

import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JapaRepository

    val japas: LiveData<List<Japa>>

    private val _totalJapas = MutableLiveData<Int>()
    val totalJapas: LiveData<Int> = _totalJapas

    private val _isHistoryEmpty = MutableLiveData<Boolean>()
    val isHistoryEmpty: LiveData<Boolean> = _isHistoryEmpty

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _filterType = MutableLiveData<FilterType>()
    val filterType: LiveData<FilterType> = _filterType

    enum class FilterType {
        ALL, COMPLETED, IN_PROGRESS
    }

    init {
        val database = JapaDatabase.getDatabase(application)
        repository = JapaRepository(database.japaDao())
        japas = repository.getAllJapas()

        _filterType.value = FilterType.ALL
        _isLoading.value = false

        // Observe japas to update total count and empty state
        japas.observeForever { japaList ->
            _totalJapas.value = japaList.size
            _isHistoryEmpty.value = japaList.isEmpty()
            _isLoading.value = false
        }
    }

    fun addRemarks(japaId: Long, remarks: String) {
        viewModelScope.launch {
            // Implementation to add remarks to japa session
            // This would require updating the database schema to include remarks in Japa table
            // or creating a separate remarks table
        }
    }

    fun setFilter(filterType: FilterType) {
        _filterType.value = filterType
        // Implement filtering logic here
    }

    fun refreshHistory() {
        _isLoading.value = true
        // The LiveData will automatically update when database changes
    }
}