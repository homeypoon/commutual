package com.example.commutual.ui.screens.explore

import com.example.commutual.model.CategoryEnum

data class ExploreUiState(
    val searchText: String = "",
    val postDescription: String = "",
    val postsSearched: Boolean = false,
    val showFiltersDialog: Boolean = false,
    val filterChipCategory: CategoryEnum = CategoryEnum.ANY,
    val selectedCategory: CategoryEnum = CategoryEnum.ANY,

    val expandedDropDownMenu: Boolean = false,
    val showProgressIndicator: Boolean = false

)

