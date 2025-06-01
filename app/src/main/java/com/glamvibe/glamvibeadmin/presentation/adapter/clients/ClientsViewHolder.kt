package com.glamvibe.glamvibeadmin.presentation.adapter.clients

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.glamvibe.glamvibeadmin.databinding.CardClientBinding
import com.glamvibe.glamvibeadmin.domain.model.User
import com.glamvibe.glamvibeadmin.utils.formatDate

class ClientsViewHolder(private val binding: CardClientBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(client: User) {
        binding.clientInitials.text = buildString {
            client.lastname.firstOrNull()?.let { letter -> append(letter) }
            append(" ")
            client.name.firstOrNull()?.let { letter -> append(letter) }
        }.uppercase()

        binding.fullName.text =
            if (client.patronymic != null) "${client.lastname} ${client.name} ${client.patronymic}"
            else "${client.lastname} ${client.name}"

        binding.birthDate.text = formatDate(client.birthDate)
        binding.phone.text = client.phone
        binding.email.text = client.email
    }
}