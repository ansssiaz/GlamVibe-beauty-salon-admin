package com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ToolbarViewModel() : ViewModel() {
    private val _showAdd = MutableStateFlow(false)
    val showAdd = _showAdd.asStateFlow()

    private val _addClicked = MutableStateFlow(false)
    val addClicked = _addClicked.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    fun setAddVisibility(visible: Boolean) {
        _showAdd.value = visible
    }

    fun addClicked(pending: Boolean) {
        _addClicked.value = pending
    }

    fun setTitle(newTitle: String) {
        _title.value = newTitle
    }
}