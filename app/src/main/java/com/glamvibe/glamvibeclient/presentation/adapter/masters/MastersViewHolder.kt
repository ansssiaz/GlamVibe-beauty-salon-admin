package com.glamvibe.glamvibeclient.presentation.adapter.masters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.glamvibe.glamvibeclient.R
import com.glamvibe.glamvibeclient.databinding.CardMasterBinding
import com.glamvibe.glamvibeclient.domain.model.Master

class MastersViewHolder(private val binding: CardMasterBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val radius =
        this.itemView.context.resources.getDimensionPixelSize(R.dimen.corner_radius)

    @SuppressLint("SetTextI18n")
    fun bind(master: Master) {
        binding.masterName.text =
            if (master.patronymic != null) "${master.lastname} ${master.name} ${master.patronymic}" else "${master.lastname} ${master.name}"

        binding.specialty.text = master.specialty

        binding.workExperience.text =
            "Опыт работы: ${master.workExperience} ${getYearWord(master.workExperience)}"

        if (master.photoUrl.isEmpty()) {
            Glide.with(binding.root)
                .load(R.drawable.empty_image)
                .transform(RoundedCorners(radius))
                .into(binding.masterPhoto)
        } else {
            Glide.with(binding.root)
                .load(master.photoUrl)
                .error(R.drawable.empty_image)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(radius)
                    )
                )
                .into(binding.masterPhoto)
        }
    }

    private fun getYearWord(years: Int): String {
        val mod10 = years % 10
        val mod100 = years % 100

        return if (mod100 in 11..14) {
            "лет"
        } else {
            when (mod10) {
                1 -> "год"
                2, 3, 4 -> "года"
                else -> "лет"
            }
        }
    }

}