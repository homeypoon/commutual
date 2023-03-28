package com.example.commutual.ui.screens.post_details


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.R
import com.example.commutual.common.composable.BasicButton
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.common.composable.DialogCancelButton
import com.example.commutual.common.composable.DialogConfirmButton
import com.example.commutual.model.User

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
        BasicToolbar(title = stringResource(R.string.post_details))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 36.dp)

        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            ) {
                Text(
                    text = AnnotatedString(
                        user.username,
                        SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.inversePrimary
                        ),

                        ),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .clickable {
                            viewModel.onUsernameClick(openScreen)
                        }
                )

            }



            androidx.compose.material3.Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
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


                    Divider(Modifier.padding(16.dp))

                    Text(
                        text = post.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    ChattingButton(
                        openScreen, viewModel, user,
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp, top = 64.dp, bottom = 16.dp)
                            .fillMaxWidth(),
                    )
                }
            }
        }

    }
}

@ExperimentalMaterialApi
@Composable
private fun ChattingButton(
    openScreen: (String) -> Unit,
    viewModel: PostDetailsViewModel,
    user: User,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BasicButton(
            text = uiState.chattingButtonText,
            modifier = modifier,
            onClick = { viewModel.updateStartChattingCard(openScreen) })
    }
    if (uiState.showStartChattingCard) {

        androidx.compose.material3.AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            text = {
                Text(
                    text = stringResource(R.string.start_chatting_with, user.username),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(4.dp, 20.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            dismissButton = {
                DialogCancelButton(R.string.cancel) {
                    viewModel.setShowRequestMatchCard(false)
                    focusManager.clearFocus()
                }
            },
            confirmButton = {
                DialogConfirmButton(R.string.start_chatting,
                    action = {
                        viewModel.setShowRequestMatchCard(false)
                        focusManager.clearFocus()
                        viewModel.onStartChattingClick(openScreen)
                    }
                )
            },
            onDismissRequest = { viewModel.setShowRequestMatchCard(false) },
            shape = MaterialTheme.shapes.medium
        )
    }

}

