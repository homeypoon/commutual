package com.example.commutual.ui.screens.edit_task

import com.example.commutual.model.Task

data class EditTaskUiState(
    val task: Task = Task(),
    val startTime: String = "",
    val endTime: String = "",
    val expandedDropDownMenu: Boolean = false,
    val completed: Boolean = false
)

