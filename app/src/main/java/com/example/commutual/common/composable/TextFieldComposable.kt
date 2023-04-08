package com.example.commutual.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
        placeholder = {
            Text(
                stringResource(text),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction,
            autoCorrect = true,
            capitalization = capitalization,
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
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
        modifier = modifier,
        value = value,
        maxLines = 8,
        onValueChange = { onNewValue(it) },
        placeholder = {
            Text(
                stringResource(text),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            autoCorrect = true,
            capitalization = capitalization,
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    text: String,
    value: String,
    onNewValue: (String) -> Unit,
    onSearchClick: (FocusManager) -> Unit,
    onFilterClick: (FocusManager) -> Unit,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    capitalization: KeyboardCapitalization,
    focusManager: FocusManager
) {
    OutlinedTextField(
        modifier = modifier,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        value = value,
        maxLines = 1,
        onValueChange = { onNewValue(it) },
        placeholder = {
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            IconButton(
                onClick = { onFilterClick(focusManager) },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = AppIcon.ic_filter),
                    contentDescription = null,
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { onSearchClick(focusManager) },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = AppIcon.ic_explore),
                    contentDescription = null,
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            autoCorrect = true,
            capitalization = capitalization,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClick(focusManager) }),

        )
}

@Composable
fun DropDownField(
    value: String,
    labelText: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit,
    expanded: Boolean,
    setExpandedDropDownMenu: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        textStyle = MaterialTheme.typography.bodyMedium,
        onValueChange = { onValueChange(it) },
        label = {
            Text(
                text = labelText,
                style = MaterialTheme.typography.labelSmall
            )
        },
        placeholder = {
            Text(
                stringResource(AppText.category),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        trailingIcon = {
            Icon(
                icon, "contentDescription",
                Modifier.clickable { setExpandedDropDownMenu(!expanded) })
        },
        readOnly = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    )
}


@Composable
fun MessageInputField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    leadingIcon: (@Composable () -> Unit),
    trailingIcon: (@Composable () -> Unit),
    maxLines: Int,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier.padding(horizontal = 12.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        value = value,
        maxLines = maxLines,
        onValueChange = {
            onNewValue(it)
        },
        placeholder = {
            Text(
                stringResource(text),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            autoCorrect = true,
            capitalization = KeyboardCapitalization.Sentences,
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
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
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(AppText.email)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    )
}

@Composable
fun PasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    imeAction: ImeAction
) {
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
        textStyle = MaterialTheme.typography.bodyMedium,
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
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        visualTransformation = visualTransformation,
    )
}
