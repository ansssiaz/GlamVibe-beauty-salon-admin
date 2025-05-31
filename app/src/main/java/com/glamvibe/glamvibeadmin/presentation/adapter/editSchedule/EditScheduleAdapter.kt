package com.glamvibe.glamvibeadmin.presentation.adapter.editSchedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.databinding.CardEditWorkingDayBinding
import com.glamvibe.glamvibeadmin.domain.model.WorkingDay
import com.glamvibe.glamvibeadmin.presentation.adapter.scheduleInformation.ScheduleDiffCallback

class EditScheduleAdapter :
    ListAdapter<WorkingDay, EditScheduleViewHolder>(ScheduleDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditScheduleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardEditWorkingDayBinding.inflate(layoutInflater, parent, false)
        val viewHolder = EditScheduleViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: EditScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}