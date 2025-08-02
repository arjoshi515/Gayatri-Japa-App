package com.shreeramarchakaru.gayatrijapa.data.database


import androidx.lifecycle.LiveData
import androidx.room.*
import com.shreeramarchakaru.gayatrijapa.data.models.AppSettings
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.data.models.JapaSession
import com.shreeramarchakaru.gayatrijapa.data.models.User

@Dao
interface JapaDao {

    // User operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): User?

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAllUsers()

    // Japa operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJapa(japa: Japa): Long

    @Update
    suspend fun updateJapa(japa: Japa)

    @Delete
    suspend fun deleteJapa(japa: Japa)

    @Query("SELECT * FROM japas ORDER BY createdAt DESC")
    fun getAllJapas(): LiveData<List<Japa>>

    @Query("SELECT * FROM japas WHERE id = :japaId")
    suspend fun getJapaById(japaId: Long): Japa?

    @Query("UPDATE japas SET currentCount = :count, updatedAt = :updatedAt WHERE id = :japaId")
    suspend fun updateJapaCount(japaId: Long, count: Int, updatedAt: Long)

    @Query("UPDATE japas SET isCompleted = :isCompleted WHERE id = :japaId")
    suspend fun updateJapaCompletion(japaId: Long, isCompleted: Boolean)

    // Japa Session operations
    @Insert
    suspend fun insertJapaSession(session: JapaSession): Long

    @Update
    suspend fun updateJapaSession(session: JapaSession)

    @Query("SELECT * FROM japa_sessions WHERE japaId = :japaId ORDER BY startTime DESC")
    fun getJapaSessionsForJapa(japaId: Long): LiveData<List<JapaSession>>

    @Query("SELECT * FROM japa_sessions WHERE isCompleted = 0 LIMIT 1")
    suspend fun getActiveSession(): JapaSession?

    // Settings operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: AppSettings)

    @Query("SELECT value FROM app_settings WHERE key = :key")
    suspend fun getSettingValue(key: String): String?

    @Query("SELECT * FROM app_settings")
    suspend fun getAllSettings(): List<AppSettings>

    @Query("UPDATE japas SET isStarted = :isStarted, updatedAt = :updatedAt WHERE id = :japaId")
    suspend fun updateJapaStarted(japaId: Long, isStarted: Boolean, updatedAt: Long)

    @Query("UPDATE japas SET isCompleted = :isCompleted, completedAt = :completedAt, updatedAt = :updatedAt WHERE id = :japaId")
    suspend fun updateJapaCompleted(japaId: Long, isCompleted: Boolean, completedAt: Long?, updatedAt: Long)

    @Query("SELECT * FROM japas WHERE isStarted = 1 AND isCompleted = 0")
    suspend fun getStartedJapas(): List<Japa>

}