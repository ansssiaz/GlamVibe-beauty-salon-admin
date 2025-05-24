package com.glamvibe.glamvibeadmin.presentation.adapter.masters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.databinding.CardMasterBinding
import com.glamvibe.glamvibeadmin.domain.model.Master

class MastersAdapter: ListAdapter<Master, MastersViewHolder>(MastersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MastersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardMasterBinding.inflate(layoutInflater, parent, false)
        val viewHolder = MastersViewHolder(binding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: MastersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}