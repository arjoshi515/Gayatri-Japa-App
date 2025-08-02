package com.shreeramarchakaru.gayatrijapa.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val mobileNumber: String,
    val isLoggedIn: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
