package com.glamvibe.glamvibeadmin.presentation.adapter.scheduleInformation

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.glamvibe.glamvibeadmin.databinding.CardWorkingDayBinding
import com.glamvibe.glamvibeadmin.domain.model.WorkingDay
import com.glamvibe.glamvibeadmin.domain.model.getStringByWeekDay

class ScheduleViewHolder(private val binding: CardWorkingDayBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(workingDay: WorkingDay) {
        binding.weekDay.text = workingDay.weekDay.getStringByWeekDay().uppercase()
        binding.startTime.text = workingDay.startTime.toString()
        binding.endTime.text = workingDay.endTime.toString()
    }
}