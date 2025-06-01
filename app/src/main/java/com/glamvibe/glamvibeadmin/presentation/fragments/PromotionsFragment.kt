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
import com.glamvibe.glamvibeadmin.databinding.FragmentPromotionsBinding
import com.glamvibe.glamvibeadmin.domain.model.Promotion
import com.glamvibe.glamvibeadmin.presentation.adapter.promotions.PromotionsAdapter
import com.glamvibe.glamvibeadmin.presentation.viewmodel.promotions.PromotionsViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PromotionsFragment : Fragment() {
    private lateinit var binding: FragmentPromotionsBinding
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private val promotionsViewModel: PromotionsViewModel by viewModel<PromotionsViewModel>()
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
        binding = FragmentPromotionsBinding.inflate(inflater)

        toolbarViewModel.setAddVisibility(true)

        toolbarViewModel.setTitle(getString(R.string.promotions_title))

        toolbarViewModel.addClicked
            .filter { it }
            .onEach {
                findNavController().navigate(
                    R.id.action_promotionsFragment_to_newPromotionFragment
                )
                toolbarViewModel.addClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewPromotionFragment.PROMOTION_CREATED_RESULT,
            viewLifecycleOwner
        ) { _, _ ->
            promotionsViewModel.getPromotions()
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
                promotionsViewModel.filterPromotionsByCategory(selected)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                promotionsViewModel.filterPromotionsByCategory(null)
            }
        }

        val promotionsAdapter = PromotionsAdapter(
            object : PromotionsAdapter.PromotionsListener {
                override fun onEditClicked(promotion: Promotion) {
                    findNavController().navigate(
                        R.id.action_promotionsFragment_to_newPromotionFragment,
                        bundleOf(NewPromotionFragment.ARG_ID to promotion.id)
                    )
                }

                override fun onDeleteClicked(promotion: Promotion) {
                    //promotionsViewModel.deletePromotion(promotion.id)
                }

                override fun onPromotionImageClicked(promotion: Promotion) {
                    findNavController().navigate(
                        R.id.action_promotionsFragment_to_promotionFragment,
                        bundleOf(PromotionFragment.ARG_ID to promotion.id)
                    )
                }
            }
        )

        binding.listOfPromotions.isNestedScrollingEnabled = false
        binding.listOfPromotions.layoutManager = LinearLayoutManager(requireContext())
        binding.listOfPromotions.adapter = promotionsAdapter

        promotionsViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                promotionsAdapter.submitList(state.filteredPromotions)

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