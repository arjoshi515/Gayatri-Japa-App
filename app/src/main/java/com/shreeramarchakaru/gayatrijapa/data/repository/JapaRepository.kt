package com.shreeramarchakaru.gayatrijapa.data.repository


import androidx.lifecycle.LiveData
import com.shreeramarchakaru.gayatrijapa.data.database.JapaDao
import com.shreeramarchakaru.gayatrijapa.data.models.*
import com.shreeramarchakaru.gayatrijapa.utils.TraceUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JapaRepository(private val japaDao: JapaDao) {

    // User operations
    suspend fun loginUser(name: String, mobileNumber: String): User {
        return withContext(Dispatchers.IO) {
            try {
                TraceUtils.logE("JapaRepository", "Attempting to login user: $name")

                // First logout all users
                japaDao.logoutAllUsers()
                TraceUtils.logE("JapaRepository", "Logged out all existing users")

                // Create new user
                val user = User(
                    name = name.trim(),
                    mobileNumber = mobileNumber.trim(),
                    isLoggedIn = true
                )

                // Insert user and get ID
                val userId = japaDao.insertUser(user)
                TraceUtils.logE("JapaRepository", "User inserted with ID: $userId")

                // Return user with ID
                user.copy(id = userId)
            } catch (e: Exception) {
                TraceUtils.logException(e)
                throw Exception("Failed to create user: ${e.message}")
            }
        }
    }

    suspend fun getLoggedInUser(): User? {
        return withContext(Dispatchers.IO) {
            try {
                japaDao.getLoggedInUser()
            } catch (e: Exception) {
                TraceUtils.logException(e)
                null
            }
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            try {
                japaDao.logoutAllUsers()
            } catch (e: Exception) {
                TraceUtils.logException(e)
            }
        }
    }

    // Japa operations
    suspend fun addJapa(japa: Japa): Long {
        return withContext(Dispatchers.IO) {
            try {
                japaDao.insertJapa(japa)
            } catch (e: Exception) {
                TraceUtils.logException(e)
                throw Exception("Failed to add japa: ${e.message}")
            }
        }
    }
    fun getAllJapas(): LiveData<List<Japa>> = japaDao.getAllJapas()

    suspend fun updateJapaCount(japaId: Long, count: Int) {
        withContext(Dispatchers.IO) {
            try {
                japaDao.updateJapaCount(japaId, count, System.currentTimeMillis())
            } catch (e: Exception) {
                TraceUtils.logException(e)
            }
        }
    }

    suspend fun getJapaById(japaId: Long): Japa? {
        return withContext(Dispatchers.IO) {
            try {
                japaDao.getJapaById(japaId)
            } catch (e: Exception) {
                TraceUtils.logException(e)
                null
            }
        }
    }

    suspend fun markJapaAsStarted(japaId: Long) {
        withContext(Dispatchers.IO) {
            try {
                japaDao.updateJapaStarted(japaId, true, System.currentTimeMillis())
            } catch (e: Exception) {
                TraceUtils.logException(e)
            }
        }
    }

    suspend fun markJapaAsCompleted(japaId: Long) {
        withContext(Dispatchers.IO) {
            try {
                japaDao.updateJapaCompleted(japaId, true, System.currentTimeMillis(), System.currentTimeMillis())
            } catch (e: Exception) {
                TraceUtils.logException(e)
            }
        }
    }

    // Session operations
    suspend fun startJapaSession(japaId: Long): Long {
        return withContext(Dispatchers.IO) {
            try {
                val session = JapaSession(
                    japaId = japaId,
                    sessionCount = 0,
                    startTime = System.currentTimeMillis()
                )
                japaDao.insertJapaSession(session)
            } catch (e: Exception) {
                TraceUtils.logException(e)
                0L
            }
        }
    }

    suspend fun updateJapaSession(session: JapaSession) {
        withContext(Dispatchers.IO) {
            try {
                japaDao.updateJapaSession(session)
            } catch (e: Exception) {
                TraceUtils.logException(e)
            }
        }
    }

    suspend fun getActiveSession(): JapaSession? {
        return withContext(Dispatchers.IO) {
            try {
                japaDao.getActiveSession()
            } catch (e: Exception) {
                TraceUtils.logException(e)
                null
            }
        }
    }

    // Settings operations
    suspend fun getJapaIncrement(): Int {
        return withContext(Dispatchers.IO) {
            try {
                japaDao.getSettingValue("japa_increment")?.toIntOrNull() ?: 10
            } catch (e: Exception) {
                TraceUtils.logException(e)
                10
            }
        }
    }

    suspend fun setJapaIncrement(increment: Int) {
        withContext(Dispatchers.IO) {
            try {
                japaDao.insertSetting(AppSettings("japa_increment", increment.toString()))
            } catch (e: Exception) {
                TraceUtils.logException(e)
            }
        }
    }

    suspend fun getScreenTimeout(): Long {
        return withContext(Dispatchers.IO) {
            try {
                japaDao.getSettingValue("screen_timeout")?.toLongOrNull() ?: 120000L
            } catch (e: Exception) {
                TraceUtils.logException(e)
                120000L
            }
        }
    }

    suspend fun updateJapa(japa: Japa) {
        withContext(Dispatchers.IO) {
            try {
                japaDao.updateJapa(japa)
            } catch (e: Exception) {
                TraceUtils.logException(e)
                throw Exception("Failed to update japa: ${e.message}")
            }
        }
    }
}