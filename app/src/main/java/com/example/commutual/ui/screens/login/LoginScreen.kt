package com.example.commutual.ui.screens.login

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.R
import com.example.commutual.common.composable.*
import com.example.commutual.common.ext.accountText
import com.example.commutual.common.ext.basicButton
import com.example.commutual.common.ext.fieldModifier
import com.example.commutual.common.ext.textButton
import com.example.commutual.R.string as AppText

@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val focusManager = LocalFocusManager.current

    BasicToolbar(title = stringResource(R.string.log_in))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })},
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(
            uiState.email,
            viewModel::onEmailChange, Modifier.fieldModifier(), focusManager, ImeAction.Next
        )
        PasswordField(uiState.password,
            viewModel::onPasswordChange,
            Modifier.fieldModifier(), focusManager, ImeAction.Done)

        BasicButton(
            AppText.log_in,
            Modifier.basicButton()
        ) { viewModel.onSignInClick(openAndPopUp) }

        BasicTextButton(AppText.forgot_password,
            Modifier.textButton()) {
            viewModel.onForgotPasswordClick()
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(AppText.don_t_have_account),
                Modifier.accountText(), maxLines = 1,
                fontSize = 16.sp)
            BasicTextButton(AppText.register, Modifier.textButton()) {
                viewModel.onRegisterClick(openAndPopUp)
            }
        }

    }
}
