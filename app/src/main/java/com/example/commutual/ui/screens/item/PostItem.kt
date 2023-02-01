package com.example.commutual.ui.screens.item

import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.commutual.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    post: Post,
) {
    ListItem(
        headlineText = {
            Text(
                post.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        supportingText = {
            Text(
                post.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
//    Divider()
//    Card(
//        backgroundColor = MaterialTheme.colors.background,
//        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(modifier = Modifier.weight(1f)) {
//                Text(text = post.title)
//                Text(text = post.description)
//            }
//        }
}
