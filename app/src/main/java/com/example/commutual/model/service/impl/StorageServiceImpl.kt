/**
 * This file manages the usage of Firebase Firestore, Storage
 */

package com.example.commutual.model.service.impl

import android.net.Uri
import com.example.commutual.model.*
import com.example.commutual.model.AlarmReceiver.Companion.ATTENDANCE
import com.example.commutual.model.AlarmReceiver.Companion.COMPLETION
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.StorageService
import com.example.commutual.model.service.trace
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl
@Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService,
    private var storage: FirebaseStorage
) :
    StorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val posts: Flow<List<Post>>
        get() = auth.currentUser.flatMapLatest {
            currentPostCollection().snapshots().map { snapshot ->
                snapshot.toObjects<Post>().filter { it.userId != auth.currentUserId }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserPosts(userId: String): Flow<List<Post>> {
        return auth.currentUser.flatMapLatest {
            currentPostCollection().whereEqualTo(USER_ID, userId).snapshots()
                .map { snapshot -> snapshot.toObjects() }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val chats: Flow<List<Chat>>
        get() = auth.currentUser.flatMapLatest {
            currentChatCollection().whereArrayContains(MEMBERS_ID_FIELD, auth.currentUserId)
                .snapshots().map { snapshot -> snapshot.toObjects() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTasks(chatId: String): Flow<List<Task>> {
        return auth.currentUser.flatMapLatest {
            currentTaskCollection(chatId).orderBy(CREATE_TIMESTAMP_FIELD, Query.Direction.ASCENDING)
                .snapshots().map { snapshot ->
                    snapshot.toObjects()

                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val currentUserTasks: Flow<List<Task>>
        get() = auth.currentUser.flatMapLatest {
            flow {
                val tasksList = mutableListOf<Task>()
                val currentUser = getUserById(auth.currentUserId)
                currentUser.tasksMap.forEach { (taskId, chatId) ->
                    val task = getTask(taskId, chatId)
                    if (task != null) {
                        tasksList.add(task)
                    }
                }
                emit(tasksList)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val chatsWithUsers: Flow<List<Pair<Chat, User>>> // Return a flow of Chat-User pairs
        get() = auth.currentUser.flatMapLatest {
            currentChatCollection().whereArrayContains(MEMBERS_ID_FIELD, auth.currentUserId)
                .snapshots().map { snapshot ->
                    snapshot.toObjects<Chat>().map { chat ->
                        val otherUserId =
                            chat.membersId.first { it != auth.currentUserId } // Get the ID of the other user
                        val otherUser = getUserById(otherUserId) // Get the User object using the ID
                        chat to otherUser // Return a Pair of Chat and User objects
                    }
                }
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFlowCurrentUser(): Flow<User?> {
        return auth.currentUser.flatMapLatest {
            currentUserCollection().document(auth.currentUserId)
                .snapshots().map { snapshot ->
                    snapshot.toObject<User>()
                }
        }
    }


    override suspend fun getChatWithChatId(chatId: String): Chat? =
        currentChatCollection().document(chatId).get().await().toObject()

    override suspend fun getPartner(membersId: MutableList<String>): User? =
        getUser(membersId.first { it != auth.currentUserId })


    private suspend fun getUserById(userId: String): User {
        val userDoc = currentUserCollection().document(userId).get().await()
        return userDoc.toObject<User>() ?: throw IllegalStateException("User $userId not found")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMessagesWithUsers(chatId: String): Flow<List<Pair<Message, User>>> {
        return auth.currentUser.flatMapLatest {
            currentMessageCollection(chatId).orderBy(TIMESTAMP_FIELD, Query.Direction.ASCENDING)
                .snapshots().map { snapshot ->
                    snapshot.toObjects<Message>().map { message ->
                        val sender =
                            getUserById(message.senderId) // Get the User object using the sender's id
                        message to sender // Return a Pair of Chat and User objects
                    }
                }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllTasksWithUsers(chatId: String): Flow<List<Pair<Task, User>>> {
        return auth.currentUser.flatMapLatest {

            currentTaskCollection(chatId).orderBy(CREATE_TIMESTAMP_FIELD, Query.Direction.ASCENDING)
                .snapshots().map { snapshot ->
                    snapshot.toObjects<Task>().map { task ->
                        val sender =
                            getUserById(task.creatorId)
                        task to sender
                    }
                }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCompletedTasksWithUsers(chatId: String): Flow<List<Pair<Task, User>>> {
        return auth.currentUser.flatMapLatest {
            currentTaskCollection(chatId).whereEqualTo(COMPLETED_FIELD, true)
                .orderBy(TIMESTAMP_FIELD, Query.Direction.ASCENDING).snapshots().map { snapshot ->
                    snapshot.toObjects<Task>().map { task ->
                        val sender = getUserById(task.creatorId)
                        task to sender
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMessagesAndTasksWithUsers(chatId: String): Flow<List<Pair<Any, User>>> {
        val messagesFlow = getMessagesWithUsers(chatId)
        val tasksFlow = getAllTasksWithUsers(chatId)

        return messagesFlow
            .flatMapLatest { messages ->
                tasksFlow.map { tasks ->

                    val combinedList = (messages + tasks).map { Pair(it.first, it.second) }
                    combinedList.sortedBy { pair ->
                        when (pair.first) {
                            is Message -> (pair.first as Message).timestamp
                            is Task -> (pair.first as Task).createTimestamp
                            else -> throw IllegalArgumentException("Invalid object type")
                        }.toDate()
                    }
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun filteredPosts(search: String, category: CategoryEnum): Flow<List<Post>> {

        return posts.transformLatest { posts ->
            emit(posts.filter { post ->
                post.category == category && (post.title.contains(
                    search,
                    ignoreCase = true
                ) || post.description.contains(search, ignoreCase = true))
            })
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun searchedPosts(search: String): Flow<List<Post>> {

        return posts.transformLatest { posts ->
            emit(posts.filter { post ->
                post.title.contains(search, ignoreCase = true) || post.description.contains(
                    search,
                    ignoreCase = true
                )
            })
        }
    }


    override suspend fun getUser(userId: String): User? =
        currentUserCollection().document(userId).get().await().toObject()


    // save user and generate an sender for the user document
    override suspend fun saveUser(userId: String, user: User): Unit =
        trace(SAVE_USER_TRACE) { currentUserCollection().document(userId).set(user).await() }

    override suspend fun updateUser(user: User): Unit = trace(UPDATE_USER_TRACE) {
        currentUserCollection().document(auth.currentUserId).set(user).await()
    }


    override suspend fun updateCurrentUser(chatId: String, taskId: String): Unit =
        trace(UPDATE_CURRENT_USER_TRACE) {
            val tasksMap = mapOf("tasksMap.${taskId}" to chatId)
            currentUserCollection().document(auth.currentUserId).update(tasksMap).await()
        }

    override suspend fun incrementCategoryCount(membersId: Array<String>, category: CategoryEnum) {
        val userRef = currentUserCollection().document(auth.currentUserId)
        userRef.update("categoryCount.${category.name}", FieldValue.increment(1))
    }

    override suspend fun incrementCommitCount(incrementCommitCount: Long) {
        val userRef = currentUserCollection().document(auth.currentUserId)
        userRef.update("commitCount", FieldValue.increment(incrementCommitCount))
    }

    override suspend fun incrementTasksScheduled(membersId: Array<String>) {

        for (memberId in membersId) {
            val userRef = currentUserCollection().document(memberId)
            userRef.update("tasksScheduled", FieldValue.increment(1))
        }
    }

    override suspend fun incrementTasksCompleted() {
        val userRef = currentUserCollection().document(auth.currentUserId)
        userRef.update("tasksCompleted", FieldValue.increment(1))
    }

    override suspend fun incrementTasksMissed() {
        val userRef = currentUserCollection().document(auth.currentUserId)
        userRef.update("tasksMissed", FieldValue.increment(1))
    }

    override suspend fun getPost(postId: String): Post? =
        currentPostCollection().document(postId).get().await().toObject()

    override suspend fun savePost(post: Post): String =
        trace(SAVE_POST_TRACE) { currentPostCollection().add(post).await().id }

    override suspend fun updatePost(post: Post): Unit = trace(UPDATE_POST_TRACE) {
        currentPostCollection().document(post.postId).set(post).await()
    }

    override suspend fun deletePost(postId: String) {
        currentPostCollection().document(postId).delete().await()
    }

    override suspend fun getTask(taskId: String, chatId: String): Task? =
        currentTaskCollection(chatId).document(taskId).get().await().toObject()

    override suspend fun saveTask(task: Task, chatId: String): String = trace(SAVE_MESSAGE_TRACE) {
        currentTaskCollection(chatId).add(
            task.copy(
                createTimestamp = Timestamp.now()
            )
        ).await().id

    }


    override suspend fun updateTask(task: Task, chatId: String): Unit =
        trace(UPDATE_TASK) {
            currentTaskCollection(chatId).document(task.taskId).set(task).await()
        }

    override suspend fun updateTaskType(task: Task, chatId: String, attendanceType: Int?): Unit =
        trace(UPDATE_TASK_TRACE) {

            if (attendanceType != null) {

                if (attendanceType == Task.ATTENDANCE_YES || attendanceType == Task.ATTENDANCE_NO) {
                    currentTaskCollection(chatId).document(task.taskId).set(
                        task.copy(
                            creatorId = auth.currentUserId,
                            showAttendanceTimestamp = Timestamp.now(),
                            createTimestamp = Timestamp.now()
                        )
                    ).await()

                    val attendanceMap = mapOf("attendance.${auth.currentUserId}" to attendanceType)

                    currentTaskCollection(chatId).document(task.taskId).update(attendanceMap)
                } else if (attendanceType == Task.COMPLETION_YES || attendanceType == Task.COMPLETION_NO) {
                    currentTaskCollection(chatId).document(task.taskId).set(
                        task.copy(
                            creatorId = auth.currentUserId,
                            showCompletionTimestamp = Timestamp.now(),
                            createTimestamp = Timestamp.now()
                        )
                    ).await()

                    val completionMap = mapOf("completion.${auth.currentUserId}" to attendanceType)
                    currentTaskCollection(chatId).document(task.taskId).update(completionMap)

                }


            } else {
                currentTaskCollection(chatId).document(task.taskId).set(
                    task.copy(
                        createTimestamp = Timestamp.now(), creatorId = auth.currentUserId
                    )
                ).await()
            }

        }

    /**
     * Update task fields based on update type from the Alarm Manager
     */
    override suspend fun updateTaskAM(task: Task, chatId: String, updateType: Int) {
        trace(UPDATE_TASK_TRACE_AM) {
            when (updateType) {
                ATTENDANCE -> {
                    currentTaskCollection(chatId).document(task.taskId).update(
                        "showAttendanceTimestamp", Timestamp.now(),
                        "createTimestamp", Timestamp.now(),
                        "showAttendance", true
                    ).await()
                }
                COMPLETION -> {
                    currentTaskCollection(chatId).document(task.taskId).update(
                        "showCompletionTimestamp",
                        Timestamp.now(),
                        "createTimestamp", Timestamp.now(),
                        "showCompletion", true,
                        "taskCompleted", true
                    ).await()
                }
                else -> {}
            }
        }
    }

    override suspend fun deleteTask(taskId: String, chatId: String) {
        currentTaskCollection(chatId).document(taskId).delete().await()
    }

    // save user and generate an sender for the user document
    override suspend fun saveChat(chat: Chat): String =
        trace(SAVE_CHAT_TRACE) { currentChatCollection().add(chat).await().id }


    override suspend fun getChatWithPostUserId(postUserId: String): Chat? {

        val membersId = mutableListOf(auth.currentUserId, postUserId)

        return currentChatCollection().whereIn(
            MEMBERS_ID_FIELD,
            listOf(membersId, membersId.reversed())
        )
            .get().await().toObjects<Chat>().firstOrNull()
    }

    override suspend fun saveMessage(message: Message, chatId: String): String =
        trace(SAVE_MESSAGE_TRACE) {
            currentMessageCollection(chatId).add(
                message.copy(
                    timestamp = Timestamp.now(), senderId = auth.currentUserId
                )
            ).await().id
        }

    override suspend fun saveImageMessage(message: Message, chatId: String, imageUri: Uri) {

        val imageRef = storage.reference
            .child("images/${chatId}/${message.messageId}/${imageUri.lastPathSegment}")

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.await()

        val downloadUrl = imageRef.downloadUrl.await()

        trace(SAVE_IMAGE_MESSAGE_TRACE) {

            currentMessageCollection(chatId).add(
                message.copy(
                    photoUri = downloadUrl.toString(),
                    timestamp = Timestamp.now(), senderId = auth.currentUserId
                )
            ).await().id
        }

    }

    override suspend fun updateMessage(message: Message, chatId: String): Unit =
        trace(UPDATE_MESSAGE_TRACE) {
            currentMessageCollection(chatId).document(message.messageId).set(message).await()
        }

    override suspend fun saveReport(report: Report): String =
        trace(SAVE_POST_TRACE) {
            currentReportCollection()
                .add(report.copy(reporterUserId = auth.currentUserId))
                .await().id
        }

    override suspend fun hasProfile(): Boolean = trace(HAS_PROFILE_TRACE) {
        val userRef = currentUserCollection().document(auth.currentUserId).get().await()
        return userRef.exists()
    }

    override suspend fun deleteAllForUser(userId: String) {
        val matchingPosts = currentPostCollection().get().await()
        matchingPosts.map { it.reference.delete().asDeferred() }.awaitAll()
    }


    private fun currentUserCollection(): CollectionReference =
        firestore.collection(USER_COLLECTION)

    private fun currentPostCollection(): CollectionReference =
        firestore.collection(POST_COLLECTION)

    private fun currentReportCollection(): CollectionReference =
        firestore.collection(REPORT_COLLECTION)

    private fun currentTaskCollection(chatId: String): CollectionReference =
        firestore.collection(CHAT_COLLECTION).document(chatId).collection(TASK_COLLECTION)

    private fun currentChatCollection(): CollectionReference =
        firestore.collection(CHAT_COLLECTION)

    private fun currentMessageCollection(chatId: String): CollectionReference =
        firestore.collection(CHAT_COLLECTION).document(chatId).collection(MESSAGE_COLLECTION)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val POST_COLLECTION = "posts"
        private const val REPORT_COLLECTION = "reports"
        private const val TASK_COLLECTION = "tasks"
        private const val CHAT_COLLECTION = "chats"
        private const val MESSAGE_COLLECTION = "messages"

        private const val SAVE_POST_TRACE = "savePost"
        private const val UPDATE_POST_TRACE = "updatePost"
        private const val SAVE_TASK_TRACE = "saveTask"
        private const val UPDATE_TASK = "updateTask"
        private const val UPDATE_TASK_TRACE = "updateTaskType"
        private const val UPDATE_TASK_TRACE_AM = "updateTaskAM"

        private const val SAVE_USER_TRACE = "saveUser"
        private const val UPDATE_USER_TRACE = "updateUser"
        private const val UPDATE_CURRENT_USER_TRACE = "updateCurrentUser"


        private const val SAVE_CHAT_TRACE = "saveChat"
        private const val SAVE_MESSAGE_TRACE = "saveMessage"
        private const val SAVE_IMAGE_MESSAGE_TRACE = "saveImageMessage"

        private const val UPDATE_MESSAGE_TRACE = "updateMessage"

        private const val HAS_PROFILE_TRACE = "hasProfile"

        private const val USER_ID = "userId"

        private const val MEMBERS_ID_FIELD = "membersId"
        private const val TIMESTAMP_FIELD = "timestamp"
        private const val CREATE_TIMESTAMP_FIELD = "createTimestamp"
        private const val COMPLETED_FIELD = "taskCompleted"
    }

}