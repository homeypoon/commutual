



package com.example.commutual.ui.screens.profile_post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.R
import com.example.commutual.common.composable.BasicIconButton
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.common.ext.basicIconButton
import com.example.commutual.ui.screens.item.BottomSheetOptionItem
import com.example.commutual.R.string as AppText

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
                stringResource(R.string.my_post)
            )

            BasicIconButton(
                imageVector = Icons.Default.MoreVert,
                modifier = Modifier.basicIconButton(),
                onIconClick = { viewModel.onIconClick(coroutineScope, bottomSheetState) },
                contentDescription = AppText.more
            )

            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
            Text(text = post.description, style = MaterialTheme.typography.bodyMedium)
        }

    }


    LaunchedEffect(Unit) { viewModel.initialize(postId) }

}
