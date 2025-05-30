package com.glamvibe.glamvibeadmin.presentation.adapter.masters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.CardMasterBinding
import com.glamvibe.glamvibeadmin.domain.model.Master

class MastersAdapter(
    private val listener: MastersListener
) : ListAdapter<Master, MastersViewHolder>(MastersDiffCallback()) {

    interface MastersListener {
        fun onEditClicked(master: Master)
        fun onDeleteClicked(master: Master)
        fun onMasterPhotoClicked(master: Master)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MastersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardMasterBinding.inflate(layoutInflater, parent, false)
        val viewHolder = MastersViewHolder(binding)

        binding.masterPhoto.setOnClickListener {
            listener.onMasterPhotoClicked(getItem(viewHolder.bindingAdapterPosition))
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

    override fun onBindViewHolder(holder: MastersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}