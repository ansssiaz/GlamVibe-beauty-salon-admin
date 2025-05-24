package com.glamvibe.glamvibeadmin.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentProfileDataBinding
import com.glamvibe.glamvibeadmin.presentation.viewmodel.administrator.AdministratorViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileDataFragment : Fragment() {
    private val administratorViewModel: AdministratorViewModel by activityViewModel<AdministratorViewModel>()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private lateinit var binding: FragmentProfileDataBinding

    private var lastname: String = ""
    private var name: String = ""
    private var patronymic: String = ""
    private var birthDate: String = ""
    private var email: String = ""
    private var phone: String = ""
    private var login: String = ""
    private var password: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileDataBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.profile_data_title))

        binding.saveChangesProfileDataButton.alpha = 0.5f
        binding.saveChangesProfileDataButton.isEnabled = false

        binding.lastnameTextEdit.addTextChangedListener(textWatcher)
        binding.nameTextEdit.addTextChangedListener(textWatcher)
        binding.birthDateTextEdit.addTextChangedListener(textWatcher)
        binding.emailTextEdit.addTextChangedListener(textWatcher)
        binding.phoneTextEdit.addTextChangedListener(textWatcher)

        administratorViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                it.administrator?.let { client ->
                    binding.lastnameTextEdit.setText(client.lastname)
                    binding.nameTextEdit.setText(client.name)
                    binding.patronymicTextEdit.setText(client.patronymic)
                    binding.birthDateTextEdit.setText(client.birthDate.toString())
                    binding.emailTextEdit.setText(client.email)
                    binding.phoneTextEdit.setText(client.phone)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.editProfileDataButton.setOnClickListener {
            binding.editProfileDataButton.isEnabled = false

            binding.lastnameTextEdit.isEnabled = true
            binding.nameTextEdit.isEnabled = true
            binding.patronymicTextEdit.isEnabled = true
            binding.birthDateTextEdit.isEnabled = true
            binding.emailTextEdit.isEnabled = true
            binding.phoneTextEdit.isEnabled = true

            binding.profileCredentialsTextview.isVisible = true
            binding.changeProfileCredentialsTextview.isVisible = true
            binding.loginTextEditLayout.isVisible = true
            binding.passwordTextEditLayout.isVisible = true
        }

        binding.saveChangesProfileDataButton.setOnClickListener {
            binding.saveChangesProfileDataButton.isEnabled = false
            binding.editProfileDataButton.isEnabled = false

            administratorViewModel.state.value.administrator?.id?.let { id ->
                lastname = binding.lastnameTextEdit.text.toString()
                name = binding.nameTextEdit.text.toString()
                patronymic = binding.patronymicTextEdit.text.toString()
                birthDate = binding.birthDateTextEdit.text.toString()
                email = binding.emailTextEdit.text.toString()
                phone = binding.phoneTextEdit.text.toString()
                login = binding.loginTextEdit.text.toString()
                password = binding.passwordTextEdit.text.toString()

                administratorViewModel.updateProfileInformation(
                    id,
                    lastname,
                    name,
                    patronymic,
                    birthDate,
                    email,
                    phone,
                    login,
                    password
                )

                findNavController().navigateUp()
            }
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

            val isEnabled =
                lastname.isNotEmpty() && name.isNotEmpty() && birthDate.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()
            binding.saveChangesProfileDataButton.isEnabled = isEnabled
            binding.saveChangesProfileDataButton.alpha = if (isEnabled) 1f else 0.5f
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}