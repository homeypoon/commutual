package com.example.commutual.ui.screens.edit_post

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.common.composable.ActionToolbar
import com.example.commutual.common.composable.BasicField
import com.example.commutual.common.composable.DescriptionField
import com.example.commutual.common.ext.fieldModifier
import com.example.commutual.common.ext.spacer
import com.example.commutual.common.ext.toolbarActions
import com.example.commutual.R.drawable as AppIcon
import com.example.commutual.R.string as AppText

@Composable
fun EditPostScreen(
    popUpScreen: () -> Unit,
    postId: String,
    modifier: Modifier = Modifier,
    viewModel: EditPostViewModel = hiltViewModel()
) {
    val post by viewModel.post
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) { viewModel.initialize(postId) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        ActionToolbar(
            title = AppText.create_post,
            modifier = Modifier.toolbarActions(),
            endActionIcon = AppIcon.ic_check,
            endAction = { viewModel.onDoneClick(popUpScreen, focusManager) }
        )

        Spacer(modifier = Modifier.spacer())

        val fieldModifier = Modifier.fieldModifier()

        BasicField(
            AppText.post_title, post.title,
            viewModel::onTitleChange, fieldModifier,
            ImeAction.Next,
            KeyboardCapitalization.Words,
            focusManager
        )
        DescriptionField(
            AppText.post_description,
            post.description,
            viewModel::onDescriptionChange,
            fieldModifier
                .wrapContentHeight(),
            KeyboardCapitalization.Sentences,
            focusManager
        )
    }
}