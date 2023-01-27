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


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.example.functions.R
import com.example.functions.common.composable.BasicToolbar
import com.example.functions.ui.screens.post_details.PostDetailsViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@ExperimentalMaterialApi
fun PostDetailsScreen(
    openScreen: (String) -> Unit,
    postId: String,
    modifier: Modifier = Modifier,
    viewModel: PostDetailsViewModel = hiltViewModel()
) {

    val post by viewModel.post
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) { viewModel.initialize(postId) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .verticalScroll(scrollState)
    ) {
        BasicToolbar(title = R.string.explore)

        Text(text = post.user.userId)
        Text(text = post.user.username)
        Text(text = post.description)

    }
}
