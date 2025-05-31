package com.glamvibe.glamvibeadmin.presentation.adapter.editSchedule

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.glamvibe.glamvibeadmin.databinding.CardEditWorkingDayBinding
import com.glamvibe.glamvibeadmin.domain.model.WorkingDay
import com.glamvibe.glamvibeadmin.domain.model.getStringByWeekDay
import kotlinx.datetime.LocalTime

class EditScheduleViewHolder(private val binding: CardEditWorkingDayBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(workingDay: WorkingDay) {
        binding.weekDay.text = workingDay.weekDay.getStringByWeekDay().uppercase()

        val emptyValue = LocalTime(0, 0)
        val startTimeText =
            if (workingDay.startTime == emptyValue) "" else workingDay.startTime.toString()
        val endTimeText =
            if (workingDay.endTime == emptyValue) "" else workingDay.endTime.toString()

        binding.startTime.setText(startTimeText)
        binding.endTime.setText(endTimeText)
    }
}