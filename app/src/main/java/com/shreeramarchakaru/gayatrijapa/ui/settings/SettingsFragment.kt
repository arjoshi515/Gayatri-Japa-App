package com.shreeramarchakaru.gayatrijapa.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.databinding.FragmentSettingsBinding
import com.shreeramarchakaru.gayatrijapa.ui.auth.LoginActivity
import com.shreeramarchakaru.gayatrijapa.utils.common.LanguageSelectionDialogFragment


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        )

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observeViewModel()
        setupClickListeners()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.logoutSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }

        viewModel.showIncrementDialog.observe(viewLifecycleOwner) { show ->
            if (show) {
                showIncrementDialog()
            }
        }

        viewModel.showLanguageDialog.observe(viewLifecycleOwner) { show ->
            if (show) {
                showLanguageDialog()
            }
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnChangeIncrement.setOnClickListener {
            viewModel.onChangeIncrementClicked()
        }

        binding.btnChangeLanguage.setOnClickListener {
            viewModel.onChangeLanguageClicked()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun showIncrementDialog() {
        val dialog = ChangeIncrementDialogFragment { increment ->
            viewModel.updateJapaIncrement(increment)
        }
        dialog.show(parentFragmentManager, "IncrementDialog")
        viewModel.onIncrementDialogShown()
    }

    private fun showLanguageDialog() {
        val dialog = LanguageSelectionDialogFragment { languageCode ->
            viewModel.updateLanguage(languageCode)
            Toast.makeText(context, getString(R.string.language_changed), Toast.LENGTH_SHORT).show()
        }
        dialog.show(parentFragmentManager, "LanguageDialog")
        viewModel.onLanguageDialogShown()
    }
}