package com.glamvibe.glamvibeadmin.presentation.adapter.services

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.CardServiceBinding
import com.glamvibe.glamvibeadmin.domain.model.Service

class ServicesAdapter(
    private val listener: ServicesListener,
) : ListAdapter<Service, ServiceViewHolder>(ServicesDiffCallback()) {

    interface ServicesListener {
        fun onEditClicked(service: Service)
        fun onDeleteClicked(service: Service)
        fun onServiceImageClicked(service: Service)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardServiceBinding.inflate(layoutInflater, parent, false)
        val viewHolder = ServiceViewHolder(binding)

        binding.serviceImage.setOnClickListener {
            listener.onServiceImageClicked(getItem(viewHolder.bindingAdapterPosition))
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

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}