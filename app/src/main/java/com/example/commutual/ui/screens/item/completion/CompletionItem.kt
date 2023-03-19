package com.example.commutual.ui.screens.item

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.commutual.FormatterClass.Companion.formatTimestamp
import com.example.commutual.R
import com.example.commutual.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletionItem(
    task: Task,
    onCLick: () -> Unit,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit
) {

    Row(modifier = Modifier.padding(8.dp)) {


        androidx.compose.material3.Surface(
            onClick = onCLick,
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
                        stringResource(R.string.task_over),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        text = formatTimestamp(task.showCompletionTimestamp, true),
                        modifier = Modifier.alignByBaseline()
                    )

                }

                Text(
                    stringResource(
                        R.string.formatted_task_title, task.title
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    stringResource(
                        R.string.formatted_task_time_nd,
                        task.startTime,
                        task.endTime
                    )
                )

                Text(
                    stringResource(
                        R.string.did_you_complete
                    )
                )

                Row {
                    Button(onClick = onYesClick) {
                        Text(
                            stringResource(R.string.yes),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Button(onClick = onNoClick) {
                        Text(
                            stringResource(R.string.no),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }
            }
        }

    }

}
