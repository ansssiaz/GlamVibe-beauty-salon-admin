package com.glamvibe.glamvibeadmin.presentation.adapter.promotionServices

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.CardPromotionServiceBinding
import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.presentation.adapter.services.ServicePayload
import com.glamvibe.glamvibeadmin.utils.dpToPx

class PromotionServicesViewHolder(private val binding: CardPromotionServiceBinding) :
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

        updateFavourite(service.isFavourite)

        binding.duration.text = service.duration.toString()

        val widthPx = dpToPx(150, binding.root.context)
        val heightPx = dpToPx(150, binding.root.context)

        if (service.imageUrl.isEmpty()) {
            Glide.with(binding.root)
                .load(R.drawable.empty_image)
                .transform(RoundedCorners(radius))
                .into(binding.serviceImage)
        } else {
            Glide.with(binding.root)
                .load(service.imageUrl)
                .apply(
                    RequestOptions()
                        .override(widthPx, heightPx)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
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