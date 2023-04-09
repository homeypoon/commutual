/**
 * This interface defines methods for interacting with Firebase Firestore, Storage, and Authentication.
 */

package com.example.commutual.model.service

import android.net.Uri
import com.example.commutual.model.*
import kotlinx.coroutines.flow.Flow

interface StorageService {
    // Getter methods
    val posts: Flow<List<Post>>
    val chats: Flow<List<Chat>>
    val currentUserTasks: Flow<List<Task>>
    val chatsWithUsers: Flow<List<Pair<Chat, User>>>
    fun getFlowCurrentUser(): Flow<User?>
    fun getUserPosts(userId: String): Flow<List<Post>>
    fun getMessagesWithUsers(chatId: String): Flow<List<Pair<Message, User>>>
    fun getMessagesAndTasksWithUsers(chatId: String): Flow<List<Pair<Any, User>>>
    fun getAllTasksWithUsers(chatId: String): Flow<List<Pair<Task, User>>>
    fun getTasks(chatId: String): Flow<List<Task>>
    suspend fun getUser(userId: String): User?
    suspend fun getPartner(membersId: MutableList<String>): User?
    suspend fun getChatWithChatId(chatId: String): Chat?
    suspend fun getChatWithPostUserId(postUserId: String): Chat?
    fun getCompletedTasksWithUsers(chatId: String): Flow<List<Pair<Task, User>>>
    suspend fun searchedPosts(search: String): Flow<List<Post>>
    suspend fun filteredPosts(search: String, category: CategoryEnum): Flow<List<Post>>


    // User methods
    suspend fun saveUser(userId: String, user: User)
    suspend fun updateUser(user: User)
    suspend fun updateCurrentUserTaskMap(chatId: String, taskId: String)
    suspend fun deleteAllForUser(userId: String)
    suspend fun hasProfile(): Boolean


    // Post methods
    suspend fun getPost(postId: String): Post?
    suspend fun savePost(post: Post): String
    suspend fun updatePost(post: Post)
    suspend fun deletePost(postId: String)

    // Task methods
    suspend fun getTask(taskId: String, chatId: String): Task?
    suspend fun saveTask(task: Task, chatId: String): String
    suspend fun updateTask(task: Task, chatId: String)
    suspend fun updateTaskType(task: Task, chatId: String, attendanceType: Int?)
    suspend fun updateTaskAM(task: Task, chatId: String, updateType: Int)
    suspend fun deleteTask(taskId: String, chatId: String)

    // Chat Methods
    suspend fun saveChat(chat: Chat): String

    // Message Methods
    suspend fun saveMessage(message: Message, chatId: String): String
    suspend fun saveImageMessage(message: Message, chatId: String, imageUri: Uri)

    suspend fun updateMessage(message: Message, chatId: String)

    // Report Methods
    suspend fun saveReport(report: Report): String


    // Increment Methods
    suspend fun incrementCategoryCount(membersId: Array<String>, category: CategoryEnum)
    suspend fun incrementCommitCount(incrementCommitCount: Long)
    suspend fun incrementTasksScheduled(membersId: Array<String>)
    suspend fun incrementTasksCompleted()
    suspend fun incrementTasksMissed()

}
