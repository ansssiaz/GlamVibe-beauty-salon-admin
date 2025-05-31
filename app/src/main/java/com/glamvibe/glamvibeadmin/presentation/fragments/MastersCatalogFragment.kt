package com.glamvibe.glamvibeadmin.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentCatalogMastersBinding
import com.glamvibe.glamvibeadmin.domain.model.Master
import com.glamvibe.glamvibeadmin.presentation.adapter.masters.MastersAdapter
import com.glamvibe.glamvibeadmin.presentation.viewmodel.masters.MastersViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MastersCatalogFragment : Fragment() {
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    val mastersViewModel: MastersViewModel by viewModel<MastersViewModel>()
    private lateinit var binding: FragmentCatalogMastersBinding
    private lateinit var categoriesAdapter: ArrayAdapter<String>
    private var currentCategories: List<String> = emptyList()

    override fun onStop() {
        super.onStop()
        toolbarViewModel.setAddVisibility(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogMastersBinding.inflate(inflater)

        toolbarViewModel.setAddVisibility(true)

        toolbarViewModel.setTitle(getString(R.string.catalog_masters_title))

        toolbarViewModel.addClicked
            .filter { it }
            .onEach {
                findNavController().navigate(
                    R.id.action_mastersCatalogFragment_to_newMasterFragment
                )
                toolbarViewModel.addClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewMasterFragment.MASTER_CREATED_RESULT,
            viewLifecycleOwner
        ) { _, _ ->
            mastersViewModel.loadMasters()
        }

        categoriesAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            mutableListOf<String>()
        ).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        binding.spinner.adapter = categoriesAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selected = parent?.getItemAtPosition(position) as? String
                mastersViewModel.filterMastersByCategory(selected)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                mastersViewModel.filterMastersByCategory(null)
            }
        }

        val mastersAdapter = MastersAdapter(
            object : MastersAdapter.MastersListener {
                override fun onEditClicked(master: Master) {
                    findNavController().navigate(
                        R.id.action_mastersCatalogFragment_to_newMasterFragment,
                        bundleOf(MasterInformationFragment.ARG_ID to master.id)
                    )
                }

                override fun onDeleteClicked(master: Master) {
                    TODO("Not yet implemented")
                }

                override fun onMasterPhotoClicked(master: Master) {
                    findNavController().navigate(
                        R.id.action_mastersCatalogFragment_to_masterInformationFragment,
                        bundleOf(MasterInformationFragment.ARG_ID to master.id)
                    )
                }
            }
        )

        binding.listOfMasters.isNestedScrollingEnabled = false
        binding.listOfMasters.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.listOfMasters.adapter = mastersAdapter

        mastersViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach()
            { state ->
                mastersAdapter.submitList(state.filteredMasters)

                val newCategories = listOf("Все категории") + state.categories
                currentCategories = newCategories

                categoriesAdapter.clear()
                categoriesAdapter.addAll(newCategories)
                categoriesAdapter.notifyDataSetChanged()

                val position = state.lastSelectedCategory?.let { category ->
                    newCategories.indexOf(category)
                } ?: 0

                if (position >= 0) {
                    binding.spinner.setSelection(position)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}