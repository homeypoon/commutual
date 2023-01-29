package com.example.commutual.ui.screens.login/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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

    BasicToolbar(AppText.log_in)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
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
                stringResource(AppText.dont_have_account),
                Modifier.accountText(), maxLines = 1,
                fontSize = 16.sp)
            BasicTextButton(AppText.register, Modifier.textButton()) {
                viewModel.onRegisterClick(openAndPopUp)
            }
        }

    }
}
