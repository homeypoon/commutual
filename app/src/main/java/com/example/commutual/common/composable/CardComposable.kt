package com.example.commutual.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@ExperimentalMaterialApi
@Composable
fun WarningCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick)
}

@ExperimentalMaterialApi
@Composable
fun RegularCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    onEditClick: () -> Unit,
) {
    CardEditor(title, icon, content, onEditClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@Composable
private fun CardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    onEditClick: () -> Unit,
) {
    Card(
        onClick = onEditClick,
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.surface,
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = RoundedCornerShape(12.dp),

        modifier = Modifier
            .padding(top = 16.dp)
            .border(1.dp, color = MaterialTheme.colorScheme.outline)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Text(
                stringResource(title),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
            )

            if (content.isNotBlank()) {
                Text(
                    text = content,
                    modifier = Modifier.padding(8.dp, 0.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Icon(
                painter = painterResource(icon),
                contentDescription = "Icon",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

