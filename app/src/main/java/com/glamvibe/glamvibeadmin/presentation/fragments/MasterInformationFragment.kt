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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentMasterInformationBinding
import com.glamvibe.glamvibeadmin.presentation.adapter.scheduleInformation.ScheduleAdapter
import com.glamvibe.glamvibeadmin.presentation.viewmodel.master.MasterViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import com.glamvibe.glamvibeadmin.utils.dpToPx
import com.glamvibe.glamvibeadmin.utils.formatDate
import com.glamvibe.glamvibeadmin.utils.getYearWord
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MasterInformationFragment : Fragment() {
    companion object {
        const val ARG_ID = "ARG_ID"
    }

    private lateinit var binding: FragmentMasterInformationBinding
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMasterInformationBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.master_information_title))

        val masterId = arguments?.getInt(ARG_ID)

        val masterViewModel by viewModel<MasterViewModel> { parametersOf(masterId) }

        val scheduleAdapter = ScheduleAdapter()
        binding.schedule.layoutManager = LinearLayoutManager(requireContext())
        binding.schedule.adapter = scheduleAdapter

        masterViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                if (state.master != null) {
                    val photoUrl = state.master.photoUrl

                    val radius =
                        requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius)

                    val widthPx = dpToPx(231, binding.root.context)
                    val heightPx = dpToPx(297, binding.root.context)

                    if (photoUrl.isEmpty()) {
                        Glide.with(binding.root)
                            .load(R.drawable.empty_image)
                            .transform(RoundedCorners(radius))
                            .into(binding.masterPhoto)
                    } else {
                        Glide.with(binding.root)
                            .load(photoUrl)
                            .override(widthPx, heightPx)
                            .error(R.drawable.empty_image)
                            .transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundedCorners(radius)
                                )
                            )
                            .into(binding.masterPhoto)
                    }

                    binding.fullName.text =
                        if (state.master.patronymic != null) "${state.master.lastname} ${state.master.name} ${state.master.patronymic}"
                        else "${state.master.lastname} ${state.master.name}"

                    binding.birthDate.text = formatDate(state.master.birthDate)
                    binding.email.text = "Email: ${state.master.email}"
                    binding.phone.text = state.master.phone

                    binding.dateOfEmployment.text = formatDate(state.master.dateOfEmployment)
                    binding.specialty.text = "Специальность: ${state.master.specialty.lowercase()}"

                    binding.servicesCategories.text =
                        "Категории: " + state.master.categories.joinToString(separator = ", ") { it.lowercase() }

                    binding.workExperience.text =
                        "Опыт работы: ${state.master.workExperience} ${getYearWord(state.master.workExperience)}"

                    scheduleAdapter.submitList(state.master.schedule)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}