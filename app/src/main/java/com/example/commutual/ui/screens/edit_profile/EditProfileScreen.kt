package com.example.commutual.ui.screens.edit_profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun EditProfileScreen(
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) { viewModel.initialize() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionToolbar(
            title = AppText.edit_profile,
            modifier = Modifier.toolbarActions(),
            endActionIcon = AppIcon.ic_check,
            endAction = { viewModel.onDoneClick(popUpScreen, focusManager) }
        )

        Spacer(modifier = Modifier.spacer())

        val fieldModifier = Modifier.fieldModifier()

        BasicField(
            AppText.username,
            user.username,
            viewModel::onNameChange,
            fieldModifier,
            ImeAction.Next,
            KeyboardCapitalization.Words,
            focusManager
        )
        DescriptionField(
            AppText.user_bio,
            user.bio,
            viewModel::onBioChange,
            fieldModifier,
            KeyboardCapitalization.Sentences,
            focusManager
        )
    }
}