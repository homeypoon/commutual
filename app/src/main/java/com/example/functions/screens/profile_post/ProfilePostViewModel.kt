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

package com.example.functions.screens.profile_post

import androidx.compose.runtime.mutableStateOf
import com.example.functions.EDIT_POST_SCREEN
import com.example.functions.POST_ID
import com.example.functions.SETTINGS_SCREEN
import com.example.functions.model.Post
import com.example.functions.model.service.ConfigurationService
import com.example.functions.model.service.LogService
import com.example.functions.model.service.StorageService
import com.example.functions.screens.FunctionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfilePostViewModel @Inject constructor(
  logService: LogService,
  private val storageService: StorageService,
  private val configurationService: ConfigurationService
) : FunctionsViewModel(logService) {

  val options = mutableStateOf<List<String>>(listOf())

  val tasks = storageService.posts

  fun loadTaskOptions() {
    val hasEditOption = configurationService.isShowPostEditButtonConfig
    options.value = EditPostActionOption.getOptions(hasEditOption)
  }

//  fun onAddClick(openScreen: (String) -> Unit) = openScreen(EDIT_TASK_SCREEN)

  fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

  fun onTaskActionClick(openScreen: (String) -> Unit, post: Post, action: String) {
    when (EditPostActionOption.getByTitle(action)) {
      EditPostActionOption.EditPost -> openScreen("$EDIT_POST_SCREEN?$POST_ID={${post.id}}")
      EditPostActionOption.DeletePost -> onDeleteTaskClick(post)
    }
  }

  private fun onDeleteTaskClick(post: Post) {
    launchCatching { storageService.delete(post.id) }
  }
}
