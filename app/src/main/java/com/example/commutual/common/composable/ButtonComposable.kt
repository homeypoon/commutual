package com.example.commutual.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BasicTextButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    TextButton(onClick = action, modifier = modifier) { Text(text = stringResource(text)) }
}

@Composable
fun BasicButton(@StringRes text: Int, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        colors =
        ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    ) {
        Text(
            text = stringResource(text).uppercase(),
            fontSize = 16.sp,
            letterSpacing = 1.5.sp,
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@Composable
fun BasicIconButton(
    imageVector: ImageVector,
    modifier: Modifier,
    onIconClick: () -> Unit,
    @StringRes contentDescription: Int
) {
    IconButton(
        modifier = Modifier.size(24.dp),
        onClick = onIconClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(contentDescription)
        )
    }
}

@Composable
fun DialogConfirmButton(@StringRes text: Int, action: () -> Unit) {

    TextButton(
        onClick = action,
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun DialogCancelButton(@StringRes text: Int, action: () -> Unit) {
    TextButton(
        onClick = action,
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }

}
