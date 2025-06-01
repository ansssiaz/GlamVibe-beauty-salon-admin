package com.glamvibe.glamvibeadmin.presentation.adapter.clients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.glamvibe.glamvibeadmin.databinding.CardClientBinding
import com.glamvibe.glamvibeadmin.domain.model.User

class ClientsAdapter(
    private val listener: ClientsListener
) : ListAdapter<User, ClientsViewHolder>(ClientsDiffCallback()) {

    interface ClientsListener {
        fun onClientClicked(client: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardClientBinding.inflate(layoutInflater, parent, false)
        val viewHolder = ClientsViewHolder(binding)

        binding.root.setOnClickListener {
            listener.onClientClicked(getItem(viewHolder.bindingAdapterPosition))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ClientsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}