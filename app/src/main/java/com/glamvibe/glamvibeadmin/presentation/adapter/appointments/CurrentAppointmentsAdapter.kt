package com.glamvibe.glamvibeadmin.presentation.adapter.appointments

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.CardAppointmentBinding
import com.glamvibe.glamvibeadmin.domain.model.Appointment
import com.glamvibe.glamvibeadmin.domain.model.AppointmentStatus

class CurrentAppointmentsAdapter(
    private val listener: CurrentAppointmentsListener,
) : ListAdapter<Appointment, AppointmentsViewHolder>(AppointmentsDiffCallback()) {

    interface CurrentAppointmentsListener {
        fun onConfirmClicked(appointment: Appointment)
        fun onCancelClicked(appointment: Appointment)
        fun onCompleteClicked(appointment: Appointment)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppointmentsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardAppointmentBinding.inflate(layoutInflater, parent, false)
        val viewHolder = AppointmentsViewHolder(binding)

        binding.menu.setOnClickListener {
            val appointment = getItem(viewHolder.bindingAdapterPosition)
            val popupMenu = PopupMenu(it.context, it, Gravity.END, 0, R.style.PopupMenu)

            val menu = when (appointment.status) {
                AppointmentStatus.IN_PROCESSING -> R.menu.appointment_in_processing_menu
                AppointmentStatus.WAITING -> R.menu.appointment_waiting_menu
                else -> 0
            }

            if (menu != 0) {
                popupMenu.inflate(menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.confirm -> {
                            listener.onConfirmClicked(getItem(viewHolder.bindingAdapterPosition))
                            true
                        }

                        R.id.complete -> {
                            listener.onCompleteClicked(getItem(viewHolder.bindingAdapterPosition))
                            true
                        }

                        R.id.cancel -> {
                            listener.onCancelClicked(getItem(viewHolder.bindingAdapterPosition))
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}