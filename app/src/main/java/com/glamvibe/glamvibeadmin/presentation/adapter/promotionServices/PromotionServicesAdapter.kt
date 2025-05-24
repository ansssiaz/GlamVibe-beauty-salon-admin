package com.glamvibe.glamvibeadmin.presentation.adapter.promotionServices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.databinding.CardPromotionServiceBinding
import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.presentation.adapter.services.ServicePayload

class PromotionServicesAdapter:
    ListAdapter<Service, PromotionServicesViewHolder>(PromotionServicesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionServicesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardPromotionServiceBinding.inflate(layoutInflater, parent, false)
        val viewHolder = PromotionServicesViewHolder(binding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: PromotionServicesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: PromotionServicesViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is ServicePayload) {
                    holder.bind(it)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }
}