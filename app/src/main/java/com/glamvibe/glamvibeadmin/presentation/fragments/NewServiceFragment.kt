package com.glamvibe.glamvibeadmin.presentation.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentNewServiceBinding
import com.glamvibe.glamvibeadmin.presentation.viewmodel.newService.NewServiceViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import com.glamvibe.glamvibeadmin.utils.dpToPx
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewServiceFragment : Fragment() {
    companion object {
        const val ARG_ID = "ARG_ID"
        const val SERVICE_CREATED_RESULT = "SERVICE_CREATED_RESULT"
    }

    private lateinit var binding: FragmentNewServiceBinding
    private lateinit var categoriesAdapter: ArrayAdapter<String>
    private var currentCategories: List<String> = emptyList()
    private var selectedImageUri: Uri? = null
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private var id: Int = 0
    private val newServiceViewModel: NewServiceViewModel by viewModel<NewServiceViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewServiceBinding.inflate(inflater)

        id = arguments?.getInt(ARG_ID) ?: 0

        val title =
            if (id != 0) R.string.edit_service_title else R.string.new_service_title

        toolbarViewModel.setTitle(getString(title))

        if (id != 0) {
            newServiceViewModel.loadServiceToEdit(id)
            binding.saveButton.isEnabled = true
            binding.saveButton.alpha = 1f
        } else {
            binding.saveButton.isEnabled = false
            binding.saveButton.alpha = 0.5f
        }

        binding.serviceImageIcon.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.serviceNameTextEdit.addTextChangedListener(textWatcher)
        binding.servicePriceTextEdit.addTextChangedListener(textWatcher)
        binding.serviceDurationTextEdit.addTextChangedListener(textWatcher)
        binding.serviceDescriptionTextEdit.addTextChangedListener(textWatcher)

        categoriesAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            mutableListOf<String>()
        ).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        binding.categorySpinner.adapter = categoriesAdapter

        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    checkSaveButtonState()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.saveButton.setOnClickListener {
            val name = binding.serviceNameTextEdit.text.toString()
            val category = binding.categorySpinner.selectedItem.toString()
            val description = binding.serviceDescriptionTextEdit.text.toString()
            val duration = binding.serviceDurationTextEdit.text.toString()
            val price = binding.servicePriceTextEdit.text.toString()

            val imageBytes: ByteArray? = selectedImageUri?.let { uri ->
                requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.readBytes()
                }
            }

            val mimeType = selectedImageUri?.let { uri ->
                requireContext().contentResolver.getType(uri)
            }

            val imageExtension = if (imageBytes != null) {
                when (mimeType) {
                    "image/jpeg" -> "jpg"
                    "image/png" -> "png"
                    "image/gif" -> "gif"
                    "image/webp" -> "webp"
                    else -> "jpg"
                }
            } else {
                null
            }

            if (id == 0) {
                if (imageBytes != null && imageExtension != null) {
                    newServiceViewModel.addService(
                        name, category, description, duration, price,
                        imageBytes, imageExtension
                    )
                }
            } else {
                newServiceViewModel.editService(
                    id,
                    name, category, description, duration, price,
                    imageBytes, imageExtension
                )
            }
        }

        newServiceViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                val newCategories = listOf("Выберите категорию") + state.categories
                currentCategories = newCategories

                categoriesAdapter.clear()
                categoriesAdapter.addAll(newCategories)
                categoriesAdapter.notifyDataSetChanged()

                val radius = requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius)
                val widthPx = dpToPx(250, requireContext())
                val heightPx = dpToPx(250, requireContext())

                if (state.serviceToEdit != null) {
                    if (state.serviceToEdit.imageUrl.isEmpty()) {
                        Glide.with(binding.root)
                            .load(R.drawable.empty_image)
                            .transform(RoundedCorners(radius))
                            .into(binding.serviceImage)
                    } else {
                        Glide.with(binding.root)
                            .load(state.serviceToEdit.imageUrl)
                            .override(widthPx, heightPx)
                            .error(R.drawable.empty_image)
                            .transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundedCorners(radius)
                                )
                            )
                            .into(binding.serviceImage)
                    }

                    binding.serviceNameTextEdit.setText(state.serviceToEdit.name)

                    val position = newCategories.indexOf(state.serviceToEdit.category)

                    if (position >= 0) {
                        binding.categorySpinner.setSelection(position)
                    }

                    binding.servicePriceTextEdit.setText(state.serviceToEdit.price.toString())
                    binding.serviceDurationTextEdit.setText(state.serviceToEdit.duration.toString())
                    binding.serviceDescriptionTextEdit.setText(state.serviceToEdit.description)
                }

                if (state.newService != null) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        SERVICE_CREATED_RESULT,
                        bundleOf()
                    )
                    findNavController().navigateUp()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            val radius = requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius)
            val widthPx = dpToPx(250, requireContext())
            val heightPx = dpToPx(250, requireContext())
            if (uri != null) {
                selectedImageUri = uri
                Glide.with(binding.root)
                    .load(uri)
                    .override(widthPx, heightPx)
                    .error(R.drawable.empty_image)
                    .transform(
                        MultiTransformation(
                            CenterCrop(),
                            RoundedCorners(radius)
                        )
                    )
                    .into(binding.serviceImage)
                binding.serviceImageIcon.alpha = 0.7f
                checkSaveButtonState()
            }
        }

    private fun checkSaveButtonState() {
        val name = binding.serviceNameTextEdit.text.toString()
        val price = binding.servicePriceTextEdit.text.toString()
        val duration = binding.serviceDurationTextEdit.text.toString()
        val description = binding.serviceDescriptionTextEdit.text.toString()
        val categorySelected = binding.categorySpinner.selectedItemPosition != 0
        val imageSelected = (selectedImageUri != null) || id != 0

        val isEnabled = name.isNotBlank() &&
                price.isNotBlank() &&
                duration.isNotBlank() &&
                description.isNotBlank() &&
                categorySelected &&
                imageSelected

        binding.saveButton.isEnabled = isEnabled
        binding.saveButton.alpha = if (isEnabled) 1f else 0.5f
    }
}