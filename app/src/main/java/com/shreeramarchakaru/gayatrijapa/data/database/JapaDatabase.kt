package com.shreeramarchakaru.gayatrijapa.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shreeramarchakaru.gayatrijapa.data.models.*
import com.shreeramarchakaru.gayatrijapa.utils.TraceUtils

@Database(
    entities = [User::class, Japa::class, JapaSession::class, AppSettings::class],
    version = 2,
    exportSchema = false
)
abstract class JapaDatabase : RoomDatabase() {

    abstract fun japaDao(): JapaDao

    companion object {
        @Volatile
        private var INSTANCE: JapaDatabase? = null

        // Migration from version 1 to 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    // Add new columns to japas table
                    database.execSQL("ALTER TABLE japas ADD COLUMN isStarted INTEGER NOT NULL DEFAULT 0")
                    database.execSQL("ALTER TABLE japas ADD COLUMN completedAt INTEGER")
                    TraceUtils.logE("Database", "Migration 1->2 completed successfully")
                } catch (e: Exception) {
                    TraceUtils.logException(e)
                }
            }
        }

        fun getDatabase(context: Context): JapaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JapaDatabase::class.java,
                    "japa_database"
                )
                    .addCallback(DatabaseCallback())
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration() // Add this for development
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                try {
                    TraceUtils.logE("Database", "Creating database and inserting default data")

                    // Insert default Gayatri Japa
                    db.execSQL("""
                        INSERT INTO japas (name, mantra, targetCount, currentCount, isCompleted, isStarted, createdAt, updatedAt) 
                        VALUES ('Gayatri Japa', 'ಓಂ | ಭೂರ್ಭುವಃ ಸ್ವಃ | ತತ್ಸವಿತ್ತುರ್ವರೇಣ್ಯಂ | ಭರ್ಗೋ ದೇವಸ್ಯ ಧೀಮಹಿ | ಧಿಯೋ ಯೋ ನಃ ಪ್ರಚೋದಯಾತ್ |', 
                               1008, 0, 0, 0, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})
                    """)

                    // Insert default settings
                    db.execSQL("INSERT INTO app_settings (key, value) VALUES ('japa_increment', '10')")
                    db.execSQL("INSERT INTO app_settings (key, value) VALUES ('screen_timeout', '120000')")

                    TraceUtils.logE("Database", "Default data inserted successfully")
                } catch (e: Exception) {
                    TraceUtils.logException(e)
                }
            }
        }
    }
}