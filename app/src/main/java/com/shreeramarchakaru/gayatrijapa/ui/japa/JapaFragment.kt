package com.shreeramarchakaru.gayatrijapa.ui.japa

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.databinding.FragmentJapaBinding
import com.shreeramarchakaru.gayatrijapa.utils.common.BaseFragment

class JapaFragment :  BaseFragment() {

    private lateinit var binding: FragmentJapaBinding
    private lateinit var viewModel: JapaViewModel
    private val args: JapaFragmentArgs by navArgs()

    private var screenTimeoutHandler: Handler? = null
    private var screenTimeoutRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_japa,
            container,
            false
        )

        viewModel = ViewModelProvider(this)[JapaViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.loadJapa(args.japaId?.toLong()!!)

        observeViewModel()
        setupClickListeners()
        setupScreenTimeout()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Keep screen on during japa
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        resetScreenTimeout()
    }

    override fun onPause() {
        super.onPause()
        // Remove keep screen on flag
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        cancelScreenTimeout()
        viewModel.saveCurrentState()
    }

    private fun observeViewModel() {
        viewModel.showCompletionDialog.observe(viewLifecycleOwner) { show ->
            if (show) {
                showCompletionDialog()
            }
        }

        viewModel.showQuickCountDialog.observe(viewLifecycleOwner) { show ->
            if (show) {
                showQuickCountDialog()
            }
        }

        viewModel.navigateToHistory.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                // Navigate to history page
                findNavController().navigate(R.id.historyFragment)
                viewModel.onNavigatedToHistory()
            }
        }

        viewModel.isJapaCompleted.observe(viewLifecycleOwner) { isCompleted ->
            // Update UI based on completion status
            binding.btnComplete.text = if (isCompleted) {
                getString(R.string.view_history)
            } else {
                getString(R.string.complete_japa)
            }
        }
    }

    private fun setupClickListeners() {
        binding.ivJapaImage.setOnClickListener {
            if (!(viewModel.isJapaCompleted.value ?: false)) {
                viewModel.onJapaImageClicked()
                resetScreenTimeout()
            }
        }

        binding.btnIncrease.setOnClickListener {
            viewModel.increaseCount()
            resetScreenTimeout()
        }

        binding.btnDecrease.setOnClickListener {
            viewModel.decreaseCount()
            resetScreenTimeout()
        }

        binding.btn108.setOnClickListener {
            viewModel.setQuickCount(108)
            resetScreenTimeout()
        }

        binding.btn1008.setOnClickListener {
            viewModel.setQuickCount(1008)
            resetScreenTimeout()
        }

        binding.btnComplete.setOnClickListener {
            val isCompleted = viewModel.isJapaCompleted.value ?: false
            if (isCompleted) {
                // Navigate to history
                findNavController().navigate(R.id.historyFragment)
            } else {
                // Show confirmation dialog before completing
                showCompleteJapaConfirmationDialog()
            }
        }

    }
    private fun showCompleteJapaConfirmationDialog() {
        val dialog = CompleteJapaConfirmationDialogFragment { confirmed ->
            if (confirmed) {
                viewModel.completeJapa()
            }
        }
        dialog.show(parentFragmentManager, "CompleteConfirmationDialog")
    }
    private fun setupScreenTimeout() {
        screenTimeoutHandler = Handler(Looper.getMainLooper())
        screenTimeoutRunnable = Runnable {
            viewModel.onScreenTimeout()
        }
    }

    private fun resetScreenTimeout() {
        screenTimeoutHandler?.removeCallbacks(screenTimeoutRunnable!!)
        screenTimeoutHandler?.postDelayed(screenTimeoutRunnable!!, 120000) // 2 minutes
    }

    private fun cancelScreenTimeout() {
        screenTimeoutHandler?.removeCallbacks(screenTimeoutRunnable!!)
    }

    private fun showCompletionDialog() {
        val dialog = JapaCompletionDialogFragment(
            onComplete = { remarks ->
                viewModel.saveCompletionRemarks(remarks)
                viewModel.completeJapa()
            },
            onSkip = {
                viewModel.onSkipCompletion()
            }
        )
        dialog.show(parentFragmentManager, "CompletionDialog")
        viewModel.onCompletionDialogShown()
    }

    private fun showQuickCountDialog() {
        val dialog = QuickCountDialogFragment { count ->
            viewModel.addQuickCount(count)
        }
        dialog.show(parentFragmentManager, "QuickCountDialog")
        viewModel.onQuickCountDialogShown()
    }
}