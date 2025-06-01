package com.glamvibe.glamvibeadmin.presentation.adapter.clients

import androidx.recyclerview.widget.DiffUtil
import com.glamvibe.glamvibeadmin.domain.model.User

class ClientsDiffCallback: DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}