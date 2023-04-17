

package com.example.commutual.ui.screens.explore

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusManager
import com.example.commutual.POST_DETAILS_SCREEN
import com.example.commutual.POST_ID
import com.example.commutual.model.CategoryEnum
import com.example.commutual.model.Post
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
) : CommutualViewModel(logService) {

    var posts = storageService.posts

    private var uiState = mutableStateOf(ExploreUiState())

    val searchText
        get() = uiState.value.searchText

    private val postsSearched
        get() = uiState.value.postsSearched

    val showFiltersDialog
        get() = uiState.value.showFiltersDialog

    val selectedCategory
        get() = uiState.value.selectedCategory

    val filterChipCategory
        get() = uiState.value.filterChipCategory

    val showProgressIndicator
        get() = uiState.value.showProgressIndicator

    fun resetFilters() {
        uiState.value = uiState.value.copy(selectedCategory = CategoryEnum.ANY)
    }

    fun onCategorySelected(category: CategoryEnum) {
        uiState.value = uiState.value.copy(selectedCategory = category)

    }

    fun setShowFiltersDialog(showFiltersDialog: Boolean) {
        uiState.value = uiState.value.copy(showFiltersDialog = showFiltersDialog)
    }

    fun onSearchTextChange(searchText: String) {
        uiState.value = uiState.value.copy(searchText = searchText)

        if (searchText.isEmpty()) {
            uiState.value = uiState.value.copy(postsSearched = false)
        }
    }


    fun onSearchClick(focusManager: FocusManager) {
        uiState.value = uiState.value.copy(
            postsSearched = true,
            showProgressIndicator = true
        )

        if (filterChipCategory == CategoryEnum.ANY) {
            launchCatching {
                posts = storageService.searchedPosts(searchText)
                showProgressIndicator(false)
            }
        } else {
            launchCatching {
                posts = storageService.filteredPosts(searchText, filterChipCategory)
                showProgressIndicator(false)
            }
        }

        clearFocus(focusManager)
    }

    fun onFilterClick(focusManager: FocusManager) {
        clearFocus(focusManager)
        uiState.value = uiState.value.copy(showFiltersDialog = true)
    }

    fun onCloseFilterChipClick(focusManager: FocusManager) {
        uiState.value = uiState.value.copy(
            selectedCategory = CategoryEnum.ANY,
            filterChipCategory = CategoryEnum.ANY
        )
        onSearchClick(focusManager)
    }

    fun onApplyFilterClick(focusManager: FocusManager) {
        setShowFiltersDialog(false)
        uiState.value = uiState.value.copy(
            showFiltersDialog = false,
            filterChipCategory = selectedCategory
        )
        onSearchClick(focusManager)
    }

    fun onPostClick(openScreen: (String) -> Unit, post: Post) {
        openScreen("$POST_DETAILS_SCREEN?$POST_ID={${post.postId}}")
    }

    fun clearFocus(focusManager: FocusManager) {
        focusManager.clearFocus()
    }

    fun showProgressIndicator(showProgressIndicator: Boolean) {
        uiState.value = uiState.value.copy(
            showProgressIndicator = showProgressIndicator
        )
    }

}
