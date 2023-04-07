package com.example.commutual.ui.screens.signup




import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
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
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    openScreen: (String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val fieldModifier = Modifier.fieldModifier()
    val focusManager = LocalFocusManager.current

    Column {


        BasicToolbar(stringResource(R.string.create_account))

        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 12.dp)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailField(
                uiState.email,
                viewModel::onEmailChange,
                fieldModifier,
                focusManager,
                ImeAction.Next
            )
            PasswordField(
                uiState.password,
                viewModel::onPasswordChange,
                fieldModifier,
                focusManager,
                ImeAction.Next
            )
            RepeatPasswordField(
                uiState.repeatPassword,
                viewModel::onRepeatPasswordChange,
                fieldModifier,
                focusManager,
                ImeAction.Done
            )

            BasicButton(AppText.create_account, Modifier.basicButton()) {
                viewModel.onSignUpClick(openAndPopUp, openScreen)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(AppText.already_have_account),
                    Modifier.accountText(), maxLines = 1,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelMedium,
                )
                BasicTextButton(AppText.log_in, Modifier.textButton()) {
                    viewModel.onLoginClick(openAndPopUp)
                }
            }
        }
    }
}
