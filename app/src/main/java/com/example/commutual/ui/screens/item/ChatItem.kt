package com.example.commutual.ui.screens.item

import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.commutual.model.Chat
import com.example.commutual.model.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatItem(
    chat: Chat,
    user: User
) {

    ListItem(
        headlineText = {
            Text(
                user.username,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        supportingText = {
            Text(
                chat.chatId,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )


//    Row(modifier = Modifier.padding(all = 8.dp)) {
//
//        Spacer(modifier = Modifier.width(8.dp))
//
//        Column() {
//            Text(
//                text = chat.chatId,
//                color = MaterialTheme.colorScheme.onSurface,
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            Surface(
//                shape = MaterialTheme.shapes.medium,
//                elevation = 1.dp,
//                // surfaceColor color will be changing gradually from primary to surface
//                color = MaterialTheme.colorScheme.surface,
//                // animateContentSize will change the Surface size gradually
//                modifier = Modifier.animateContentSize().padding(1.dp)
//            ) {
//                Text(
//                    text = chat.membersId[0],
//                    modifier = Modifier.padding(all = 4.dp),
//                    // If the message is expanded, we display all its text
//                    // otherwise we only display the first line
//                    maxLines = Int.MAX_VALUE,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
}
