package com.shreeramarchakaru.gayatrijapa.data.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "japa_sessions",
    foreignKeys = [
        ForeignKey(
            entity = Japa::class,
            parentColumns = ["id"],
            childColumns = ["japaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class JapaSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val japaId: Long,
    val sessionCount: Int,
    val startTime: Long,
    val endTime: Long? = null,
    val isCompleted: Boolean = false,
    val remarks: String? = null
)
