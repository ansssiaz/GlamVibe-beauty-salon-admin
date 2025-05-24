package com.glamvibe.glamvibeadmin.presentation.adapter.promotions

import androidx.recyclerview.widget.DiffUtil
import com.glamvibe.glamvibeadmin.domain.model.Promotion

class PromotionsDiffCallback : DiffUtil.ItemCallback<Promotion>() {
    override fun areItemsTheSame(oldItem: Promotion, newItem: Promotion): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Promotion, newItem: Promotion): Boolean =
        oldItem == newItem
}