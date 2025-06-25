package com.glamvibe.glamvibeadmin.presentation.adapter.editPromotionServices

import android.annotation.SuppressLint
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.CardEditPromotionServiceBinding
import com.glamvibe.glamvibeadmin.domain.model.Service

class EditPromotionServicesViewHolder(private val binding: CardEditPromotionServiceBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(service: Service) {
        val serviceToDisplay = service.name.ifBlank { "Выберите услугу" }
        setupSpinner(serviceToDisplay)

        binding.price.text = if (service.price > 0) service.price.toString() else ""
        binding.priceWithPromotionRuble.isVisible = binding.price.text.isNotEmpty()
        binding.discountPercentage.text = if (service.discountPercentage != null)
            "-${service.discountPercentage}%" else ""
        binding.priceWithPromotion.setText(service.priceWithPromotion?.toString() ?: "")
    }

    private fun setupSpinner(serviceToDisplay: String) {
        val services = listOf(
            "Выберите услугу",
            "Аппаратный маникюр",
            "Кератиновое выпрямление",
            "Классический педикюр"
        )
        val adapter = ArrayAdapter(
            binding.root.context,
            R.layout.spinner_dropdown_item,
            services
        ).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        binding.spinner.adapter = adapter
        val position = services.indexOf(serviceToDisplay).takeIf { it >= 0 } ?: 0
        binding.spinner.setSelection(position)
    }
}