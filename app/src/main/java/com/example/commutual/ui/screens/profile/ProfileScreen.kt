package com.example.commutual.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commutual.R
import com.example.commutual.TASK_DEFAULT_ID
import com.example.commutual.common.composable.ActionToolbar
import com.example.commutual.common.composable.BasicButton
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.ui.screens.item.PostItem
import com.example.commutual.R.string as AppText


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@ExperimentalMaterialApi
fun ProfileScreen(
    openScreen: (String) -> Unit,
    userId: String,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) { viewModel.initialize(userId) }

    val uiState by viewModel.uiState
    val posts = if (userId != TASK_DEFAULT_ID) {
        viewModel.storageService.getUserPosts(userId)
    } else {
        viewModel.storageService.getUserPosts(viewModel.accountService.currentUserId)
    }

    val userPosts = posts.collectAsStateWithLifecycle(emptyList())
    val user by viewModel.user

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        if (userId != TASK_DEFAULT_ID) {
            BasicToolbar(
                title = stringResource(AppText.profile)
            )
        } else {
            ActionToolbar(
                title = stringResource(AppText.profile),
                endActionIcon = R.drawable.ic_settings,
                endAction = { viewModel.onSettingsClick(openScreen) },
                modifier = modifier
            )
        }

        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth()
//                    .wrapContentHeight()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                ) {

                    Text(
                        text = user.username,
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = stringResource(
                            AppText.days_on_platform,
                            uiState.userTotalDays.toString()
                        ),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp, 12.dp),
                    ) {
                        androidx.compose.material3.Surface(
                            shape = RoundedCornerShape(4.dp),
                            shadowElevation = 5.dp,
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 100.dp)
                        ) {
                            Text(
                                text = user.bio,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 8,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(12.dp, 6.dp),
                                textAlign = TextAlign.Start,
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .height(IntrinsicSize.Min)
//                            .align(Alignment.Center)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(4.dp, 0.dp)
                                .weight(1f)
                                .weight(1f)
                        ) {
                            Text(
                                text = user.commitCount.toString(),
                                style = MaterialTheme.typography.labelLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .fillMaxWidth()
                            )
                            Text(
                                text = stringResource(R.string.commit_count),
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .fillMaxWidth()
                            )
                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(4.dp, 0.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = uiState.totalTasksScheduled.toString(),
                                style = MaterialTheme.typography.labelLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .fillMaxWidth()
                            )
                            Text(
                                text = stringResource(R.string.tasks_scheduled),
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .fillMaxWidth()
                            )
                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(4.dp, 0.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = uiState.completionRate,
                                style = MaterialTheme.typography.labelLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .fillMaxWidth()
                            )
                            Text(
                                text = stringResource(R.string.completion_rate),
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                if (userId == TASK_DEFAULT_ID) {
                    BasicButton(
                        AppText.edit_profile,
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 16.dp, 16.dp, bottom = 12.dp)
                    ) { viewModel.onEditProfileClick(openScreen) }
                }
                Divider(modifier = Modifier.padding(0.dp, 4.dp))
            }

            items(userPosts.value, key = { it.postId }) { postItem ->

                Surface(modifier = Modifier.clickable {
                    if (userId != TASK_DEFAULT_ID) {
                        viewModel.onOtherPostClick(openScreen, postItem)
                    } else {
                        viewModel.onOwnPostClick(openScreen, postItem)
                    }
                }) {
                    PostItem(
                        post = postItem
                    )
                }

            }
        }


    }
}
