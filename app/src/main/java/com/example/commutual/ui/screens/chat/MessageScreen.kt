package com.example.commutual.ui.screens.chat

import ConfirmationItem
import ConfirmationResultItem
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commutual.FormatterClass
import com.example.commutual.R
import com.example.commutual.common.composable.MessageInputField
import com.example.commutual.model.AlarmReceiver
import com.example.commutual.model.Message
import com.example.commutual.model.Task
import com.example.commutual.model.Task.Companion.ATTENDANCE_NO
import com.example.commutual.model.Task.Companion.ATTENDANCE_NOT_DONE
import com.example.commutual.model.Task.Companion.ATTENDANCE_YES
import com.example.commutual.model.Task.Companion.COMPLETION_NO
import com.example.commutual.model.Task.Companion.COMPLETION_NOT_DONE
import com.example.commutual.model.Task.Companion.COMPLETION_YES
import com.example.commutual.ui.screens.item.CreatedTaskItem
import com.example.commutual.ui.screens.item.MessageItem
import com.example.commutual.ui.screens.item.TaskItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun MessagesScreen(
    openScreen: (String) -> Unit,
    chatId: String,
    modifier: Modifier = Modifier,
    viewModel: MessageViewModel = hiltViewModel()
) {

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState


    val tasks by viewModel.getTasks(chatId).collectAsStateWithLifecycle(emptyList())
    val upcomingTasks = tasks.filter { !it.showAttendance && !it.showCompletion  }
    val inProgressTasks = tasks.filter { it.showAttendance && !it.showCompletion && (it.attendance[viewModel.currentUserId] != ATTENDANCE_NO) }
    val previousTasks = tasks.filter { (it.showCompletion) || (it.attendance[viewModel.currentUserId] == ATTENDANCE_NO) }


    val messagesWithTasks by viewModel.getMessagesAndTasksWithUsers(chatId)
        .collectAsState(emptyList())

    /**
     * Scroll to bottom of chat
     */
    suspend fun scrollToBottom() {
        if (messagesWithTasks.isNotEmpty()) {
            scrollState.animateScrollToItem(messagesWithTasks.lastIndex)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getSender(chatId)
    }

    androidx.compose.material.Scaffold(

        floatingActionButton = {

            if (viewModel.tabIndex == 1) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            text = stringResource(R.string.add_task),
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.displaySmall
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
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        uiState.partner.username,
                        color = MaterialTheme.colorScheme.onSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.clickable {
                            viewModel.onUsernameClick(openScreen)
                        }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    MaterialTheme.colorScheme.secondary
                ),
            )

            TabRow(selectedTabIndex = viewModel.tabIndex) {
                viewModel.messageTabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = viewModel.tabIndex == index,
                        onClick = { viewModel.setMessageTab(index) },
                        text = {
                            Text(
                                text = stringResource(tab.tabStringRes),
                                style = MaterialTheme.typography.headlineSmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }


            if (viewModel.tabIndex == 0) {

                LaunchedEffect(messagesWithTasks.size) {
                    scrollToBottom()
                }

                // Chat Tab
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    state = scrollState,
                    contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
                ) {

                    if (messagesWithTasks.isNotEmpty()) {
                        items(messagesWithTasks) { (item, user) ->
                            if (item is Message) {
                                MessageItem(item, user)
                                { timestamp -> FormatterClass.formatTimestamp(timestamp, true) }
                            } else if (item is Task) {

                                if (!item.showAttendance) {
                                    CreatedTaskItem(item, user, viewModel::onCreatedTaskCLicked)
                                }

                                if (item.showAttendance && (item.attendance[viewModel.currentUserId] == ATTENDANCE_NOT_DONE)) {
                                    ConfirmationItem(
                                        confirmationType = AlarmReceiver.ATTENDANCE,
                                        task = item,
                                        onCLick = {
                                            viewModel.onConfirmationItemClicked()
                                        },
                                        onYesClick = {
                                            viewModel.onAttendanceYesClicked(
                                                item,
                                                chatId
                                            )
                                        },
                                        onNoClick = {
                                            viewModel.onAttendanceNoClicked(item, chatId)
                                        }
                                    )
                                }

                                if ((item.showAttendance) && (item.attendance[viewModel.currentUserId] == ATTENDANCE_YES)) {
                                    ConfirmationResultItem(
                                        resultType = ATTENDANCE_YES,
                                        task = item,
                                        user = viewModel.currentUser.value,
                                        viewModel::onConfirmationItemClicked
                                    )
                                } else if (item.attendance[viewModel.currentUserId] == ATTENDANCE_NO) {
                                    ConfirmationResultItem(
                                        resultType = ATTENDANCE_NO,
                                        task = item,
                                        user = viewModel.currentUser.value,
                                        viewModel::onConfirmationItemClicked
                                    )
                                }
                                if ((item.showAttendance) &&
                                    (item.attendance[viewModel.chat.value.membersId.first { it != viewModel.currentUserId }]
                                            == ATTENDANCE_YES)
                                ) {
                                    ConfirmationResultItem(
                                        resultType = ATTENDANCE_YES,
                                        task = item,
                                        user = viewModel.partnerObject.value,
                                        viewModel::onConfirmationItemClicked
                                    )
                                } else if ((item.showAttendance) && (item.attendance[viewModel.chat.value.membersId.first { it != viewModel.currentUserId }] == ATTENDANCE_NO)) {
                                    ConfirmationResultItem(
                                        resultType = ATTENDANCE_NO,
                                        task = item,
                                        user = viewModel.partnerObject.value,
                                        viewModel::onConfirmationItemClicked
                                    )
                                }
                                // completion
                                if (item.showCompletion && !(item.attendance[viewModel.currentUserId] == ATTENDANCE_NO) && (item.completion[viewModel.currentUserId] == COMPLETION_NOT_DONE)) {
                                    ConfirmationItem(
                                        confirmationType = AlarmReceiver.COMPLETION,
                                        task = item,
                                        onCLick = {
                                            viewModel.onConfirmationItemClicked()
                                        },
                                        onYesClick = {
                                            viewModel.onCompletionYesClicked(
                                                item,
                                                chatId
                                            )
                                        },
                                        onNoClick = {
                                            viewModel.onCompletionNoClicked(item, chatId)
                                        }
                                    )
                                }

                                if ((item.showCompletion) && (item.completion[viewModel.currentUserId] == COMPLETION_YES)) {
                                    ConfirmationResultItem(
                                        resultType = COMPLETION_YES,
                                        task = item,
                                        user = viewModel.currentUser.value,
                                        viewModel::onConfirmationItemClicked
                                    )
                                } else if (item.completion[viewModel.currentUserId] == COMPLETION_NO) {
                                    ConfirmationResultItem(
                                        resultType = COMPLETION_NO,
                                        task = item,
                                        user = viewModel.currentUser.value,
                                        viewModel::onConfirmationItemClicked
                                    )
                                }
                                if ((item.showCompletion) &&
                                    (item.completion[viewModel.chat.value.membersId.first { it != viewModel.currentUserId }]
                                            == COMPLETION_YES)
                                ) {
                                    ConfirmationResultItem(
                                        resultType = COMPLETION_YES,
                                        task = item,
                                        user = viewModel.partnerObject.value,
                                        viewModel::onConfirmationItemClicked
                                    )
                                } else if ((item.showCompletion) && (item.completion[viewModel.chat.value.membersId.first { it != viewModel.currentUserId }] == COMPLETION_NO)) {
                                    ConfirmationResultItem(
                                        resultType = COMPLETION_NO,
                                        task = item,
                                        user = viewModel.partnerObject.value,
                                        viewModel::onConfirmationItemClicked
                                    )
                                }
                            }

                        }

                    }

                }



                MessageInputField(
                    R.string.message,
                    uiState.messageText,
                    viewModel::onMessageTextChange,
                    {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.onSendClick(chatId, focusManager)
                                    scrollToBottom()

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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(top = 18.dp, bottom = 8.dp)
                ) {

                    if (inProgressTasks.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.in_progress_tasks_sessions),
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                            )
                        }

                        items(inProgressTasks) { inProgressTask ->
                            TaskItem(task = inProgressTask)
                        }

                    }

                    if (inProgressTasks.isNotEmpty() && upcomingTasks.isNotEmpty() && previousTasks.isNotEmpty()) {
                        item {
                            Divider(
                                modifier = Modifier
                                    .padding(top = 32.dp, bottom = 20.dp)
                            )
                        }
                    }

                    if (upcomingTasks.isNotEmpty()) {

                        item {
                            Text(
                                text = stringResource(R.string.upcoming_tasks_sessions),
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                            )
                        }
                        items(upcomingTasks) { upcomingTask ->
                            TaskItem(task = upcomingTask)
                        }
                    }

                    // Display divider if needed
                    if (inProgressTasks.isNotEmpty() && upcomingTasks.isNotEmpty() && previousTasks.isNotEmpty()) {
                        item {
                            Divider(
                                modifier = Modifier
                                    .padding(top = 32.dp, bottom = 20.dp)
                            )
                        }
                    } else if (
                        (inProgressTasks.isNotEmpty() && upcomingTasks.isEmpty() && previousTasks.isNotEmpty())
                    ) {
                        item {
                            Divider(
                                modifier = Modifier
                                    .padding(top = 32.dp, bottom = 20.dp)
                            )
                        }
                    } else if (
                        (inProgressTasks.isEmpty() && upcomingTasks.isNotEmpty() && previousTasks.isNotEmpty())
                    ) {
                        item {
                            Divider(
                                modifier = Modifier
                                    .padding(top = 32.dp, bottom = 20.dp)
                            )
                        }
                    }

                    if (previousTasks.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.previous_tasks_sessions),
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                            )
                        }

                        items(previousTasks) { previousTask ->
                            TaskItem(task = previousTask)
                        }

                    }

                    item {
                        Spacer(modifier = Modifier.padding(32.dp))
                    }

                }

            }
        }

    }
}


