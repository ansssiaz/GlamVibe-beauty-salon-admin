package com.glamvibe.glamvibeadmin.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentProfileBinding
import com.glamvibe.glamvibeadmin.presentation.viewmodel.administrator.AdministratorViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileFragment : Fragment() {
    private val administratorViewModel: AdministratorViewModel by activityViewModel<AdministratorViewModel>()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.profile_title))

        administratorViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                it.administrator?.let { client ->
                    val fullName = listOfNotNull(
                        client.lastname,
                        client.name,
                        client.patronymic
                    ).joinToString(" ")

                    binding.name.text = fullName
                    binding.userInitials.text = buildString {
                        client.lastname.firstOrNull()?.let { letter -> append(letter) }
                        append(" ")
                        client.name.firstOrNull()?.let { letter -> append(letter) }
                    }.uppercase()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.appointmentsItem.setOnClickListener {
            requireParentFragment().requireParentFragment().findNavController()
                .navigate(
                    R.id.action_bottomMenuFragment_to_appointmentsFragment
                )
        }

        binding.profileDataItem.setOnClickListener {
            requireParentFragment().requireParentFragment().findNavController()
                .navigate(
                    R.id.action_bottomMenuFragment_to_profileDataFragment
                )
        }

        binding.exitItem.setOnClickListener {
            administratorViewModel.state.value.administrator?.id?.let { id ->
                administratorViewModel.logOut(id)
            }
            requireParentFragment().requireParentFragment().findNavController()
                .navigate(
                    R.id.action_bottomMenuFragment_to_authorizationFragment
                )
        }

        return binding.root
    }
}