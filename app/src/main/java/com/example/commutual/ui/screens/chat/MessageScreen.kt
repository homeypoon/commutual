package com.example.commutual.ui.screens.chat

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.FormatterClass
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.common.composable.MessageInputField
import com.example.commutual.model.Message
import com.example.commutual.model.Task
import com.example.commutual.ui.screens.item.CreatedTaskItem
import com.example.commutual.ui.screens.item.MessageItem
import com.example.commutual.ui.screens.item.TaskItem
import kotlinx.coroutines.launch
import com.example.commutual.R.string as AppText

@Composable
fun MessagesScreen(
    popUpScreen: () -> Unit,
    openScreen: (String) -> Unit,
    chatId: String,
    modifier: Modifier = Modifier,
    viewModel: MessageViewModel = hiltViewModel()
) {

//    val messages = remember { mutableStateListOf<Message>() }
    LaunchedEffect(Unit) {
        viewModel.getSender(chatId)
    }

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState

    val messages by viewModel.getMessagesWithUsers(chatId).collectAsState(emptyList())

    val tasksWithUsers by viewModel.getTasksWithUsers(chatId)
        .collectAsState(Pair(emptyList(), emptyList()))
    val (completedTasks, upcomingTasks) = tasksWithUsers

    // Sort messages and tasks by timestamp
    val messagesWithTasks = (messages.map { Pair(it.first.timestamp, it) } +
            upcomingTasks.map { Pair(it.first.timestamp, it) } +
            completedTasks.map { Pair(it.first.timestamp, it) })
        .sortedBy { it.first }
        .map { it.second }


    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            scrollState.animateScrollToItem(messages.size - 1)
        }
    }

    androidx.compose.material.Scaffold(

        floatingActionButton = {

            if (viewModel.tabIndex == 1) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            text = stringResource(R.string.add_task),
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.displayMedium
                        )
                    },
                    icon = {
                        androidx.compose.material.Icon(
                            Icons.Filled.Add, "Add",
                            tint = MaterialTheme.colorScheme.onTertiary
                        )
                    },
                    onClick = { viewModel.onAddTaskClick(openScreen) },
                    modifier = modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            BasicToolbar(
                title = (stringResource(AppText.chat_with, uiState.partner.username))
            )

            TabRow(selectedTabIndex = viewModel.tabIndex) {
                viewModel.messageTabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = viewModel.tabIndex == index,
                        onClick = { viewModel.setMessageTab(index) },
                        text = {
                            Text(
                                text = stringResource(tab.tabStringRes),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }


            if (viewModel.tabIndex == 0) {

//            Chat Tab
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    state = scrollState,
                    contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
                ) {
                    items(messagesWithTasks) { (item, user) ->

                        when (item) {
                            is Message -> {
                                MessageItem(item, user)
                                { timestamp -> FormatterClass.formatTimestamp(timestamp, true) }
                            }
                            is Task -> {
                                CreatedTaskItem(item, user, viewModel::onCreatedTaskCLicked)
                            }
                        }

                    }
                }

                MessageInputField(
                    R.string.message,
                    uiState.messageText,
                    viewModel::onMessageTextChange,
                    {
                        Icon(
                            painter = painterResource(R.drawable.ic_home),
                            contentDescription = stringResource(R.string.send)
                        )
                    },
                    {
                        IconButton(
                            onClick = {

                                coroutineScope.launch {
                                    viewModel.onSendClick(chatId, focusManager)
                                    if (messages.isNotEmpty()) {
                                        scrollState.animateScrollToItem(messages.size - 1)
                                        Log.v("mscreen", messages.get(messages.size - 1).first.text)

                                    }
                                }
                            },
                            content = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_send),
                                    contentDescription = stringResource(R.string.send)
                                )
                            }
                        )
                    },
                    4,
                    Modifier
                        .padding(2.dp, 8.dp)
                        .fillMaxWidth(),
                    focusManager
                )

            } else {
                // Tasks Tab

                if (upcomingTasks.isNotEmpty()) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.upcoming_tasks)
                            )
                        }
                        items(upcomingTasks) { (upcomingTasks, creator) ->
                            TaskItem(task = upcomingTasks, creator = creator)
                        }
                    }
                }

                if (completedTasks.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.completed_tasks)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
                    ) {
                        items(completedTasks) { (completedTasks, creator) ->
                            TaskItem(task = completedTasks, creator = creator)
                        }
                    }

                    Spacer(modifier = Modifier.padding(10.dp))

                }

            }
        }

    }
}


