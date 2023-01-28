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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.ui.screens.item.PostItem

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@ExperimentalMaterialApi
fun ExploreScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight().verticalScroll(scrollState)) {
        BasicToolbar(title = R.string.explore)

        val posts = viewModel.posts.collectAsStateWithLifecycle(emptyList())
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .weight(1f, true)
        ) {
            items(posts.value, key = { it.postId }) { postItem ->
                Surface(modifier = Modifier.clickable {
                    viewModel.onPostClick(openScreen, postItem)
                }) {
                    PostItem(
                        post = postItem
                    )
                }
            }
        }


    }
}
