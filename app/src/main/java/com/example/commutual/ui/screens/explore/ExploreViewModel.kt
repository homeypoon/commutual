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
import com.example.commutual.POST_DETAILS_SCREEN
import com.example.commutual.POST_ID
import com.example.commutual.model.Post
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
) : CommutualViewModel(logService) {

    var posts = storageService.posts
    var searchedPosts: Flow<List<Post>> = flow { listOf<Post>()}

    private var uiState = mutableStateOf(ExploreUiState())
        private set

     val searchText
        get() = uiState.value.searchText

    val postsSearched
        get() = uiState.value.postsSearched

    fun onSearchTextChange(searchText: String) {
        uiState.value = uiState.value.copy(searchText = searchText)

        Log.d("", postsSearched.toString())

        if (searchText.isEmpty()) {
            uiState.value = uiState.value.copy(postsSearched = false)
        }
    }

    fun onSearchClick() {
        uiState.value = uiState.value.copy(postsSearched = true)
        launchCatching {
            posts = storageService.searchedPosts(searchText)
        }
        Log.d("", postsSearched.toString())
    }


    fun onPostClick(openScreen: (String) -> Unit, post: Post) {
        openScreen("$POST_DETAILS_SCREEN?$POST_ID={${post.postId}}")
    }

}
