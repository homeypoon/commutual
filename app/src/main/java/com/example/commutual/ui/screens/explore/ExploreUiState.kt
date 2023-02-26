package com.example.commutual.ui.screens.explore

import com.example.commutual.model.CategoryEnum

data class ExploreUiState(
    val searchText: String = "",
    val postDescription: String = "",
    val postsSearched: Boolean = false,


    val category: CategoryEnum = CategoryEnum.DEFAULT,
    val expandedDropDownMenu: Boolean = false
)

