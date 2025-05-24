package com.glamvibe.glamvibeadmin.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.core.content.ContextCompat.getColor
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentAuthorizationBinding
import com.glamvibe.glamvibeadmin.presentation.viewmodel.administrator.AdministratorViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import com.glamvibe.glamvibeadmin.utils.getErrorText
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AuthorizationFragment : Fragment() {
    private val administratorViewModel: AdministratorViewModel by activityViewModel<AdministratorViewModel>()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private lateinit var binding: FragmentAuthorizationBinding
    private var loginText: String = ""
    private var passwordText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var snackbar: Snackbar? = null

        binding = FragmentAuthorizationBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.app_name))

        binding.signInButton.alpha = 0.5f
        binding.signInButton.isEnabled = false

        binding.loginTextEdit.addTextChangedListener(textWatcher)
        binding.passwordTextEdit.addTextChangedListener(textWatcher)

        binding.signInButton.setOnClickListener {
            administratorViewModel.logIn(loginText, passwordText)
            binding.signInButton.isEnabled = false
            binding.signUpButton.isEnabled = false
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_registrationFragment)
        }

        administratorViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it.administrator != null && isAdded) {
                    findNavController().navigate(R.id.action_authorizationFragment_to_bottomMenuFragment)
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
                                administratorViewModel.logIn(loginText, passwordText)
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

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            loginText = binding.loginTextEdit.text.toString()
            passwordText = binding.passwordTextEdit.text.toString()

            val isEnabled = loginText.isNotEmpty() && passwordText.isNotEmpty()
            binding.signInButton.isEnabled = isEnabled
            binding.signInButton.alpha = if (isEnabled) 1f else 0.5f
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}