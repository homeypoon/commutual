package com.example.commutual.ui.screens.edit_post

import com.example.commutual.model.Category

data class EditPostUiState(
    val postTitle: String = "",
    val postDescription: String = "",
    val category: Category? = null,
    val expandedDropDownMenu: Boolean = false
)

