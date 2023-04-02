package com.example.commutual.ui.screens.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.commutual.common.ext.categoryChip
import com.example.commutual.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    post: Post,
) {
    Column {

        ListItem(
            colors = ListItemDefaults.colors(
                MaterialTheme.colorScheme.surface
            ),
            overlineText = {
                Text(
                    stringResource(post.category.categoryStringRes),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .categoryChip(
                            MaterialTheme.colorScheme.secondary
                        )
                        .padding(top = 2.dp)
                )
            },
            headlineText = {
                Text(
                    post.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 2.dp)
                )
            },
            supportingText = {
                Column {
                    Text(
                        post.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                    )

                }
            }
        )
        Divider()
    }

}
