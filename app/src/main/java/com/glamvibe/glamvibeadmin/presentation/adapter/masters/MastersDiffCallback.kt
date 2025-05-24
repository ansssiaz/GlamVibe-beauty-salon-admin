package com.glamvibe.glamvibeadmin.presentation.adapter.masters

import androidx.recyclerview.widget.DiffUtil
import com.glamvibe.glamvibeadmin.domain.model.Master

class MastersDiffCallback : DiffUtil.ItemCallback<Master>() {
    override fun areItemsTheSame(oldItem: Master, newItem: Master): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Master, newItem: Master): Boolean =
        oldItem == newItem
}