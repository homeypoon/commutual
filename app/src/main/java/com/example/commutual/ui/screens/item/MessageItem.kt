package com.example.commutual.ui.screens.item

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.commutual.model.Message

@Composable
fun MessageItem(message: Message, currentTimeMillis: Long) {
    Row(modifier = Modifier.padding(all = 8.dp)) {

        Spacer(modifier = Modifier.width(8.dp))

        Column() {
            Text(
                text = message.senderId,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = MaterialTheme.colorScheme.surface,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its text
                    // otherwise we only display the first line
                    maxLines = Int.MAX_VALUE,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
