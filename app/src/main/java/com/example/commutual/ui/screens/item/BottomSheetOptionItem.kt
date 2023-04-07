package com.example.commutual.ui.screens.item

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.commutual.ui.screens.profile_post.PostDetailsBottomSheetOption
import com.example.commutual.ui.screens.profile_post.ProfilePostBottomSheetOption
import com.example.commutual.ui.screens.profile_post.ReportBottomSheetOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetOptionItem(
    option: ProfilePostBottomSheetOption,
) {
    Column {
        ListItem(
            headlineText = {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            leadingContent = {
                Icon(
                    painter = painterResource(option.iconId),
                    contentDescription = option.title,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        Divider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetOptionItem(
    option: PostDetailsBottomSheetOption,
) {
    Column {
        ListItem(
            headlineText = {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error
                )
            },
            leadingContent = {
                Icon(
                    painter = painterResource(option.iconId),
                    contentDescription = option.title,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        )
        Divider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetOptionItem(
    option: ReportBottomSheetOption,
) {
    Column {
        ListItem(
            headlineText = {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        Divider()
    }
}