package com.example.functions.screens.edit_post

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.functions.R.drawable as AppIcon
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.functions.R.string as AppText
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.functions.common.composable.ActionToolbar
import com.example.functions.common.composable.BasicField
import com.example.functions.common.ext.fieldModifier
import com.example.functions.common.ext.spacer
import com.example.functions.common.ext.toolbarActions

@Composable
fun EditPostScreen(
    openScreen: (String) -> Unit,
//    popUpScreen: () -> Unit,
    postId: String,
    modifier: Modifier = Modifier,
    viewModel: EditPostViewModel = hiltViewModel()
) {
    val post by viewModel.post

    LaunchedEffect(Unit) { viewModel.initialize(postId) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionToolbar(
            title = AppText.create_post,
            modifier = Modifier.toolbarActions(),
            endActionIcon = AppIcon.ic_check,
            endAction = { viewModel.onDoneClick(openScreen)}
        )

        Spacer(modifier = Modifier.spacer())

        val fieldModifier = Modifier.fieldModifier()

        BasicField(AppText.post_title, post.title, viewModel::onTitleChange, fieldModifier)
        BasicField(AppText.post_description, post.description, viewModel::onDescriptionChange, fieldModifier)
    }
}