package com.shreeramarchakaru.gayatrijapa.ui.japa

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.shreeramarchakaru.gayatrijapa.R

class CompleteJapaConfirmationDialogFragment(
    private val onConfirm: (Boolean) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Complete Japa")
            .setMessage("Are you sure you want to complete this japa? This action cannot be undone.")
            .setPositiveButton("Yes, Complete") { _, _ ->
                onConfirm(true)
            }
            .setNegativeButton("Cancel") { _, _ ->
                onConfirm(false)
            }
            .setCancelable(true)
            .create()
    }
}