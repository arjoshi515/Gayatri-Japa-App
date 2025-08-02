package com.shreeramarchakaru.gayatrijapa.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

class LanguageManager private constructor() {

    companion object {
        private const val PREF_NAME = "language_pref"
        private const val KEY_LANGUAGE = "selected_language"

        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_HINDI = "hi"
        const val LANGUAGE_KANNADA = "kn"

        @Volatile
        private var INSTANCE: LanguageManager? = null

        fun getInstance(): LanguageManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LanguageManager().also { INSTANCE = it }
            }
        }
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setLanguage(context: Context, languageCode: String) {
        getPreferences(context).edit()
            .putString(KEY_LANGUAGE, languageCode)
            .apply()

        updateResources(context, languageCode)
    }

    fun getLanguage(context: Context): String {
        return getPreferences(context).getString(KEY_LANGUAGE, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH
    }

    fun updateResources(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }

    fun getLanguageDisplayName(languageCode: String): String {
        return when (languageCode) {
            LANGUAGE_ENGLISH -> "English"
            LANGUAGE_HINDI -> "हिंदी"
            LANGUAGE_KANNADA -> "ಕನ್ನಡ"
            else -> "English"
        }
    }

    fun getAllLanguages(): List<Language> {
        return listOf(
            Language(LANGUAGE_ENGLISH, "English"),
            Language(LANGUAGE_HINDI, "हिंदी"),
            Language(LANGUAGE_KANNADA, "ಕನ್ನಡ")
        )
    }

    data class Language(
        val code: String,
        val displayName: String
    )
}