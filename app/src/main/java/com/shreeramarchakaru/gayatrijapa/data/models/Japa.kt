package com.shreeramarchakaru.gayatrijapa.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "japas")
data class Japa(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val mantra: String,
    val targetCount: Int,
    val currentCount: Int = 0,
    val imageUrl: String? = null,
    val localImagePath: String? = null,
    val isCompleted: Boolean = false,
    val isStarted: Boolean = false, // New field to track if japa is started
    val completedAt: Long? = null, // Track completion time
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)