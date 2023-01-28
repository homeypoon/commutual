/*
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

package com.example.commutual.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.commutual.R.drawable as AppIcon
import com.example.commutual.R.string as AppText

@Composable
fun BasicField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    imeAction: ImeAction,
    capitalization: KeyboardCapitalization,
    focusManager: FocusManager
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(text)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction,
            autoCorrect = true,
            capitalization = capitalization,
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )
    )
}

@Composable
fun DescriptionField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    capitalization: KeyboardCapitalization,
    focusManager: FocusManager
) {
    OutlinedTextField(
        modifier = modifier.height(160.dp),
        value = value,
        maxLines = 6,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(text)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            autoCorrect = true,
            capitalization = capitalization,
        )

    )
}

@Composable
fun RequestMessageField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    capitalization: KeyboardCapitalization,
    focusManager: FocusManager
) {
    OutlinedTextField(
        modifier = modifier.height(180.dp),
        value = value,
        maxLines = 8,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(text)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            autoCorrect = true,
            capitalization = capitalization,
        )

    )
}

@Composable
fun EndField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    capitalization: KeyboardCapitalization,
    focusManager: FocusManager,
    maxLines: Int = 1,
) {
    OutlinedTextField(
        modifier = modifier.focusable(true),
        value = value,
        maxLines = maxLines,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(text)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            autoCorrect = true,
            capitalization = capitalization,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        )

    )
}

@Composable
fun EmailField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    imeAction: ImeAction
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(AppText.email)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),

    )
}

@Composable
fun PasswordField(
    value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier, focusManager: FocusManager, imeAction: ImeAction) {
    PasswordField(value, AppText.password, onNewValue, modifier, focusManager, imeAction)
}

@Composable
fun RepeatPasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    imeAction: ImeAction
) {
    PasswordField(value, AppText.repeat_password, onNewValue, modifier, focusManager, imeAction)
}

@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    imeAction: ImeAction
) {
    var isVisible by remember { mutableStateOf(false) }

    val icon =
        if (isVisible) painterResource(AppIcon.ic_visibility_on)
        else painterResource(AppIcon.ic_visibility_off)

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = icon, contentDescription = "Visibility")
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        visualTransformation = visualTransformation
    )
}
