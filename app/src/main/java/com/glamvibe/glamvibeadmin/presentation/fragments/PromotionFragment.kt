package com.glamvibe.glamvibeadmin.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentPromotionBinding
import com.glamvibe.glamvibeadmin.presentation.adapter.promotionServices.PromotionServicesAdapter
import com.glamvibe.glamvibeadmin.presentation.viewmodel.promotion.PromotionViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import com.glamvibe.glamvibeadmin.utils.dpToPx
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.format.TextStyle
import java.util.Locale

class PromotionFragment : Fragment() {
    companion object {
        const val ARG_ID = "ARG_ID"
    }

    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private lateinit var binding: FragmentPromotionBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPromotionBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.promotion_information_title))

        val promotionId = arguments?.getInt(ServiceInformationFragment.ARG_ID)

        val promotionViewModel by viewModel<PromotionViewModel> { parametersOf(promotionId) }

        val promotionServicesAdapter = PromotionServicesAdapter()

        binding.listOfServices.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.listOfServices.adapter = promotionServicesAdapter

        promotionViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                if (state.promotion != null) {
                    val imageUrl = state.promotion.imageUrl

                    val radius =
                        requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius)

                    val widthPx = dpToPx(330, binding.root.context)
                    val heightPx = dpToPx(220, binding.root.context)

                    if (imageUrl.isEmpty()) {
                        Glide.with(binding.root)
                            .load(R.drawable.empty_image)
                            .transform(RoundedCorners(radius))
                            .into(binding.promotionImage)
                    } else {
                        Glide.with(binding.root)
                            .load(imageUrl)
                            .override(widthPx, heightPx)
                            .error(R.drawable.empty_image)
                            .transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundedCorners(radius)
                                )
                            )
                            .into(binding.promotionImage)
                    }
                    binding.promotionName.text = state.promotion.name

                    val formattedStartDate = formatPromotionDate(state.promotion.startDate)
                    val formattedEndDate = formatPromotionDate(state.promotion.endDate)

                    binding.promotionDates.text = "$formattedStartDate - $formattedEndDate"
                    binding.promotionDescription.text = state.promotion.description
                    promotionServicesAdapter.submitList(state.promotion.services)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    private fun formatPromotionDate(date: LocalDate): String {
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val year = date.year.toString()
        return "$day $month $year"
    }
}