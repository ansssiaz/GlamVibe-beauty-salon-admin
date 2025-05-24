package com.glamvibe.glamvibeadmin.presentation.adapter.appointments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.databinding.CardAppointmentBinding
import com.glamvibe.glamvibeadmin.domain.model.Appointment

class LastAppointmentsAdapter :
    ListAdapter<Appointment, AppointmentsViewHolder>(AppointmentsDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppointmentsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardAppointmentBinding.inflate(layoutInflater, parent, false)
        val viewHolder = AppointmentsViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}