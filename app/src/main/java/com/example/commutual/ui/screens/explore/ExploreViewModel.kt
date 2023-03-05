/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.commutual.ui.screens.explore

import android.util.Log
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
        private set

     val searchText
        get() = uiState.value.searchText

    val postsSearched
        get() = uiState.value.postsSearched

    val showFiltersDialog
        get() = uiState.value.showFiltersDialog

    val selectedCategory
        get() = uiState.value.selectedCategory

    val filterChipCategory
        get() = uiState.value.filterChipCategory

    val expandedDropDownMenu
        get() = uiState.value.expandedDropDownMenu

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

        Log.d("", postsSearched.toString())

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
        Log.d("", postsSearched.toString())
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
