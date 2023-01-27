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

package com.example.functions.ui.screens.profile_post

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import com.example.functions.EDIT_POST_SCREEN
import com.example.functions.POST_DEFAULT_ID
import com.example.functions.POST_ID
import com.example.functions.common.ext.idFromParameter
import com.example.functions.model.Post
import com.example.functions.model.service.ConfigurationService
import com.example.functions.model.service.LogService
import com.example.functions.model.service.StorageService
import com.example.functions.ui.screens.FunctionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterialApi::class)
@HiltViewModel
class ProfilePostViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : FunctionsViewModel(logService) {

    val post = mutableStateOf(Post())

    fun initialize(postId: String) {
        launchCatching {
            if (postId != POST_DEFAULT_ID) {
                post.value = storageService.getPost(postId.idFromParameter()) ?: Post()
            }
        }
    }

    fun onIconClick(coroutineScope: CoroutineScope, bottomSheetState: ModalBottomSheetState) {
        coroutineScope.launch {
            if (!bottomSheetState.isVisible)
                bottomSheetState.show()
        }
    }

    fun onEditPostClick(
        openScreen: (String) -> Unit,
        post: Post,
        coroutineScope: CoroutineScope,
        bottomSheetState: ModalBottomSheetState
    ) {
        openScreen("$EDIT_POST_SCREEN?$POST_ID={${post.id}}")
        coroutineScope.launch {
            if (bottomSheetState.isVisible)
                bottomSheetState.hide()
        }
    }

    fun onDeletePostClick(
        popUpScreen: () -> Unit,
        post: Post,
        coroutineScope: CoroutineScope,
        bottomSheetState: ModalBottomSheetState
    ) {
        launchCatching { storageService.deletePost(post.id) }
        coroutineScope.launch {
            if (bottomSheetState.isVisible)
                bottomSheetState.hide()
        }
        popUpScreen()

    }


}
