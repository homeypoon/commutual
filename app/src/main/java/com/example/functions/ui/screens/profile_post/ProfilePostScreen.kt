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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.functions.R
import com.example.functions.common.composable.ActionToolbar
import com.example.functions.common.composable.BasicIconButton
import com.example.functions.common.composable.BasicToolbar
import com.example.functions.common.composable.BottomSheetComposable
import com.example.functions.common.ext.basicIconButton
import com.example.functions.common.ext.smallSpacer
import com.example.functions.common.ext.toolbarActions
import com.example.functions.ui.screens.profile_post.ProfilePostViewModel
import kotlinx.coroutines.launch
import com.example.functions.R.drawable as AppIcon
import com.example.functions.R.string as AppText

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@ExperimentalMaterialApi
fun ProfilePostScreen(
    openScreen: (String) -> Unit,
    postId: String,
    modifier: Modifier = Modifier,
    viewModel: ProfilePostViewModel = hiltViewModel()
) {

    val post by viewModel.post
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            BottomSheetComposable()
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
