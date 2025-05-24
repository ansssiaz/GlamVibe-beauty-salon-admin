package com.glamvibe.glamvibeadmin.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentRegistrationBinding
import com.glamvibe.glamvibeadmin.presentation.viewmodel.newAdministrator.NewAdministratorViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import com.glamvibe.glamvibeadmin.utils.getErrorText
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class RegistrationFragment : Fragment() {
    private val newAdministratorViewModel: NewAdministratorViewModel by viewModel<NewAdministratorViewModel>()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private lateinit var binding: FragmentRegistrationBinding

    private var lastname: String = ""
    private var name: String = ""
    private var patronymic: String? = null
    private var birthDate: String = ""
    private var email: String = ""
    private var phone: String = ""
    private var login: String = ""
    private var password: String = ""
    private var formData: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var snackbar: Snackbar? = null

        binding = FragmentRegistrationBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.registration_title))

        binding.registrationButton.alpha = 0.5f
        binding.registrationButton.isEnabled = false

        binding.lastnameTextEdit.addTextChangedListener(textWatcher)
        binding.nameTextEdit.addTextChangedListener(textWatcher)
        binding.birthDateTextEdit.addTextChangedListener(textWatcher)
        binding.emailTextEdit.addTextChangedListener(textWatcher)
        binding.phoneTextEdit.addTextChangedListener(textWatcher)
        binding.loginTextEdit.addTextChangedListener(textWatcher)
        binding.passwordTextEdit.addTextChangedListener(textWatcher)

        newAdministratorViewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it.administrator != null) {
                    findNavController().navigateUp()
                }

                if (it.isError) {
                    val errorText =
                        it.status.throwableOrNull?.getErrorText(requireContext()).toString()

                    if (snackbar == null) {
                        snackbar = Snackbar.make(
                            binding.root,
                            errorText,
                            Snackbar.LENGTH_INDEFINITE
                        ).apply {
                            setAction(getString(R.string.snackbar_action_text)) {
                                newAdministratorViewModel.register(
                                    lastname,
                                    name,
                                    patronymic,
                                    birthDate,
                                    email,
                                    phone,
                                    login,
                                    password,
                                    formData
                                )
                            }
                            setActionTextColor(getColor(requireContext(), R.color.mauve))
                        }
                    } else {
                        snackbar?.setText(errorText)
                    }
                    snackbar?.show()
                } else {
                    snackbar?.dismiss()
                    snackbar = null
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.registrationButton.setOnClickListener {
            binding.registrationButton.isEnabled = false

            lastname = binding.lastnameTextEdit.text.toString()
            name = binding.nameTextEdit.text.toString()
            patronymic = binding.patronymicTextEdit.text.toString().ifBlank { null }
            birthDate = binding.birthDateTextEdit.text.toString()
            email = binding.emailTextEdit.text.toString()
            phone = binding.phoneTextEdit.text.toString()
            login = binding.loginTextEdit.text.toString()
            password = binding.passwordTextEdit.text.toString()

            formData = createFormData()

            newAdministratorViewModel.register(
                lastname,
                name,
                patronymic,
                birthDate,
                email,
                phone,
                login,
                password,
                formData
            )
        }

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lastname = binding.lastnameTextEdit.text.toString()
            name = binding.nameTextEdit.text.toString()
            birthDate = binding.birthDateTextEdit.text.toString()
            email = binding.emailTextEdit.text.toString()
            phone = binding.phoneTextEdit.text.toString()
            login = binding.loginTextEdit.text.toString()
            password = binding.passwordTextEdit.text.toString()

            val isEnabled =
                lastname.isNotEmpty() && name.isNotEmpty() && birthDate.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty()

            binding.registrationButton.isEnabled = isEnabled
            binding.registrationButton.alpha = if (isEnabled) 1f else 0.5f
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun createFormData(): String? {
        val selectedSkinTypeId = binding.formSkinTypeRadioGroup.checkedRadioButtonId

        val skinType = if (selectedSkinTypeId != -1) {
            val selectedRadioButton =
                binding.formSkinTypeRadioGroup.findViewById<MaterialRadioButton>(selectedSkinTypeId)

            selectedRadioButton.text.toString()
                .lowercase(Locale.getDefault())
        } else {
            null
        }

        val hairType = mutableListOf<String>()
        if (binding.formHairTypeNormalCheckbox.isChecked) hairType.add(binding.formHairTypeNormalCheckbox.text.toString())
        if (binding.formHairTypeDryCheckbox.isChecked) hairType.add(binding.formHairTypeDryCheckbox.text.toString())
        if (binding.formHairTypeOilyCheckbox.isChecked) hairType.add(binding.formHairTypeOilyCheckbox.text.toString())
        if (binding.formHairTypeColoredCheckbox.isChecked) hairType.add(binding.formHairTypeColoredCheckbox.text.toString())
        if (binding.formHairTypeDamagedCheckbox.isChecked) hairType.add(binding.formHairTypeDamagedCheckbox.text.toString())

        val upcomingEvents = mutableListOf<String>()
        if (binding.formUpcomingEventsWeddingCheckbox.isChecked) upcomingEvents.add(binding.formUpcomingEventsWeddingCheckbox.text.toString())
        if (binding.formUpcomingEventsHolidayCheckbox.isChecked) upcomingEvents.add(binding.formUpcomingEventsHolidayCheckbox.text.toString())
        if (binding.formUpcomingEventsPhotoshootCheckbox.isChecked) upcomingEvents.add(binding.formUpcomingEventsPhotoshootCheckbox.text.toString())

        val stringBuilder = StringBuilder()

        if (skinType != null) {
            stringBuilder.append("$skinType кожа")
        }

        if (hairType.isNotEmpty()) {
            if (stringBuilder.isNotEmpty()) stringBuilder.append(", ")
            stringBuilder.append("${hairType.joinToString(", ") { it.lowercase(Locale.getDefault()) }} волосы")
        }

        if (upcomingEvents.isNotEmpty()) {
            if (stringBuilder.isNotEmpty()) stringBuilder.append(", ")
            stringBuilder.append(upcomingEvents.joinToString(", ") { it.lowercase(Locale.getDefault()) })
        }

        val formDataString = stringBuilder.toString().takeIf { it.isNotEmpty() }

        return formDataString
    }
}