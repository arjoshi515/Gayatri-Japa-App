package com.shreeramarchakaru.gayatrijapa.ui.selection

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.databinding.FragmentJapaSelectionBinding
import com.shreeramarchakaru.gayatrijapa.ui.selection.adapter.JapaAdapter
import com.shreeramarchakaru.gayatrijapa.utils.common.BaseFragment

class JapaSelectionFragment : BaseFragment() {

    private lateinit var binding: FragmentJapaSelectionBinding
    private lateinit var viewModel: JapaSelectionViewModel
    private lateinit var japaAdapter: JapaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_japa_selection,
            container,
            false
        )

        viewModel = ViewModelProvider(this)[JapaSelectionViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupRecyclerView()
        observeViewModel()
        setupClickListeners()

        return binding.root
    }

    private fun setupRecyclerView() {
        japaAdapter = JapaAdapter { japa ->
            // Check if japa is already started and not completed
            if (japa.isStarted && !japa.isCompleted) {
                // Skip Sankalp and go directly to Japa
                val action = JapaSelectionFragmentDirections.actionSelectionToJapa(japa.id)
                findNavController().navigate(action)
            } else if (japa.isCompleted) {
                // If completed, show history or restart option
                showCompletedJapaDialog(japa)
            } else {
                // Go to Sankalp first for new japas
                val action = JapaSelectionFragmentDirections.actionSelectionToSankalp(japa.id)
                findNavController().navigate(action)
            }
        }

        binding.rvJapas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = japaAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.japas.observe(viewLifecycleOwner) { japas ->
            japaAdapter.submitList(japas)
        }

        viewModel.showAddJapaDialog.observe(viewLifecycleOwner) { show ->
            if (show) {
                showAddJapaDialog()
            }
        }

        viewModel.gayatriJapaStatus.observe(viewLifecycleOwner) { japa ->
            japa?.let { handleGayatriJapaNavigation(it) }
        }
    }

    private fun setupClickListeners() {
        binding.btnAddCustomJapa.setOnClickListener {
            viewModel.onAddCustomJapaClicked()
        }

        binding.btnGayatriJapa.setOnClickListener {
            // Check Gayatri japa status before navigation
            viewModel.checkGayatriJapaStatus()
        }
    }

    private fun handleGayatriJapaNavigation(japa: com.shreeramarchakaru.gayatrijapa.data.models.Japa) {
        when {
            japa.isStarted && !japa.isCompleted -> {
                // Japa in progress - go directly to Japa page
                val action = JapaSelectionFragmentDirections.actionSelectionToJapa(japa.id)
                findNavController().navigate(action)
            }
            japa.isCompleted -> {
                // Japa completed - show restart option
                showCompletedJapaDialog(japa)
            }
            else -> {
                // New japa - go to Sankalp first
                val action = JapaSelectionFragmentDirections.actionSelectionToSankalp(japa.id)
                findNavController().navigate(action)
            }
        }
    }

    private fun showCompletedJapaDialog(japa: com.shreeramarchakaru.gayatrijapa.data.models.Japa) {
        val dialog = CompletedJapaDialogFragment(japa) { action ->
            when (action) {
                CompletedJapaDialogFragment.Action.RESTART -> {
                    viewModel.restartJapa(japa.id)
                    val navAction = JapaSelectionFragmentDirections.actionSelectionToSankalp(japa.id)
                    findNavController().navigate(navAction)
                }
                CompletedJapaDialogFragment.Action.VIEW_HISTORY -> {
                    findNavController().navigate(R.id.historyFragment)
                }
                CompletedJapaDialogFragment.Action.START_NEW -> {
                    viewModel.createNewJapaFromTemplate(japa)
                }
            }
        }
        dialog.show(parentFragmentManager, "CompletedJapaDialog")
    }

    private fun showAddJapaDialog() {
        val dialog = AddJapaDialogFragment { name, mantra, targetCount ->
            viewModel.addCustomJapa(name, mantra, targetCount)
        }
        dialog.show(parentFragmentManager, "AddJapaDialog")
        viewModel.onAddJapaDialogShown()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_language -> {
                showLanguageDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}