package com.glamvibe.glamvibeadmin.presentation.adapter.promotions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.databinding.CardPromotionBinding
import com.glamvibe.glamvibeadmin.domain.model.Promotion

class PromotionsAdapter(private val listener: PromotionsListener) :
    ListAdapter<Promotion, PromotionsViewHolder>(PromotionsDiffCallback()) {

    interface PromotionsListener {
        fun onPromotionImageClicked(promotion: Promotion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardPromotionBinding.inflate(layoutInflater, parent, false)
        val viewHolder = PromotionsViewHolder(binding)

        binding.promotionImage.setOnClickListener {
            listener.onPromotionImageClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: PromotionsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}