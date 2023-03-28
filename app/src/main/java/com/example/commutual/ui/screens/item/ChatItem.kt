package com.example.commutual.ui.screens.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.commutual.model.Chat
import com.example.commutual.model.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatItem(
    chat: Chat,
    user: User
) {
    Column {
        ListItem(
            colors = ListItemDefaults.colors(
                MaterialTheme.colorScheme.surface
            ),
            headlineText = {
                Text(
                    user.username,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 14.dp),
                )
            },
        )
        Divider()
    }

}