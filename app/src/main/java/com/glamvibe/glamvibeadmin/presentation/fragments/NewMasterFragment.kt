package com.glamvibe.glamvibeadmin.presentation.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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
import com.glamvibe.glamvibeadmin.databinding.FragmentNewMasterBinding
import com.glamvibe.glamvibeadmin.domain.model.WorkingDay
import com.glamvibe.glamvibeadmin.domain.model.getStringByWeekDay
import com.glamvibe.glamvibeadmin.domain.model.getWeekDayByString
import com.glamvibe.glamvibeadmin.presentation.adapter.editSchedule.EditScheduleAdapter
import com.glamvibe.glamvibeadmin.presentation.viewmodel.newMaster.NewMasterViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import com.glamvibe.glamvibeadmin.utils.ALL_WEEK_DAYS
import com.glamvibe.glamvibeadmin.utils.dpToPx
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalTime
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class NewMasterFragment : Fragment() {
    companion object {
        const val ARG_ID = "ARG_ID"
        const val MASTER_CREATED_RESULT = "SERVICE_CREATED_RESULT"
    }

    private lateinit var binding: FragmentNewMasterBinding
    private var selectedImageUri: Uri? = null
    private var masterSchedule: List<WorkingDay>? = null
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private var id: Int = 0
    private val newMasterViewModel: NewMasterViewModel by viewModel<NewMasterViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewMasterBinding.inflate(inflater)

        id = arguments?.getInt(ARG_ID) ?: 0

        val title =
            if (id != 0) R.string.edit_master_title else R.string.new_master_title

        toolbarViewModel.setTitle(getString(title))

        if (id != 0) {
            newMasterViewModel.loadMasterToEdit(id)
            binding.saveButton.isEnabled = true
            binding.saveButton.alpha = 1f
        } else {
            binding.saveButton.isEnabled = false
            binding.saveButton.alpha = 0.5f
        }

        binding.masterImageIcon.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.lastnameTextEdit.addTextChangedListener(textWatcher)
        binding.nameTextEdit.addTextChangedListener(textWatcher)
        binding.birthDateTextEdit.addTextChangedListener(textWatcher)
        binding.emailTextEdit.addTextChangedListener(textWatcher)
        binding.phoneTextEdit.addTextChangedListener(textWatcher)
        binding.specialtyTextEdit.addTextChangedListener(textWatcher)
        binding.dateOfEmploymentTextEdit.addTextChangedListener(textWatcher)
        binding.workExperienceTextEdit.addTextChangedListener(textWatcher)

        val scheduleAdapter = EditScheduleAdapter()
        binding.schedule.layoutManager = LinearLayoutManager(requireContext())
        binding.schedule.adapter = scheduleAdapter

        binding.saveButton.setOnClickListener {
            val lastname = binding.lastnameTextEdit.text.toString()
            val name = binding.nameTextEdit.text.toString()
            val patronymic = binding.patronymicTextEdit.text.toString()
            val birthDate = binding.birthDateTextEdit.text.toString()
            val email = binding.emailTextEdit.text.toString()
            val phone = binding.phoneTextEdit.text.toString()
            val specialty = binding.specialtyTextEdit.text.toString()
            val dateOfEmployment = binding.dateOfEmploymentTextEdit.text.toString()
            val workExperience = binding.workExperienceTextEdit.text.toString()

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
        }

        newMasterViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                if (state.categories.isNotEmpty()) {
                    binding.categoriesCheckboxesContainer.removeAllViews()
                    for (category in state.categories) {
                        val checkBox = MaterialCheckBox(requireContext()).apply {
                            text = category
                            typeface = ResourcesCompat.getFont(requireContext(), R.font.raleway)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.brown))
                            textSize = 16f
                        }

                        checkBox.setOnCheckedChangeListener { _, _ ->
                            checkSaveButtonState()
                        }

                        binding.categoriesCheckboxesContainer.addView(checkBox)
                    }
                }

                if (state.masterToEdit != null) {
                    val radius =
                        requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius)
                    val widthPx = dpToPx(231, requireContext())
                    val heightPx = dpToPx(297, requireContext())

                    if (state.masterToEdit.photoUrl.isEmpty()) {
                        Glide.with(binding.root)
                            .load(R.drawable.empty_image)
                            .transform(RoundedCorners(radius))
                            .into(binding.masterPhoto)
                    } else {
                        Glide.with(binding.root)
                            .load(state.masterToEdit.photoUrl)
                            .override(widthPx, heightPx)
                            .error(R.drawable.empty_image)
                            .transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundedCorners(radius)
                                )
                            )
                            .into(binding.masterPhoto)
                    }

                    binding.lastnameTextEdit.setText(state.masterToEdit.lastname)
                    binding.nameTextEdit.setText(state.masterToEdit.name)
                    binding.patronymicTextEdit.setText(state.masterToEdit.patronymic)
                    binding.birthDateTextEdit.setText(state.masterToEdit.birthDate.toString())
                    binding.emailTextEdit.setText(state.masterToEdit.email)
                    binding.phoneTextEdit.setText(state.masterToEdit.phone)
                    binding.specialtyTextEdit.setText(state.masterToEdit.specialty)
                    binding.dateOfEmploymentTextEdit.setText(state.masterToEdit.dateOfEmployment.toString())
                    binding.workExperienceTextEdit.setText(state.masterToEdit.workExperience.toString())

                    for (i in 0 until binding.categoriesCheckboxesContainer.childCount) {
                        val checkBox =
                            binding.categoriesCheckboxesContainer.getChildAt(i) as MaterialCheckBox

                        checkBox.isChecked =
                            state.masterToEdit.categories.any { it == checkBox.text }
                    }

                    masterSchedule = state.masterToEdit.schedule
                }

                if (state.newMaster != null) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        MASTER_CREATED_RESULT,
                        bundleOf()
                    )
                    findNavController().navigateUp()
                }

                val displaySchedule = prepareScheduleForDisplay(masterSchedule)
                scheduleAdapter.submitList(displaySchedule)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            val radius = requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius)
            val widthPx = dpToPx(231, requireContext())
            val heightPx = dpToPx(297, requireContext())
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
                    .into(binding.masterPhoto)
                binding.masterImageIcon.alpha = 0.7f
                checkSaveButtonState()
            }
        }

    private fun checkSaveButtonState() {
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
    }

    private fun prepareScheduleForDisplay(masterSchedule: List<WorkingDay>?): List<WorkingDay> {
        return ALL_WEEK_DAYS.map { weekDay ->
            masterSchedule?.find { it.weekDay.getStringByWeekDay() == weekDay }
                ?: WorkingDay(weekDay.getWeekDayByString(), LocalTime(0, 0), LocalTime(0, 0))
        }
    }
}