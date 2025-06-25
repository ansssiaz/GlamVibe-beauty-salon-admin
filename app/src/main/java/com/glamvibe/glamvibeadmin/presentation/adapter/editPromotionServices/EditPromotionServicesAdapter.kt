package com.glamvibe.glamvibeadmin.presentation.adapter.editPromotionServices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.databinding.CardEditPromotionServiceBinding
import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.presentation.adapter.promotionServices.PromotionServicesDiffCallback

class EditPromotionServicesAdapter :
    ListAdapter<Service, EditPromotionServicesViewHolder>(PromotionServicesDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditPromotionServicesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardEditPromotionServiceBinding.inflate(layoutInflater, parent, false)
        val viewHolder = EditPromotionServicesViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: EditPromotionServicesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}