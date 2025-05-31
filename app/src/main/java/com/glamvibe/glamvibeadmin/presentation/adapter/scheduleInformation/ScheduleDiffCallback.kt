package com.glamvibe.glamvibeadmin.presentation.adapter.scheduleInformation

import androidx.recyclerview.widget.DiffUtil
import com.glamvibe.glamvibeadmin.domain.model.WorkingDay

class ScheduleDiffCallback : DiffUtil.ItemCallback<WorkingDay>() {
    override fun areItemsTheSame(oldItem: WorkingDay, newItem: WorkingDay): Boolean =
        oldItem.weekDay == newItem.weekDay

    override fun areContentsTheSame(oldItem: WorkingDay, newItem: WorkingDay): Boolean =
        oldItem == newItem
}