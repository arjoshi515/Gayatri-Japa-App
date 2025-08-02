package com.shreeramarchakaru.gayatrijapa.utils.common

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shreeramarchakaru.gayatrijapa.MainActivity
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.utils.LanguageManager

class LanguageSelectionDialogFragment(
    private val onLanguageSelected: ((String) -> Unit)? = null
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val recyclerView = RecyclerView(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val languages = LanguageManager.getInstance().getAllLanguages()
        val currentLanguage = LanguageManager.getInstance().getLanguage(requireContext())

        val adapter = LanguageAdapter(languages, currentLanguage) { selectedLanguage ->
            LanguageManager.getInstance().setLanguage(requireContext(), selectedLanguage)

            onLanguageSelected?.invoke(selectedLanguage)

            // Restart activity to apply language change
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()

            dismiss()
        }

        recyclerView.adapter = adapter

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.select_language)
            .setView(recyclerView)
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}