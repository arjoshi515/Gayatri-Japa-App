package com.shreeramarchakaru.gayatrijapa.utils.common

import android.content.Context
import androidx.fragment.app.Fragment
import com.shreeramarchakaru.gayatrijapa.utils.LanguageManager


abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        val languageManager = LanguageManager.getInstance()
        val languageCode = languageManager.getLanguage(context)
        val updatedContext = languageManager.updateResources(context, languageCode)
        super.onAttach(updatedContext)
    }

    protected fun showLanguageDialog() {
        val dialog = LanguageSelectionDialogFragment { languageCode ->
            // Language will be changed and activity will be recreated
        }
        dialog.show(parentFragmentManager, "LanguageDialog")
    }
}