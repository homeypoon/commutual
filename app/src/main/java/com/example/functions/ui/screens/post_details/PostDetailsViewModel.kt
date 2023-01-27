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

package com.example.functions.ui.screens.post_details

import androidx.compose.runtime.mutableStateOf
import com.example.functions.POST_DEFAULT_ID
import com.example.functions.POST_ID
import com.example.functions.PROFILE_POST_SCREEN
import com.example.functions.common.ext.idFromParameter
import com.example.functions.model.Post
import com.example.functions.model.User
import com.example.functions.model.service.AccountService
import com.example.functions.model.service.LogService
import com.example.functions.model.service.StorageService
import com.example.functions.ui.screens.FunctionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
  logService: LogService,
  private val storageService: StorageService,
  private val accountService: AccountService
) : FunctionsViewModel(logService) {

  val post = mutableStateOf(Post())
  val user = mutableStateOf(User())

  fun initialize(postId: String) {
    launchCatching {
      if (postId != POST_DEFAULT_ID) {
        post.value = storageService.getPost(postId.idFromParameter()) ?: Post()

        val postUser = storageService.getUser(post.value.userId)
        if (postUser != null) {
          user.value = postUser
        } else {
          // Handle the case where there is no user
        }
      }
    }
  }

  fun getUserInfo(postId:String) {

  }

  fun onPostClick(openScreen: (String) -> Unit, post: Post) {
    openScreen("$PROFILE_POST_SCREEN?$POST_ID={${post.postId}}")
  }

}
