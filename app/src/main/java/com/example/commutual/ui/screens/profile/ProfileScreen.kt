

package com.example.commutual.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commutual.R
import com.example.commutual.common.composable.ActionToolbar
import com.example.commutual.common.composable.BasicButton
import com.example.commutual.common.ext.basicButton
import com.example.commutual.ui.screens.item.PostItem
import com.example.commutual.R.string as AppText


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@ExperimentalMaterialApi
fun ProfileScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) { viewModel.initialize() }

    val uiState by viewModel.uiState
    val userPosts = viewModel.userPosts.collectAsStateWithLifecycle(emptyList())
    val user by viewModel.user

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentHeight()
    ) {
        ActionToolbar(
            title = stringResource(AppText.profile),
            endActionIcon = R.drawable.ic_settings,
            endAction = { viewModel.onSettingsClick(openScreen) },
            modifier = modifier
        )


        LazyColumn(
            Modifier
                .weight(1f)
//                    .wrapContentHeight()
        ) {
            item {
                Text(text = user.username, style = MaterialTheme.typography.headlineLarge)
                Text(text = user.bio, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = user.commitCount.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(AppText.days, uiState.userTotalDays.toString()),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "totalTasksScheduled: ${uiState.totalTasksScheduled}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Log.d("profile", "totalTasksScheduled: ${uiState.totalTasksScheduled}")
                Text(
                    text = "totalTasksCompleted: ${uiState.totalTasksCompleted}",
                    style = MaterialTheme.typography.bodyMedium
                )


                BasicButton(
                    AppText.edit_profile,
                    Modifier.basicButton()
                ) { viewModel.onEditProfileClick(openScreen) }
            }
            items(userPosts.value, key = { it.postId }) { postItem ->
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