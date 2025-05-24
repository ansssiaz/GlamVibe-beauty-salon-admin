package com.glamvibe.glamvibeadmin.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentMenuBinding
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.catalog_title))

        binding.mastersItem.setOnClickListener {
            requireParentFragment().requireParentFragment().findNavController()
                .navigate(
                    R.id.action_bottomMenuFragment_to_mastersCatalogFragment
                )
        }

        binding.servicesItem.setOnClickListener {
            requireParentFragment().requireParentFragment().findNavController()
                .navigate(
                    R.id.action_bottomMenuFragment_to_servicesCatalogFragment
                )
        }

        return binding.root
    }
}