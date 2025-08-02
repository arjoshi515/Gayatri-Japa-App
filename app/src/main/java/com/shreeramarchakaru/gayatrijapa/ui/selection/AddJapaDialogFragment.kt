package com.shreeramarchakaru.gayatrijapa.ui.selection

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.databinding.DialogAddJapaBinding


class AddJapaDialogFragment(
    private val onJapaAdded: (String, String, Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogAddJapaBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_add_japa,
            null,
            false
        )

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Add Custom Japa")
            .setPositiveButton("Add") { _, _ ->
                addJapa()
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    private fun addJapa() {
        val name = binding.etJapaName.text.toString().trim()
        val mantra = binding.etMantra.text.toString().trim()
        val targetCountText = binding.etTargetCount.text.toString().trim()

        if (validateInput(name, mantra, targetCountText)) {
            val targetCount = targetCountText.toInt()
            onJapaAdded(name, mantra, targetCount)
        }
    }

    private fun validateInput(name: String, mantra: String, targetCountText: String): Boolean {
        return when {
            name.isEmpty() -> {
                showError("Please enter japa name")
                false
            }
            mantra.isEmpty() -> {
                showError("Please enter mantra")
                false
            }
            targetCountText.isEmpty() -> {
                showError("Please enter target count")
                false
            }
            targetCountText.toIntOrNull() == null || targetCountText.toInt() <= 0 -> {
                showError("Please enter valid target count")
                false
            }
            else -> true
        }
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}