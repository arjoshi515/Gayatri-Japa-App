package com.shreeramarchakaru.gayatrijapa.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shreeramarchakaru.gayatrijapa.utils.LanguageManager


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateLanguage()
    }

    override fun attachBaseContext(newBase: Context?) {
        val languageManager = LanguageManager.getInstance()
        val languageCode = newBase?.let { languageManager.getLanguage(it) } ?: LanguageManager.LANGUAGE_ENGLISH
        val context = newBase?.let { languageManager.updateResources(it, languageCode) }
        super.attachBaseContext(context ?: newBase)
    }

    private fun updateLanguage() {
        val languageManager = LanguageManager.getInstance()
        val languageCode = languageManager.getLanguage(this)
        languageManager.updateResources(this, languageCode)
    }

    fun changeLanguage(languageCode: String) {
        LanguageManager.getInstance().setLanguage(this, languageCode)
        recreate()
    }
}