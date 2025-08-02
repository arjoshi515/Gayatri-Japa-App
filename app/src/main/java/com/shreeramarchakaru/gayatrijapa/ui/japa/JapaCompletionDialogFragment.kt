package com.shreeramarchakaru.gayatrijapa.ui.japa

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.databinding.DialogJapaCompletionBinding

class JapaCompletionDialogFragment(
    private val onComplete: (String) -> Unit,
    private val onSkip: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogJapaCompletionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_japa_completion,
            null,
            false
        )

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("🎉 Target Reached!")
            .setMessage("Congratulations! You have reached your target count. Would you like to complete this japa or continue?")
            .setPositiveButton("Complete Japa") { _, _ ->
                val remarks = binding.etRemarks.text.toString().trim()
                onComplete(remarks)
            }
            .setNegativeButton("Continue Japa") { _, _ ->
                onSkip()
            }
            .setCancelable(false)
            .create()
    }
}