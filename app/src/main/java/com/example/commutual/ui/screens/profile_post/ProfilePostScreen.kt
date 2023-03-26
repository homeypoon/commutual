package com.example.commutual.ui.screens.profile_post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.R
import com.example.commutual.common.composable.ActionToolbar
import com.example.commutual.common.ext.categoryChip
import com.example.commutual.ui.screens.item.BottomSheetOptionItem

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
            ActionToolbar(
                stringResource(R.string.my_post),
                endActionIcon = R.drawable.ic_vertical_dots,
                endAction = { viewModel.onIconClick(coroutineScope, bottomSheetState) }
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 36.dp)
            ) {

                Text(
                    stringResource(post.category.categoryStringRes),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.categoryChip(
                        MaterialTheme.colorScheme.secondary
                    )
                )

                Text(text = post.title,
                    style = MaterialTheme.typography.headlineMedium)
                Text(text = post.description,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }

    }


    LaunchedEffect(Unit) { viewModel.initialize(postId) }

}
