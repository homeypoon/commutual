package com.example.commutual.ui.screens.item

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.commutual.model.Message
import com.example.commutual.model.User
import com.google.firebase.Timestamp

@Composable
fun MessageItem(
    message: Message,
    sender: User,
    formatTimestamp: (Timestamp) -> String
) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier =
            Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = formatTimestamp(message.timestamp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
            )

            Surface(
                shape = MaterialTheme.shapes.small,
                elevation = 1.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .animateContentSize()
                    .defaultMinSize(minWidth = 64.dp)
            ) {

                Column(
                    modifier = Modifier
                    .padding(16.dp, 6.dp)

                ) {
                    Text(
                        text = sender.username,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelSmall
                    )


                    Text(
                        text = message.text,
                        modifier = Modifier.padding(all = 4.dp),
                        maxLines = 10,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }
        }
    }
}

