package com.glamvibe.glamvibeclient.presentation.adapter.services

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.glamvibe.glamvibeclient.R
import com.glamvibe.glamvibeclient.databinding.CardServiceBinding
import com.glamvibe.glamvibeclient.domain.model.Service
import com.glamvibe.glamvibeclient.utils.dpToPx

class ServiceViewHolder(private val binding: CardServiceBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val radius =
        this.itemView.context.resources.getDimensionPixelSize(R.dimen.corner_radius)

    fun bind(payload: ServicePayload) {
        if (payload.favourite != null) {
            updateFavourite(payload.favourite)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(service: Service) {
        binding.serviceName.text = service.name

        if (service.priceWithPromotion != null && service.discountPercentage != null) {
            binding.price.text = service.priceWithPromotion.toString()
            binding.price.setTextColor(getColor(this.itemView.context, R.color.mauve))

            binding.ruble.setImageResource(R.drawable.baseline_currency_ruble_24_mauve)

            binding.oldPrice.text = service.price.toString()
            binding.oldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            binding.discountPercentage.text = "-${service.discountPercentage}%"
        } else {
            binding.price.text = service.price.toString()
            binding.oldPriceRuble.isVisible = false
            binding.promotion.isVisible = false
        }

        binding.duration.text = service.duration.toString()

        updateFavourite(service.isFavourite)

        val widthPx = dpToPx(250, binding.root.context)
        val heightPx = dpToPx(250, binding.root.context)

        if (service.imageUrl.isEmpty()) {
            Glide.with(binding.root)
                .load(R.drawable.empty_image)
                .transform(RoundedCorners(radius))
                .into(binding.serviceImage)
        } else {
            Glide.with(binding.root)
                .load(service.imageUrl)
                .override(widthPx, heightPx)
                .error(R.drawable.empty_image)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(radius)
                    )
                )
                .into(binding.serviceImage)
        }
    }

    private fun updateFavourite(isFavourite: Boolean) {
        binding.favourite.setIconResource(
            if (isFavourite) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )
    }
}