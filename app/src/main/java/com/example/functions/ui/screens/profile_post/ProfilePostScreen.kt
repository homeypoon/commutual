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


@file:OptIn(ExperimentalLifecycleComposeApi::class)

package com.example.functions.ui.screens.profile_post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.example.functions.common.composable.BasicIconButton
import com.example.functions.common.composable.BasicToolbar
import com.example.functions.common.ext.basicIconButton
import com.example.functions.ui.screens.item.BottomSheetOptionItem
import com.example.functions.R.string as AppText

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@ExperimentalMaterialApi
fun ProfilePostScreen(
    popUpScreen: () -> Unit,
    openScreen: (String) -> Unit,
    postId: String,
    modifier: Modifier = Modifier,
    viewModel: ProfilePostViewModel = hiltViewModel()
) {

    val post by viewModel.post
//    val posts = viewModel.posts.collectAsStateWithLifecycle(emptyList())
//    val user by posts

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    val bottomSheetOptions = listOf(
        PostBottomSheetOption.Edit,
        PostBottomSheetOption.Delete
    )

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            LazyColumn {
                items(bottomSheetOptions) {
                    Surface(modifier = Modifier.clickable {
                        when (it) {
                            PostBottomSheetOption.Edit -> viewModel.onEditPostClick(
                                openScreen, post, coroutineScope, bottomSheetState
                            )
                            PostBottomSheetOption.Delete -> viewModel.onDeletePostClick(
                                popUpScreen, post, coroutineScope, bottomSheetState
                            )
                        }
                    }) {
                        BottomSheetOptionItem(it)
                    }
                }
            }
        }
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            BasicToolbar(
                title = AppText.my_post
            )

            BasicIconButton(
                imageVector = Icons.Default.MoreVert,
                modifier = Modifier.basicIconButton(),
                onIconClick = { viewModel.onIconClick(coroutineScope, bottomSheetState) },
                contentDescription = AppText.more
            )

            Text(text = post.title)
            Text(text = post.description)
        }

    }


    LaunchedEffect(Unit) { viewModel.initialize(postId) }

}
