package com.glamvibe.glamvibeclient.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.glamvibe.glamvibeclient.R
import com.glamvibe.glamvibeclient.databinding.FragmentCatalogMastersBinding
import com.glamvibe.glamvibeclient.presentation.adapter.masters.MastersAdapter
import com.glamvibe.glamvibeclient.presentation.viewmodel.masters.MastersViewModel
import com.glamvibe.glamvibeclient.presentation.viewmodel.toolbar.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MastersCatalogFragment : Fragment() {
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private lateinit var binding: FragmentCatalogMastersBinding
    private lateinit var categoriesAdapter: ArrayAdapter<String>
    private var currentCategories: List<String> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogMastersBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.catalog_masters_title))

        val mastersViewModel: MastersViewModel by viewModel<MastersViewModel>()

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

        val mastersAdapter = MastersAdapter()

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