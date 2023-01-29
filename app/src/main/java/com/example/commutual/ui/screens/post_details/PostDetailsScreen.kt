package com.example.commutual.ui.screens.post_details/*
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


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.example.commutual.R
import com.example.commutual.common.composable.*
import com.example.commutual.common.ext.basicButton

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
    val user by viewModel.user
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) { viewModel.initialize(postId) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
    ) {
        BasicToolbar(title = R.string.explore)

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = user.username,
                fontSize = 12.sp
            )
        }

        Text(
            text = post.title,
            fontSize = 18.sp, style = MaterialTheme.typography.h2
        )
        Text(
            text = post.description,
            fontSize = 18.sp, style = MaterialTheme.typography.body1
        )

        RequestMatchButton (
            openScreen, viewModel)
//        {
//            viewModel.onRequestMatchClick(openScreen)
//        }

    }
}

@ExperimentalMaterialApi
@Composable
//private fun com.example.commutual.ui.screens.post_details.RequestMatchButton(onRequestMessageChange: (String) -> Unit, viewModel: PostDetailsViewModel) {

private fun RequestMatchButton(openScreen: (String) -> Unit, viewModel: PostDetailsViewModel) {
    var showRequestMatchCard by remember { mutableStateOf(false) }
    val text = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState

    BasicButton(
        R.string.request_match,
        Modifier.basicButton()
    ) { showRequestMatchCard = true }


    if (showRequestMatchCard) {
        AlertDialog(
            text = {
                Column(Modifier.fillMaxSize()) {
                    Text(text = stringResource(id = R.string.request_match),
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(4.dp, 20.dp))
                    DescriptionField(
                        R.string.request_match_message,
                        uiState.requestMessage,
                        viewModel::onRequestMessageChange ,
                        Modifier
                            .padding(2.dp, 8.dp)
                            .weight(1f)
                            .wrapContentHeight(),
                        KeyboardCapitalization.Sentences,
                        focusManager
                    )
                }
            },
            dismissButton = {
                DialogCancelButton(R.string.cancel) {
                    showRequestMatchCard = false
                    focusManager.clearFocus()
                }
            },
            confirmButton = {
                DialogConfirmButton(R.string.request_match) {
                    showRequestMatchCard = false
                    focusManager.clearFocus()
                    viewModel.resetRequestMessage()
                    viewModel.onRequestMatchClick(openScreen)
                }
            },
            onDismissRequest = { showRequestMatchCard = false },
            modifier = Modifier.height(380.dp)
        )
    }
}

