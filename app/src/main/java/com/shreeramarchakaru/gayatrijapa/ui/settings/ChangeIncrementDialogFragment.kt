package com.shreeramarchakaru.gayatrijapa.ui.settings

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.databinding.DialogChangeIncrementBinding


class ChangeIncrementDialogFragment(
    private val onIncrementChanged: (Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogChangeIncrementBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_change_increment,
            null,
            false
        )

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Change Japa Increment")
            .setPositiveButton("Update") { _, _ ->
                updateIncrement()
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    private fun updateIncrement() {
        val incrementText = binding.etIncrement.text.toString().trim()

        if (incrementText.isEmpty()) {
            Toast.makeText(context, "Please enter increment value", Toast.LENGTH_SHORT).show()
            return
        }

        val increment = incrementText.toIntOrNull()
        if (increment == null || increment <= 0) {
            Toast.makeText(context, "Please enter valid increment value", Toast.LENGTH_SHORT).show()
            return
        }

        onIncrementChanged(increment)
    }
}