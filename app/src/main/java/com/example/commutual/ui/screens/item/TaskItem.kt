package com.example.commutual.ui.screens.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.commutual.FormatterClass
import com.example.commutual.R
import com.example.commutual.common.ext.categoryChip
import com.example.commutual.model.Task
import com.example.commutual.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    creator: User,
) {
    ListItem(
        colors = ListItemDefaults.colors(
            MaterialTheme.colorScheme.surface
        ),
        overlineText = {

            Column {
                Text(
                    stringResource(task.category.categoryStringRes),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.categoryChip(
                        MaterialTheme.colorScheme.secondary
                    )
                )
            }


        },
        headlineText = {
            Text(
                stringResource(R.string.formatted_task, task.title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )
        },
        supportingText = {
            Column {
                if (!task.details.isEmpty()) {
                    Text(
                        task.details,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Text(
                    stringResource(
                        R.string.formatted_task_time,
                        FormatterClass.formatDate(selectedYear = task.year,
                            selectedMonth = task.month,
                            selectedDay = task.day
                        ),
                        task.startTime,
                        task.endTime
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 6.dp)

                )
            }
        },
        shadowElevation = 4.dp,
        modifier = Modifier.padding(horizontal = 18.dp)
//        tonalElevation = 4.dp
    )
}
