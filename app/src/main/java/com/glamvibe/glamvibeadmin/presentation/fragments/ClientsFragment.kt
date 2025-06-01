package com.glamvibe.glamvibeadmin.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.glamvibe.glamvibeadmin.R
import com.glamvibe.glamvibeadmin.databinding.FragmentClientsBinding
import com.glamvibe.glamvibeadmin.domain.model.User
import com.glamvibe.glamvibeadmin.presentation.adapter.clients.ClientsAdapter
import com.glamvibe.glamvibeadmin.presentation.viewmodel.clients.ClientsViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClientsFragment : Fragment() {
    private lateinit var binding: FragmentClientsBinding
    private val toolbarViewModel: ToolbarViewModel by activityViewModels<ToolbarViewModel>()
    private val clientsViewModel: ClientsViewModel by viewModel<ClientsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClientsBinding.inflate(inflater)

        toolbarViewModel.setTitle(getString(R.string.clients_title))

        binding.searchTextEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.searchTextEditLayout.setEndIconOnClickListener(null)
                } else {
                    binding.searchTextEditLayout.setEndIconOnClickListener {
                        binding.searchTextEdit.setText("")
                        clientsViewModel.resetSearch()
                        binding.searchTextEdit.clearFocus()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.searchTextEdit.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchTextEdit.text.toString()
                if (query.isNotBlank()) {
                    clientsViewModel.searchClient(query)
                    val inputMethodManager =
                        v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)

                    binding.searchTextEdit.clearFocus()
                }
                true
            } else {
                false
            }
        }

        binding.searchTextEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (binding.searchTextEdit.text.isNullOrEmpty()) {
                    binding.searchTextEdit.hint = getString(R.string.hint_search_clients)
                    binding.searchTextEditLayout.isHintEnabled = false
                    binding.searchTextEditLayout.isHintEnabled = true
                }
            }
        }


        val clientsAdapter = ClientsAdapter(
            object : ClientsAdapter.ClientsListener {
                override fun onClientClicked(client: User) {
                    findNavController().navigate(
                        R.id.action_clientsFragment_to_clientAppointmentsFragment,
                        bundleOf(ClientAppointmentsFragment.ARG_ID to client.id)
                    )
                }
            }
        )

        binding.listOfClients.layoutManager = LinearLayoutManager(requireContext())
        binding.listOfClients.adapter = clientsAdapter

        clientsViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                clientsAdapter.submitList(state.foundClients)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}