package com.example.commutual.ui.screens.edit_post

import com.example.commutual.model.CategoryEnum

data class EditPostUiState(
    val postTitle: String = "",
    val postDescription: String = "",
    val category: CategoryEnum = CategoryEnum.ANY,
    val expandedDropDownMenu: Boolean = false
)

