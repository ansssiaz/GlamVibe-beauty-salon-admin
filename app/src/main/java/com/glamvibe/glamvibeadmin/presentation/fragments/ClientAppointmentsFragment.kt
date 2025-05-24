package com.glamvibe.glamvibeadmin.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentClientAppointmentsBinding
import com.glamvibe.glamvibeadmin.domain.model.Appointment
import com.glamvibe.glamvibeadmin.presentation.adapter.appointments.CurrentAppointmentsAdapter
import com.glamvibe.glamvibeadmin.presentation.adapter.appointments.LastAppointmentsAdapter
import com.glamvibe.glamvibeadmin.presentation.viewmodel.clientAppointments.ClientAppointmentsViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.administrator.AdministratorViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ClientAppointmentsFragment : Fragment() {
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private val administratorViewModel: AdministratorViewModel by activityViewModel<AdministratorViewModel>()
    private lateinit var binding: FragmentClientAppointmentsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClientAppointmentsBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.appointments_title))

        val clientId = administratorViewModel.state.value.administrator?.id

        val clientAppointmentsViewModel: ClientAppointmentsViewModel by viewModel<ClientAppointmentsViewModel> {
            parametersOf(
                clientId
            )
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewAppointmentFragment.APPOINTMENT_CREATED_RESULT,
            viewLifecycleOwner
        ) { _, _ ->
            clientAppointmentsViewModel.getAppointments()
        }

        val currentAppointmentsAdapter = CurrentAppointmentsAdapter(
            object : CurrentAppointmentsAdapter.CurrentAppointmentsListener {
                override fun onConfirmClicked(appointment: Appointment) {
                    TODO("Not yet implemented")
                }

                override fun onCompleteClicked(appointment: Appointment) {
                    TODO("Not yet implemented")
                }

                override fun onCancelClicked(appointment: Appointment) {
                    TODO("Not yet implemented")
                }
            }
        )

        binding.currentAppointmentsList.layoutManager = LinearLayoutManager(requireContext())
        binding.currentAppointmentsList.adapter = currentAppointmentsAdapter

        val lastAppointmentsAdapter = LastAppointmentsAdapter()
        binding.lastAppointmentsList.layoutManager = LinearLayoutManager(requireContext())
        binding.lastAppointmentsList.adapter = lastAppointmentsAdapter

        binding.currentAppointmentsList.isNestedScrollingEnabled = false
        binding.lastAppointmentsList.isNestedScrollingEnabled = false

        clientAppointmentsViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                binding.noCurrentAppointmentsText.isVisible = state.currentAppointments.isEmpty()
                binding.noLastAppointmentsText.isVisible = state.lastAppointments.isEmpty()

                currentAppointmentsAdapter.submitList(state.currentAppointments)
                lastAppointmentsAdapter.submitList(state.lastAppointments)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}