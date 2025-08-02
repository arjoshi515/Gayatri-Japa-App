package com.shreeramarchakaru.gayatrijapa.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.databinding.FragmentHistoryBinding
import com.shreeramarchakaru.gayatrijapa.ui.history.adapter.HistoryAdapter
import com.shreeramarchakaru.gayatrijapa.utils.common.BaseFragment

class HistoryFragment : BaseFragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_history,
            container,
            false
        )

        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
        setupClickListeners()

        return binding.root
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter { japa ->
            showRemarksDialog(japa)
        }

        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshHistory()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.japas.observe(viewLifecycleOwner) { japas ->
            historyAdapter.submitList(japas)
        }
    }

    private fun setupClickListeners() {
        binding.fabFilter.setOnClickListener {
            toggleFilterVisibility()
        }

        binding.btnFilterAll.setOnClickListener {
            viewModel.setFilter(HistoryViewModel.FilterType.ALL)
        }

        binding.btnFilterCompleted.setOnClickListener {
            viewModel.setFilter(HistoryViewModel.FilterType.COMPLETED)
        }

        binding.btnFilterInProgress.setOnClickListener {
            viewModel.setFilter(HistoryViewModel.FilterType.IN_PROGRESS)
        }

        binding.btnStartJapa.setOnClickListener {
            // Navigate to japa selection
            findNavController().navigate(R.id.japaSelectionFragment)
        }
    }

    private fun toggleFilterVisibility() {
        val isVisible = binding.llFilterSection.visibility == View.VISIBLE
        binding.llFilterSection.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    private fun showRemarksDialog(japa: Japa) {
        val dialog = AddRemarksDialogFragment(japa) { japaId, remarks ->
            viewModel.addRemarks(japaId, remarks)
        }
        dialog.show(parentFragmentManager, "RemarksDialog")
    }
}