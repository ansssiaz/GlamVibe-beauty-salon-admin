package com.glamvibe.glamvibeadmin.presentation.viewmodel.toolbar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ToolbarViewModel() : ViewModel() {
    private val _title = MutableStateFlow("")

    val title = _title.asStateFlow()

    fun setTitle(newTitle: String) {
        _title.value = newTitle
    }
}