package com.example.functions.ui.screens.item

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.functions.ui.screens.profile_post.PostBottomSheetOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetOptionItem(
    option: PostBottomSheetOption,
) {

    Column {
        ListItem(
            headlineText = { Text(text = option.title) },
            leadingContent = {
                Icon(
                    painter = painterResource(option.iconId),
                    contentDescription = option.title
                )
            }
        )
        Divider()
    }
}