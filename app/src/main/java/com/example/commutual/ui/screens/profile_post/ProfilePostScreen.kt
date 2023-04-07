package com.example.commutual.ui.screens.profile_post

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.R
import com.example.commutual.common.composable.ActionToolbar
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

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    val bottomSheetOptions = listOf(
        ProfilePostBottomSheetOption.Edit,
        ProfilePostBottomSheetOption.Delete
    )
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            LazyColumn {
                items(bottomSheetOptions) {
                    Surface(modifier = Modifier.clickable {
                        when (it) {
                            ProfilePostBottomSheetOption.Edit -> viewModel.onEditPostClick(
                                openScreen, post, coroutineScope, bottomSheetState
                            )
                            ProfilePostBottomSheetOption.Delete -> viewModel.onDeletePostClick(
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
                .background(MaterialTheme.colorScheme.background)
        ) {
            ActionToolbar(
                stringResource(R.string.my_post),
                endActionIcon = R.drawable.ic_vertical_dots
            ) { viewModel.onIconClick(coroutineScope, bottomSheetState) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 36.dp)

            ) {

                androidx.compose.material3.Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .defaultMinSize(minHeight = 320.dp)
                ) {

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 22.dp, start = 0.dp, end = 0.dp)
                    ) {

                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()

                        ) {

                            Text(
                                stringResource(post.category.categoryStringRes),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }


                        androidx.compose.material3.Divider(Modifier.padding(16.dp))

                        Text(
                            text = post.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.padding(48.dp))

                    }

                }

            }
        }
    }


    LaunchedEffect(Unit) { viewModel.initialize(postId) }

}
