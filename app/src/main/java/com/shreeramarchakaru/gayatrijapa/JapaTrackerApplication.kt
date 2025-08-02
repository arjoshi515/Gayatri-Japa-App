package com.shreeramarchakaru.gayatrijapa

import android.app.Application
import android.content.Context
import com.shreeramarchakaru.gayatrijapa.utils.LanguageManager
import com.shreeramarchakaru.gayatrijapa.utils.TraceUtils

class JapaTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TraceUtils.logE("Application", "Application started")
        initializeLanguage()
    }

    override fun attachBaseContext(base: Context?) {
        val languageManager = LanguageManager.getInstance()
        val languageCode = base?.let { languageManager.getLanguage(it) } ?: LanguageManager.LANGUAGE_ENGLISH
        val context = base?.let { languageManager.updateResources(it, languageCode) }
        super.attachBaseContext(context ?: base)
    }

    private fun initializeLanguage() {
        try {
            val languageManager = LanguageManager.getInstance()
            val savedLanguage = languageManager.getLanguage(this)
            languageManager.updateResources(this, savedLanguage)
            TraceUtils.logE("Application", "Language initialized: $savedLanguage")
        } catch (e: Exception) {
            TraceUtils.logException(e)
        }
    }
}