package com.shreeramarchakaru.gayatrijapa.ui.selection.sankalp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.databinding.FragmentSankalpBinding
import com.shreeramarchakaru.gayatrijapa.utils.common.BaseFragment

class SankalpFragment : BaseFragment() {

    private lateinit var binding: FragmentSankalpBinding
    private lateinit var viewModel: SankalpViewModel
    private val args: SankalpFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sankalp,
            container,
            false
        )

        viewModel = ViewModelProvider(this)[SankalpViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Convert String to Long with fallback
        val japaId = args.japaId ?: 1L
        viewModel.loadJapa(japaId)

        observeViewModel()
        setupClickListeners()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.navigateToJapa.observe(viewLifecycleOwner) { japaId ->
            if (japaId != null) {
                val action = SankalpFragmentDirections.actionSankalpToJapa(japaId)
                findNavController().navigate(action)
                viewModel.onNavigatedToJapa()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSankalpDone.setOnClickListener {
            viewModel.onSankalpDone()
        }
    }
}