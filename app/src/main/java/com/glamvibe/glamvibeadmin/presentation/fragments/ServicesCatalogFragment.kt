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
import androidx.recyclerview.widget.LinearLayoutManager
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentCatalogServicesBinding
import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.presentation.adapter.services.ServicesAdapter
import com.glamvibe.glamvibeadmin.presentation.viewmodel.services.ServicesViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServicesCatalogFragment : Fragment() {
    private lateinit var binding: FragmentCatalogServicesBinding
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private val servicesViewModel: ServicesViewModel by viewModel<ServicesViewModel>()
    private lateinit var categoriesAdapter: ArrayAdapter<String>
    private var currentCategories: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogServicesBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.catalog_services_title))

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
                servicesViewModel.filterServicesByCategory(selected)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                servicesViewModel.filterServicesByCategory(null)
            }
        }

        val servicesAdapter = ServicesAdapter(
            object : ServicesAdapter.ServicesListener {
                override fun onFavouriteClicked(service: Service) {

                }

                override fun onServiceImageClicked(service: Service) {
                    findNavController().navigate(
                        R.id.action_servicesCatalogFragment_to_serviceInformationFragment,
                        bundleOf(ServiceInformationFragment.ARG_ID to service.id)
                    )
                }
            }
        )

        binding.listOfServices.isNestedScrollingEnabled = false
        binding.listOfServices.layoutManager = LinearLayoutManager(requireContext())
        binding.listOfServices.adapter = servicesAdapter

        servicesViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                servicesAdapter.submitList(state.filteredServices)

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