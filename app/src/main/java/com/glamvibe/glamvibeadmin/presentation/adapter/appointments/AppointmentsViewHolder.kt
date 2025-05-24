package com.glamvibe.glamvibeadmin.presentation.adapter.appointments

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.CardAppointmentBinding
import com.glamvibe.glamvibeadmin.domain.model.Appointment
import com.glamvibe.glamvibeadmin.domain.model.AppointmentStatus
import com.glamvibe.glamvibeadmin.domain.model.getStringByStatus
import com.glamvibe.glamvibeadmin.domain.model.getStringByWeekDay
import java.time.format.TextStyle
import java.util.Locale

class AppointmentsViewHolder(private val binding: CardAppointmentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(appointment: Appointment) {
        when (appointment.status) {
            AppointmentStatus.IN_PROCESSING, AppointmentStatus.WAITING -> {
                binding.statusIcon.setImageResource(R.drawable.baseline_change_circle_24)
            }

            AppointmentStatus.DONE -> {
                binding.statusIcon.setImageResource(R.drawable.baseline_check_circle_24)
                binding.menu.isVisible = false
            }

            else -> {
                binding.statusIcon.setImageResource(R.drawable.baseline_remove_circle_24)
                binding.menu.isVisible = false
            }
        }

        binding.status.text = appointment.status.getStringByStatus()

        binding.month.text =
            appointment.date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

        binding.day.text = appointment.date.dayOfMonth.toString()

        if (appointment.client.patronymic != null) {
            binding.clientData.text =
                "${appointment.client.lastname} ${appointment.client.name.first()}.${appointment.client.patronymic.first()}."
        } else {
            binding.clientData.text =
                "${appointment.client.lastname} ${appointment.client.name.first()}."
        }

        if (appointment.master.patronymic != null) {
            binding.masterData.text =
                "${appointment.master.lastname} ${appointment.master.name.first()}.${appointment.master.patronymic.first()}."
        } else {
            binding.masterData.text =
                "${appointment.master.lastname} ${appointment.master.name.first()}."
        }

        binding.service.text = appointment.service

        binding.weekDay.text = appointment.weekDay.getStringByWeekDay()
            .uppercase(Locale.getDefault())

        binding.time.text = appointment.time.toString()

        binding.comment.text = appointment.comment
    }
}