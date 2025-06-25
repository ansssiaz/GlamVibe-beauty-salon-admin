package com.glamvibe.glamvibeadmin.presentation.adapter.promotions

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.CardPromotionBinding
import com.glamvibe.glamvibeadmin.domain.model.Promotion

class PromotionsAdapter(private val listener: PromotionsListener) :
    ListAdapter<Promotion, PromotionsViewHolder>(PromotionsDiffCallback()) {

    interface PromotionsListener {
        fun onEditClicked(promotion: Promotion)
        fun onDeleteClicked(promotion: Promotion)
        fun onPromotionImageClicked(promotion: Promotion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardPromotionBinding.inflate(layoutInflater, parent, false)
        val viewHolder = PromotionsViewHolder(binding)

        binding.promotionImage.setOnClickListener {
            listener.onPromotionImageClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it, Gravity.END, 0, R.style.PopupMenu).apply {
                inflate(R.menu.catalog_item_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.delete -> {
                            listener.onDeleteClicked(getItem(viewHolder.bindingAdapterPosition))
                            true
                        }

                        R.id.edit -> {
                            listener.onEditClicked(getItem(viewHolder.bindingAdapterPosition))
                            true
                        }

                        else -> false
                    }
                }
                show()
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: PromotionsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}