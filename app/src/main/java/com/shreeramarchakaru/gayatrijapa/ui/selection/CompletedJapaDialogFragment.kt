package com.shreeramarchakaru.gayatrijapa.ui.selection

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.databinding.DialogCompletedJapaBinding

class CompletedJapaDialogFragment(
    private val japa: Japa,
    private val onActionSelected: (Action) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogCompletedJapaBinding

    enum class Action {
        RESTART, VIEW_HISTORY, START_NEW
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_completed_japa,
            null,
            false
        )

        binding.japa = japa

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("${japa.name} Completed!")
            .setPositiveButton("Restart This Japa") { _, _ ->
                onActionSelected(Action.RESTART)
            }
            .setNegativeButton("View History") { _, _ ->
                onActionSelected(Action.VIEW_HISTORY)
            }
            .setNeutralButton("Start New") { _, _ ->
                onActionSelected(Action.START_NEW)
            }
            .create()
    }
}