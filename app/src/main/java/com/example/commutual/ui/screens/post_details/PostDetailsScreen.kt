package com.example.commutual.ui.screens.post_details


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.R
import com.example.commutual.common.composable.BasicButton
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.common.composable.DialogCancelButton
import com.example.commutual.common.composable.DialogConfirmButton
import com.example.commutual.common.ext.basicButton
import com.example.commutual.common.ext.categoryChip
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

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = user.username,
                fontSize = 12.sp
            )
        }

        Text(
            stringResource(post.category.categoryStringRes),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.categoryChip(
                MaterialTheme.colorScheme.secondary
            )
        )

        Text(
            text = post.title,
            fontSize = 18.sp, style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = post.description,
            fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium
        )

        ChattingButton(
            openScreen, viewModel, user
        )
//        {
//            viewModel.onStartChattingClick(openScreen)
//        }

    }
}

@ExperimentalMaterialApi
@Composable
//private fun com.example.commutual.ui.screens.post_details.ChattingButton(onRequestMessageChange: (String) -> Unit, viewModel: PostDetailsViewModel) {

private fun ChattingButton(
    openScreen: (String) -> Unit,
    viewModel: PostDetailsViewModel,
    user: User
) {
//    var showStartChattingCard by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState
    val coroutineScope = rememberCoroutineScope()

    BasicButton(
        text = uiState.chattingButtonText,
        Modifier.basicButton()
    ) { viewModel.updateStartChattingCard(openScreen) }

    if (uiState.showStartChattingCard) {

            androidx.compose.material3.AlertDialog(
                text = {
                    Text(
                        text = stringResource(R.string.start_chatting_with, user.username),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(4.dp, 20.dp)
                    )
                },
                dismissButton = {
                    DialogCancelButton(R.string.cancel) {
                        viewModel.setShowRequestMatchCard(false)
                        focusManager.clearFocus()
                    }
                },
                confirmButton = {
                    DialogConfirmButton(R.string.start_chatting) {
                        viewModel.setShowRequestMatchCard(false)
                        focusManager.clearFocus()
                        viewModel.onStartChattingClick(openScreen)
                    }
                },
                onDismissRequest = { viewModel.setShowRequestMatchCard(false) },
            )
        }

}

