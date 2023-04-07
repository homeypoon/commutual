package com.example.commutual.common.composable

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicToolbar(title: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.displayLarge
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            MaterialTheme.colorScheme.secondary
        ),
    )
    Divider(thickness = 2.dp,
        color = MaterialTheme.colorScheme.onSecondary)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionToolbar(
    title: String,
    @DrawableRes endActionIcon: Int,
    endAction: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            MaterialTheme.colorScheme.secondary
        ),
        actions = {
            IconButton(onClick = endAction) {
                Icon(
                    painter = painterResource(endActionIcon),
                    contentDescription = "Action",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    )

    Divider(thickness = 2.dp,
        color = MaterialTheme.colorScheme.onSecondary
    )
}
