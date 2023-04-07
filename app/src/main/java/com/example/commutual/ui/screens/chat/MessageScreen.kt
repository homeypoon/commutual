package com.example.commutual.ui.screens.chat

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
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
import com.example.commutual.ui.screens.item.BottomSheetOptionItem
import com.example.commutual.ui.screens.item.TaskItem
import com.example.commutual.ui.screens.item.message_type.ImageItem
import com.example.commutual.ui.screens.item.message_type.ImageMessageItem
import com.example.commutual.ui.screens.item.message_type.MessageItem
import com.example.commutual.ui.screens.item.task_reminder.ConfirmationItem
import com.example.commutual.ui.screens.item.task_reminder.ConfirmationResultItem
import com.example.commutual.ui.screens.item.task_reminder.CreatedTaskItem
import com.example.commutual.ui.screens.profile_post.PostDetailsBottomSheetOption
import com.example.commutual.ui.screens.profile_post.ReportBottomSheetOption
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class,
    ExperimentalMaterialApi::class
)
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

    val postBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val reportBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val postBottomSheetOptions = listOf(
        PostDetailsBottomSheetOption.Report
    )

    val reportBottomSheetOptions = listOf(
        ReportBottomSheetOption.Spam,
        ReportBottomSheetOption.Nudity,
        ReportBottomSheetOption.Hate,
        ReportBottomSheetOption.FalseInformation,
        ReportBottomSheetOption.Scam,
    )

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.setPhotoUri(uri)
        }


    val tasks by viewModel.getTasks(chatId).collectAsStateWithLifecycle(emptyList())
    val upcomingTasks = tasks.filter { !it.showAttendance && !it.showCompletion }
    val inProgressTasks =
        tasks.filter { it.showAttendance && !it.showCompletion && (it.attendance[viewModel.currentUserId] != ATTENDANCE_NO) }
    val previousTasks =
        tasks.filter { (it.showCompletion) || (it.attendance[viewModel.currentUserId] == ATTENDANCE_NO) }


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


    ModalBottomSheetLayout(sheetState = postBottomSheetState, sheetContent = {
        LazyColumn {
            items(postBottomSheetOptions) {
                androidx.compose.material.Surface(modifier = Modifier.clickable {
                    when (it) {
                        PostDetailsBottomSheetOption.Report -> viewModel.onReportPostClick(
                            coroutineScope, postBottomSheetState, reportBottomSheetState
                        )
                    }
                }) {
                    BottomSheetOptionItem(it)
                }
            }
        }
    }) {
        ModalBottomSheetLayout(sheetState = reportBottomSheetState, sheetContent = {
            LazyColumn {
                items(reportBottomSheetOptions) {
                    androidx.compose.material.Surface(modifier = Modifier.clickable {
                        when (it) {
                            ReportBottomSheetOption.Spam -> viewModel.onReportItemPostClick(
                                coroutineScope, it, reportBottomSheetState
                            )
                            ReportBottomSheetOption.Nudity -> viewModel.onReportItemPostClick(
                                coroutineScope, it, reportBottomSheetState
                            )
                            ReportBottomSheetOption.Hate -> viewModel.onReportItemPostClick(
                                coroutineScope, it, reportBottomSheetState
                            )
                            ReportBottomSheetOption.FalseInformation -> viewModel.onReportItemPostClick(
                                coroutineScope, it, reportBottomSheetState
                            )
                            ReportBottomSheetOption.Scam -> viewModel.onReportItemPostClick(
                                coroutineScope, it, reportBottomSheetState
                            )
                        }
                    }) {
                        BottomSheetOptionItem(it)
                    }
                }
            }
        }) {
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
                                    Icons.Filled.Add,
                                    "Add",
                                    tint = MaterialTheme.colorScheme.onTertiary
                                )
                            },
                            onClick = {
                                viewModel.onAddTaskClick(openScreen)
                            },
                            modifier = modifier.padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }) { padding ->

                Column(modifier = Modifier
                    .padding(padding)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }) {
                    CenterAlignedTopAppBar(title = {
                        Text(uiState.partner.username,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier.clickable {
                                viewModel.onUsernameClick(openScreen)
                            })
                    }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        MaterialTheme.colorScheme.secondary
                    ), actions = {
                        androidx.compose.material.IconButton(onClick = {
                            viewModel.onIconClick(
                                coroutineScope, postBottomSheetState
                            )
                        }

                        ) {
                            androidx.compose.material.Icon(
                                painter = painterResource(R.drawable.ic_vertical_dots),
                                contentDescription = "Action",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    })
                    Divider(
                        thickness = 2.dp, color = MaterialTheme.colorScheme.onSecondary
                    )

                    TabRow(selectedTabIndex = viewModel.tabIndex) {
                        viewModel.messageTabs.forEachIndexed { index, tab ->
                            Tab(selected = viewModel.tabIndex == index,
                                onClick = { viewModel.setMessageTab(index) },
                                text = {
                                    Text(
                                        text = stringResource(tab.tabStringRes),
                                        style = MaterialTheme.typography.headlineLarge,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                })
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
                                    // if the item is a message, display message-only, image-only, and
                                    // message-image item accordingly

                                    if (item is Message) {
                                        when (item.type) {
                                            Message.TYPE_MESSAGE_ONLY -> {
                                                // message-only item
                                                MessageItem(item, user) { timestamp ->
                                                    FormatterClass.formatTimestamp(
                                                        timestamp, true
                                                    )
                                                }
                                            }
                                            Message.TYPE_IMAGE_ONLY -> {
                                                // image-only item
                                                ImageItem(item, user) { timestamp ->
                                                    FormatterClass.formatTimestamp(
                                                        timestamp, true
                                                    )
                                                }
                                            }
                                            Message.TYPE_IMAGE_MESSAGE -> {
                                                // image + message item
                                                ImageMessageItem(item, user) { timestamp ->
                                                    FormatterClass.formatTimestamp(
                                                        timestamp, true
                                                    )
                                                }
                                            }
                                        }

                                    } else if (item is Task) {
                                        // if the item is a task, display the appropriate task-related item

                                        if (!item.showAttendance) {
                                            CreatedTaskItem(
                                                item, user, viewModel::onCreatedTaskCLicked
                                            )
                                        }

                                        if (item.showAttendance && (item.attendance[viewModel.currentUserId] == ATTENDANCE_NOT_DONE)) {
                                            ConfirmationItem(confirmationType = AlarmReceiver.ATTENDANCE,
                                                task = item,
                                                onCLick = {
                                                    viewModel.onConfirmationItemClicked()
                                                },
                                                onYesClick = {
                                                    viewModel.onAttendanceYesClicked(
                                                        item, chatId
                                                    )
                                                },
                                                onNoClick = {
                                                    viewModel.onAttendanceNoClicked(item, chatId)
                                                })
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
                                        if ((item.showAttendance) && (item.attendance[viewModel.chat.value.membersId.first { it != viewModel.currentUserId }] == ATTENDANCE_YES)) {
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
                                        if (item.showCompletion && item.attendance[viewModel.currentUserId] != ATTENDANCE_NO && (item.completion[viewModel.currentUserId] == COMPLETION_NOT_DONE)) {
                                            ConfirmationItem(confirmationType = AlarmReceiver.COMPLETION,
                                                task = item,
                                                onCLick = {
                                                    viewModel.onConfirmationItemClicked()
                                                },
                                                onYesClick = {
                                                    viewModel.onCompletionYesClicked(
                                                        item, chatId
                                                    )
                                                },
                                                onNoClick = {
                                                    viewModel.onCompletionNoClicked(item, chatId)
                                                })
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
                                        if ((item.showCompletion) && (item.completion[viewModel.chat.value.membersId.first { it != viewModel.currentUserId }] == COMPLETION_YES)) {
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


                        Column(modifier = Modifier.fillMaxWidth()) {
                            if (viewModel.photoUri != null) {

                                Divider(modifier = Modifier.padding(top = 2.dp, bottom = 10.dp))


                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                        .padding(horizontal = 48.dp)
                                ) {


                                    //Use Coil to display the selected image
                                    val painter = rememberAsyncImagePainter(
                                        ImageRequest.Builder(LocalContext.current)
                                            .data(data = viewModel.photoUri).build()
                                    )


                                    Image(
                                        painter = painter,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(120.dp, 150.dp)
                                    )

                                    Box(
                                        contentAlignment = Alignment.TopEnd,
                                        modifier = Modifier.size(120.dp, 150.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { viewModel.onCloseImageClick(focusManager) },
                                            modifier = Modifier
                                                .size(28.dp)
                                                .padding(top = 4.dp, end = 4.dp),
                                            shape = CircleShape,
                                            contentPadding = PaddingValues(0.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = MaterialTheme.colorScheme.secondary,
                                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                                            )
                                        ) {
                                            Icon(
                                                painterResource(R.drawable.ic_close),
                                                contentDescription = "Close image",
                                                tint = Color.Black,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }



                            MessageInputField(
                                R.string.message,
                                uiState.messageText,
                                viewModel::onMessageTextChange,
                                {
                                    if (viewModel.photoUri == null) {
                                        IconButton(onClick = {
                                            viewModel.onAddImageClick(launcher, focusManager)
                                            coroutineScope.launch {
                                                scrollToBottom()
                                            }
                                        }, content = {

                                            Icon(
                                                painter = painterResource(R.drawable.ic_add),
                                                contentDescription = stringResource(R.string.send)
                                            )
                                        }


                                        )
                                    }
                                },
                                {
                                    IconButton(onClick = {
                                        coroutineScope.launch {
                                            viewModel.onSendClick(chatId, focusManager)
                                            scrollToBottom()
                                        }
                                    }, content = {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_send),
                                            contentDescription = stringResource(R.string.send)
                                        )
                                    })
                                },
                                4,
                                Modifier
                                    .padding(2.dp, 8.dp)
                                    .fillMaxWidth(),
                                focusManager
                            )
                        }


                    } else {
                        // Tasks Tab

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
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
                                        modifier = Modifier.padding(top = 32.dp, bottom = 20.dp)
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
                                        modifier = Modifier.padding(top = 32.dp, bottom = 20.dp)
                                    )
                                }
                            } else if ((inProgressTasks.isNotEmpty() && upcomingTasks.isEmpty() && previousTasks.isNotEmpty())) {
                                item {
                                    Divider(
                                        modifier = Modifier.padding(top = 32.dp, bottom = 20.dp)
                                    )
                                }
                            } else if ((inProgressTasks.isEmpty() && upcomingTasks.isNotEmpty() && previousTasks.isNotEmpty())) {
                                item {
                                    Divider(
                                        modifier = Modifier.padding(top = 32.dp, bottom = 20.dp)
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
    }

}


