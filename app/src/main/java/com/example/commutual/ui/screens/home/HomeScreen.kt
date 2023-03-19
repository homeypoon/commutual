package com.example.commutual.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.model.User
import com.example.commutual.ui.screens.item.TaskItem

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

//    LaunchedEffect(Unit) { viewModel.initialize() }

    val upcomingUserTasks = viewModel.upcomingUserTasks.collectAsStateWithLifecycle(emptyList())


    Scaffold(
    floatingActionButton = {

        ExtendedFloatingActionButton(
            text = {
                Text(text = stringResource(R.string.add_post),
                    color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.displayMedium)
            },
            icon = {
                Icon(Icons.Filled.Add, "Add",
                    tint = MaterialTheme.colorScheme.onTertiary)
            },
            onClick = { viewModel.onAddClick(openScreen) },
            modifier = modifier.padding(16.dp),
            containerColor = MaterialTheme.colorScheme.tertiary,
        )
    }
    ) { padding ->
        Column(
            modifier = modifier.padding(padding)
        ) {
            BasicToolbar(
                title = stringResource(R.string.home)
            )

            if (upcomingUserTasks.value.isNotEmpty()) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.upcoming_tasks_sessions)
                        )
                    }
                    items(upcomingUserTasks.value) { task ->
                        TaskItem(task = task, creator = User())
                    }
                }
            }
        }
    }
}