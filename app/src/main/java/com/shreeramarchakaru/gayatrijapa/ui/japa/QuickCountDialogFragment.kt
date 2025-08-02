package com.shreeramarchakaru.gayatrijapa.ui.japa

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.databinding.DialogQuickCountBinding

class QuickCountDialogFragment(
    private val onCountAdded: (Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogQuickCountBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_quick_count,
            null,
            false
        )

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Add Quick Count")
            .setPositiveButton("Add") { _, _ ->
                addCount()
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    private fun addCount() {
        val countText = binding.etCount.text.toString().trim()

        if (countText.isEmpty()) {
            Toast.makeText(context, "Please enter count", Toast.LENGTH_SHORT).show()
            return
        }

        val count = countText.toIntOrNull()
        if (count == null || count <= 0) {
            Toast.makeText(context, "Please enter valid count", Toast.LENGTH_SHORT).show()
            return
        }

        onCountAdded(count)
    }
}