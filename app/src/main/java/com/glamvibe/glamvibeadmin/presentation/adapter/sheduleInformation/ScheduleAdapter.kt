package com.glamvibe.glamvibeadmin.presentation.adapter.sheduleInformation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.databinding.CardWorkingDayBinding
import com.glamvibe.glamvibeadmin.domain.model.WorkingDay

class ScheduleAdapter : ListAdapter<WorkingDay, ScheduleViewHolder>(ScheduleDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardWorkingDayBinding.inflate(layoutInflater, parent, false)
        val viewHolder = ScheduleViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}