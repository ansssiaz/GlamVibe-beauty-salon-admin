package com.glamvibe.glamvibeadmin.presentation.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentNewPromotionBinding
import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.presentation.adapter.editPromotionServices.EditPromotionServicesAdapter
import com.glamvibe.glamvibeadmin.presentation.viewmodel.newPromotion.NewPromotionViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import com.glamvibe.glamvibeadmin.utils.dpToPx
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPromotionFragment : Fragment() {
    companion object {
        const val ARG_ID = "ARG_ID"
        const val PROMOTION_CREATED_RESULT = "PROMOTION_CREATED_RESULT"
    }

    private lateinit var binding: FragmentNewPromotionBinding
    private var selectedImageUri: Uri? = null
    private var promotionServices: List<Service>? = null
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private var id: Int = 0
    private val newPromotionViewModel: NewPromotionViewModel by viewModel<NewPromotionViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPromotionBinding.inflate(inflater)

        id = arguments?.getInt(NewMasterFragment.ARG_ID) ?: 0

        val title =
            if (id != 0) R.string.edit_promotion_title else R.string.new_promotion_title

        toolbarViewModel.setTitle(getString(title))

        if (id != 0) {
            newPromotionViewModel.loadPromotionToEdit(id)
            binding.saveButton.isEnabled = true
            binding.saveButton.alpha = 1f
        } else {
            binding.saveButton.isEnabled = false
            binding.saveButton.alpha = 0.5f
        }

        binding.promotionImageIcon.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

/*        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.nameTextEdit.addTextChangedListener(textWatcher)
        binding.startDateTextEdit.addTextChangedListener(textWatcher)
        binding.endDateTextEdit.addTextChangedListener(textWatcher)
        binding.descriptionTextEdit.addTextChangedListener(textWatcher)
 */

        val editPromotionServicesAdapter = EditPromotionServicesAdapter()
        binding.listOfPromotionServices.layoutManager = LinearLayoutManager(requireContext())
        binding.listOfPromotionServices.adapter = editPromotionServicesAdapter

        /*        binding.saveButton.setOnClickListener {
                    val name = binding.nameTextEdit.text.toString()


                    val schedule = mutableListOf<WorkingDay>()
                    for (i in 0 until binding.schedule.childCount) {
                        val itemView = binding.schedule.getChildAt(i)

                        val dayOfWeek = itemView.findViewById<TextView>(R.id.week_day).text as String
                        val startText = itemView.findViewById<AppCompatEditText>(R.id.start_time).text.toString()
                        val endText = itemView.findViewById<AppCompatEditText>(R.id.end_time).text.toString()

                        if (startText.isNotBlank() && endText.isNotBlank()) {
                            val start = runCatching { LocalTime.parse(startText) }.getOrNull()
                            val end = runCatching { LocalTime.parse(endText) }.getOrNull()
                            if (start != null && end != null) {
                                schedule.add(WorkingDay(dayOfWeek.lowercase()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                                    .getWeekDayByString(), start, end))
                            }
                        }
                    }

                    val selectedCategories = mutableListOf<String>()
                    for (i in 0 until binding.categoriesCheckboxesContainer.childCount) {
                        val checkBox =
                            binding.categoriesCheckboxesContainer.getChildAt(i) as MaterialCheckBox
                        if (checkBox.isChecked) {
                            val category = checkBox.text.toString()
                            selectedCategories.add(category)
                        }
                    }

                    val photoBytes: ByteArray? = selectedImageUri?.let { uri ->
                        requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                            inputStream.readBytes()
                        }
                    }

                    val mimeType = selectedImageUri?.let { uri ->
                        requireContext().contentResolver.getType(uri)
                    }

                    val photoExtension = if (photoBytes != null) {
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
                        if (photoBytes != null && photoExtension != null) {
                            newMasterViewModel.addMaster(
                                lastname,
                                name,
                                patronymic,
                                birthDate,
                                phone,
                                email,
                                specialty,
                                selectedCategories.toList(),
                                schedule.toList(),
                                dateOfEmployment,
                                workExperience,
                                photoBytes,
                                photoExtension
                            )
                        }
                    } else {
                        newMasterViewModel.editMaster(
                            id,
                            lastname,
                            name,
                            patronymic,
                            birthDate,
                            phone,
                            email,
                            specialty,
                            selectedCategories.toList(),
                            schedule.toList(),
                            dateOfEmployment,
                            workExperience,
                            photoBytes,
                            photoExtension
                        )
                    }
                }*/

        newPromotionViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                if (state.promotionToEdit != null) {
                    val radius =
                        requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius)
                    val widthPx = dpToPx(330, requireContext())
                    val heightPx = dpToPx(220, requireContext())

                    if (state.promotionToEdit.imageUrl.isEmpty()) {
                        Glide.with(binding.root)
                            .load(R.drawable.empty_image)
                            .transform(RoundedCorners(radius))
                            .into(binding.promotionImage)
                    } else {
                        Glide.with(binding.root)
                            .load(state.promotionToEdit.imageUrl)
                            .override(widthPx, heightPx)
                            .error(R.drawable.empty_image)
                            .transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundedCorners(radius)
                                )
                            )
                            .into(binding.promotionImage)
                    }

                    binding.nameTextEdit.setText(state.promotionToEdit.name)
                    binding.startDateTextEdit.setText(state.promotionToEdit.startDate.toString())
                    binding.endDateTextEdit.setText(state.promotionToEdit.endDate.toString())
                    binding.descriptionTextEdit.setText(state.promotionToEdit.description)

                    promotionServices = state.promotionToEdit.services
                }

                if (state.newPromotion != null) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        PROMOTION_CREATED_RESULT,
                        bundleOf()
                    )
                    findNavController().navigateUp()
                }

                val displayPromotionServices = prepareServicesForDisplay(promotionServices)
                editPromotionServicesAdapter.submitList(displayPromotionServices)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            val radius = requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius)
            val widthPx = dpToPx(330, requireContext())
            val heightPx = dpToPx(220, requireContext())
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
                    .into(binding.promotionImage)
                binding.promotionImageIcon.alpha = 0.7f
                //checkSaveButtonState()
            }
        }

    /*private fun checkSaveButtonState() {
        val lastname = binding.lastnameTextEdit.text.toString()
        val name = binding.nameTextEdit.text.toString()
        val birthDate = binding.birthDateTextEdit.text.toString()
        val email = binding.emailTextEdit.text.toString()
        val phone = binding.phoneTextEdit.text.toString()
        val specialty = binding.specialtyTextEdit.text.toString()
        val dateOfEmployment = binding.dateOfEmploymentTextEdit.text.toString()
        val workExperience = binding.workExperienceTextEdit.text.toString()
        val categorySelected = (0 until binding.categoriesCheckboxesContainer.childCount)
            .map { binding.categoriesCheckboxesContainer.getChildAt(it) as MaterialCheckBox }
            .any { it.isChecked }

        val imageSelected = (selectedImageUri != null) || id != 0

        val isEnabled = lastname.isNotBlank() &&
                name.isNotBlank() &&
                birthDate.isNotBlank() &&
                email.isNotBlank() &&
                phone.isNotBlank() &&
                specialty.isNotBlank() &&
                dateOfEmployment.isNotBlank() &&
                workExperience.isNotBlank() &&
                categorySelected &&
                imageSelected

        binding.saveButton.isEnabled = isEnabled
        binding.saveButton.alpha = if (isEnabled) 1f else 0.5f
    }*/

    private fun prepareServicesForDisplay(promotionServices: List<Service>?): List<Service> {
        return promotionServices?.take(3) ?: listOf(Service(), Service(), Service())
    }
}