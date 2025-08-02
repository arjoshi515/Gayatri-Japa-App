package com.shreeramarchakaru.gayatrijapa.ui.history


import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.databinding.DialogAddRemarksBinding


class AddRemarksDialogFragment(
    private val japa: Japa,
    private val onRemarksAdded: (Long, String) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogAddRemarksBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_add_remarks,
            null,
            false
        )

        binding.japa = japa

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Add Remarks for ${japa.name}")
            .setPositiveButton("Save") { _, _ ->
                val remarks = binding.etRemarks.text.toString().trim()
                onRemarksAdded(japa.id, remarks)
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}

