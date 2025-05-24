package com.glamvibe.glamvibeadmin.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentAppointmentsBinding
import com.glamvibe.glamvibeadmin.domain.model.Appointment
import com.glamvibe.glamvibeadmin.presentation.adapter.appointments.CurrentAppointmentsAdapter
import com.glamvibe.glamvibeadmin.presentation.viewmodel.administrator.AdministratorViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.appointments.AppointmentsViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppointmentsFragment : Fragment() {
    private val administratorViewModel: AdministratorViewModel by activityViewModel<AdministratorViewModel>()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private val appointmentsViewModel: AppointmentsViewModel by viewModel<AppointmentsViewModel>()
    private lateinit var binding: FragmentAppointmentsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentsBinding.inflate(inflater)
        toolbarViewModel.setTitle(getString(R.string.main_page_title))

        administratorViewModel.checkTokenPair()

        administratorViewModel.getProfileInformation()

        val appointmentsAdapter = CurrentAppointmentsAdapter(
            object : CurrentAppointmentsAdapter.CurrentAppointmentsListener {
                override fun onConfirmClicked(appointment: Appointment) {
                    appointmentsViewModel.confirm(appointment.id, appointment.date)
                }

                override fun onCompleteClicked(appointment: Appointment) {
                    appointmentsViewModel.complete(appointment.id, appointment.date)
                }

                override fun onCancelClicked(appointment: Appointment) {
                    val dialogView = layoutInflater.inflate(R.layout.dialog_cancel_reason, null)
                    val etReason =
                        dialogView.findViewById<TextInputEditText>(R.id.comment_text_edit)
                    val btnCancel = dialogView.findViewById<MaterialButton>(R.id.exit_button)
                    val btnOk = dialogView.findViewById<MaterialButton>(R.id.ok_button)

                    btnOk.alpha = 0.5f
                    btnOk.isEnabled = false

                    val dialog = AlertDialog.Builder(requireContext())
                        .setView(dialogView)
                        .setCancelable(true)
                        .create()

                    dialog.setOnShowListener {
                        btnCancel.setOnClickListener {
                            dialog.dismiss()
                        }

                        btnOk.setOnClickListener {
                            val reason = etReason.text.toString().trim()
                            if (reason.isNotEmpty()) {
                                appointmentsViewModel.cancel(
                                    appointment.id,
                                    appointment.date,
                                    reason
                                )
                                dialog.dismiss()
                            }
                        }
                        etReason.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                btnOk.isEnabled = !s.isNullOrBlank()
                                if (!s.isNullOrBlank()) btnOk.alpha = 1.0f
                            }

                            override fun afterTextChanged(s: Editable?) {}
                        })
                    }
                    dialog.show()
                }
            }
        )

        binding.calendar.setOnDateSelectListener { dateModel ->
            appointmentsViewModel.getAppointmentsForDate(
                dateModel.year,
                dateModel.monthNumber,
                dateModel.day
            )
        }

        binding.appointmentsList.layoutManager = LinearLayoutManager(requireContext())
        binding.appointmentsList.adapter = appointmentsAdapter

        appointmentsViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                appointmentsAdapter.submitList(it.appointments)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        administratorViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it.administrator == null && isAdded) {
                    requireParentFragment().requireParentFragment().findNavController()
                        .navigate(R.id.action_bottomMenuFragment_to_authorizationFragment)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}