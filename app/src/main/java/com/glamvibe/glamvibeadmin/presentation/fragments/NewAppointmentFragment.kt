package com.glamvibe.glamvibeadmin.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentNewAppointmentBinding
import com.glamvibe.glamvibeadmin.presentation.viewmodel.newAppointment.NewAppointmentViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import com.glamvibe.glamvibeadmin.utils.Status
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewAppointmentFragment : Fragment() {
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private val newAppointmentViewModel by viewModel<NewAppointmentViewModel>()
    private lateinit var binding: FragmentNewAppointmentBinding
    private lateinit var clientsAdapter: ArrayAdapter<String>
    private lateinit var servicesAdapter: ArrayAdapter<String>
    private lateinit var mastersAdapter: ArrayAdapter<String>
    private var currentClients: List<String> = emptyList()
    private var currentServices: List<String> = emptyList()
    private var currentMasters: List<String> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewAppointmentBinding.inflate(inflater)
        toolbarViewModel.setTitle(getString(R.string.new_appointment_title))

        binding.makeAppointmentButton.isEnabled = false
        binding.makeAppointmentButton.alpha = 0.5f

        clientsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            mutableListOf<String>()
        ).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        binding.clientSpinner.adapter = clientsAdapter

        binding.clientSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected = parent?.getItemAtPosition(position) as? String
                    newAppointmentViewModel.onClientSelected(selected)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    newAppointmentViewModel.onClientSelected(null)
                }
            }

        servicesAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            mutableListOf<String>()
        ).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        binding.serviceSpinner.adapter = servicesAdapter

        binding.serviceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected = parent?.getItemAtPosition(position) as? String
                    newAppointmentViewModel.filterMastersByService(selected)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    newAppointmentViewModel.filterMastersByService(null)
                }
            }

        mastersAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            mutableListOf<String>()
        ).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        binding.masterSpinner.adapter = mastersAdapter

        binding.masterSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected = parent?.getItemAtPosition(position) as? String
                    newAppointmentViewModel.onMasterSelected(selected)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    newAppointmentViewModel.onMasterSelected(null)
                }
            }

        binding.appointmentDatePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = LocalDate(year, monthOfYear + 1, dayOfMonth)
            newAppointmentViewModel.onDateSelected(selectedDate)
        }

        binding.makeAppointmentButton.setOnClickListener {
            val year = binding.appointmentDatePicker.year
            val month = binding.appointmentDatePicker.month + 1
            val day = binding.appointmentDatePicker.dayOfMonth
            val newDate = LocalDate(year, month, day)

            val weekDay = newDate.dayOfWeek

            val checkedChipId = binding.timeChipGroup.checkedChipId

            val chip = binding.timeChipGroup.findViewById<Chip>(checkedChipId)
            val timeText = chip.text.toString()
            val newTime = LocalTime.parse(timeText)
            newAppointmentViewModel.makeAppointment(newDate, newTime, weekDay)
        }

        binding.timeChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val checkedChipId = checkedIds[0]
                val selectedChip =
                    binding.timeChipGroup.findViewById<Chip>(checkedChipId)
                val selectedTime = selectedChip?.tag as? LocalTime
                selectedTime?.let { newAppointmentViewModel.onTimeSelected(it) }
                binding.makeAppointmentButton.isEnabled = true
                binding.makeAppointmentButton.alpha = 1f
            } else {
                binding.makeAppointmentButton.isEnabled = false
                binding.makeAppointmentButton.alpha = 0.5f
            }
        }

        newAppointmentViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                val newClients = listOf("Выберите клиента") + state.clientsNames
                currentClients = newClients

                clientsAdapter.clear()
                clientsAdapter.addAll(newClients)
                clientsAdapter.notifyDataSetChanged()

                val clientsAdapterPosition = state.lastSelectedService?.let { client ->
                    newClients.indexOf(client)
                } ?: 0

                if (clientsAdapterPosition >= 0) {
                    binding.serviceSpinner.setSelection(clientsAdapterPosition)
                }

                val newServices = listOf("Выберите услугу") + state.services.map { it.name }
                currentServices = newServices

                servicesAdapter.clear()
                servicesAdapter.addAll(newServices)
                servicesAdapter.notifyDataSetChanged()

                val serviceAdapterPosition = state.lastSelectedService?.let { service ->
                    newServices.indexOf(service)
                } ?: 0

                if (serviceAdapterPosition >= 0) {
                    binding.serviceSpinner.setSelection(serviceAdapterPosition)
                }

                val masterNames = state.filteredMasters.map {
                    if (it.patronymic != null) "${it.lastname} ${it.name} ${it.patronymic}" else "${it.lastname} ${it.name}"
                }

                val newMasters = listOf("Выберите мастера") + masterNames
                currentMasters = newMasters
                mastersAdapter.clear()
                mastersAdapter.addAll(newMasters)
                mastersAdapter.notifyDataSetChanged()

                val masterAdapterPosition = state.lastSelectedMaster?.let { master ->
                    newMasters.indexOf(master)
                } ?: 0

                if (masterAdapterPosition >= 0) {
                    binding.masterSpinner.setSelection(masterAdapterPosition)
                }

                binding.timeChipGroup.removeAllViews()

                binding.timeChipGroup.isSingleSelection = true

                //binding.noAvailableTimeTitle.isVisible = state.availableSlots.isEmpty()

                state.availableSlots.forEach { time ->
                    val chip = Chip(requireContext())
                    chip.id = View.generateViewId()

                    val timeString = "%02d:%02d".format(time.hour, time.minute)

                    chip.chipBackgroundColor = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.chip_bg_selector
                    )
                    chip.chipStrokeColor =
                        ContextCompat.getColorStateList(requireContext(), R.color.brown)
                    chip.chipStrokeWidth = 1f
                    chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown))
                    chip.text = timeString
                    chip.isCheckable = true
                    chip.tag = time

                    if (state.selectedTime == time) {
                        chip.isChecked = true
                    }

                    binding.timeChipGroup.addView(chip)
                }

                binding.makeAppointmentButton.isEnabled = state.selectedTime != null
                binding.makeAppointmentButton.alpha = if (state.selectedTime != null) 1f else 0.5f

                if (state.status is Status.Error) {
                    val message = (state.status).throwable.message ?: "Неизвестная ошибка"
                    AlertDialog.Builder(requireContext())
                        .setTitle("Ошибка")
                        .setMessage(message)
                        .setPositiveButton("ОК", null)
                        .show()
                }

                if (state.appointment != null) {
                    findNavController().navigateUp()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        return binding.root
    }
}