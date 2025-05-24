package com.glamvibe.glamvibeadmin.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentBottomMenuBinding

class BottomMenuFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBottomMenuBinding.inflate(inflater)
        val appointmentsClickListener = View.OnClickListener {
            findNavController().navigate(R.id.action_bottomMenuFragment_to_newAppointmentFragment)
        }

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.appointmentsFragment -> {
                    binding.addButton.setOnClickListener(appointmentsClickListener)
                    binding.addButton.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.menuFragment -> {
                    binding.addButton.setOnClickListener(null)
                    binding.addButton.animate()
                        .scaleX(0F)
                        .scaleY(0F)
                }

                R.id.profileFragment -> {
                    binding.addButton.setOnClickListener(null)
                    binding.addButton.animate()
                        .scaleX(0F)
                        .scaleY(0F)
                }
            }
        }
        binding.bottomMenu.setupWithNavController(navController)
        return binding.root
    }
}