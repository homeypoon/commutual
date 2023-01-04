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

package com.example.functions.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.functions.common.composable.ActionToolbar
import com.example.functions.common.ext.smallSpacer
import com.example.functions.common.ext.toolbarActions
import com.example.functions.screens.profile_post.ProfilePostViewModel
import kotlinx.coroutines.launch
import com.example.functions.R.drawable as AppIcon
import com.example.functions.R.string as AppText

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@ExperimentalMaterialApi
fun ProfilePostScreen(
  openScreen: (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: ProfilePostViewModel = hiltViewModel()
) {
    val posts = viewModel.tasks.collectAsStateWithLifecycle(emptyList())
    val options by viewModel.options
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,)
    val coroutineScope = rememberCoroutineScope()

  ModalBottomSheetLayout(
    sheetContent = {

      LazyColumn {

        items(count = 5) {

          ListItem(
            modifier = Modifier.clickable {


              coroutineScope.launch {
                bottomSheetState.hide()
              }
            },
            text = {
              Text(text = "Item $it")
            },
            icon = {
              Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite"
              )
            }
          )
        }
      }
    },
    sheetState = bottomSheetState
  ) {
    // app UI

    Column(modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()) {
      ActionToolbar(
        title = AppText.profile,
        modifier = Modifier.toolbarActions(),
        endActionIcon = AppIcon.ic_settings,
        endAction = { viewModel.onSettingsClick(openScreen) }
      )

      Spacer(modifier = Modifier.smallSpacer())

//      LazyColumn {
//        items(posts.value, key = { it.id }) { postItem ->
//          EditPostItem(
//            post = postItem,
//            options = options,
//            onActionClick = { action -> viewModel.onTaskActionClick(openScreen, postItem, action) }
//          )
//        }
//      }


    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Button(onClick = {
        coroutineScope.launch {
          bottomSheetState.show()
        }
      }) {
        Text(text = "Show Bottom Sheet")
      }
    }
  }



    }



//  Icon(
//    modifier = Modifier.padding(8.dp, 0.dp),
//    imageVector = Icons.Default.MoreVert,
//    contentDescription = "More"
//  )
  LaunchedEffect(viewModel) { viewModel.loadTaskOptions() }
}
