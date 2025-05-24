package com.glamvibe.glamvibeadmin.presentation.adapter.promotions

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.CardPromotionBinding
import com.glamvibe.glamvibeadmin.domain.model.Promotion
import com.glamvibe.glamvibeadmin.utils.dpToPx

class PromotionsViewHolder(private val binding: CardPromotionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val radius =
        this.itemView.context.resources.getDimensionPixelSize(R.dimen.corner_radius)

    @SuppressLint("SetTextI18n")
    fun bind(promotion: Promotion) {
        val widthPx = dpToPx(330, binding.root.context)
        val heightPx = dpToPx(220, binding.root.context)

        if (promotion.imageUrl.isEmpty()) {
            Glide.with(binding.root)
                .load(R.drawable.empty_image)
                .transform(RoundedCorners(radius))
                .into(binding.promotionImage)
        } else {
            Glide.with(binding.root)
                .load(promotion.imageUrl)
                .override(widthPx, heightPx)
                .error(R.drawable.empty_image)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(radius)
                    )
                )
                .into(binding.promotionImage)
        }
    }
}