package com.example.commutual.ui.screens.item

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.commutual.FormatterClass
import com.example.commutual.FormatterClass.Companion.formatTimestamp
import com.example.commutual.R
import com.example.commutual.model.Task
import com.example.commutual.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatedTaskItem(
    task: Task,
    creator: User,
    onClick: () -> Unit
) {

    Row(modifier = Modifier.padding(8.dp)) {


        androidx.compose.material3.Surface(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp,
            shadowElevation = 1.dp,
            // surfaceColor color will be changing gradually from primary to surface
            color = MaterialTheme.colorScheme.surface,
            // animateContentSize will change the Surface size gradually
            modifier = Modifier
                .animateContentSize()

        ) {
            Column(
                Modifier.padding(8.dp, 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.task_created, creator.username),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        text = formatTimestamp(task.timestamp, true),
                        modifier = Modifier.alignByBaseline()
                    )

                }

                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    stringResource(
                        R.string.formatted_task_time,
                        FormatterClass.formatDate(task.date),
                        task.startTime,
                        task.endTime
                    )
                )
            }
        }

    }

}
