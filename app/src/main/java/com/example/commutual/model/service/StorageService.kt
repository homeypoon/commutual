

package com.example.commutual.model.service

import com.example.commutual.model.*
import kotlinx.coroutines.flow.Flow

interface StorageService {
  val posts: Flow<List<Post>>
  val userPosts: Flow<List<Post>>
  val chats: Flow<List<Chat>>
  val upcomingUserTasks: Flow<List<Task>>
  val chatsWithUsers: Flow<List<Pair<Chat, User>>>
  fun getMessagesWithUsers(chatId: String): Flow<List<Pair<Message, User>>>

  fun getTasksWithUsers(chatId: String): Flow<Pair<List<Pair<Task, User>>, List<Pair<Task, User>>>>
  fun getCompletedTasksWithUsers(chatId: String): Flow<List<Pair<Task, User>>>


  suspend fun searchedPosts(search: String): Flow<List<Post>>
  suspend fun filteredPosts(search: String, category: CategoryEnum): Flow<List<Post>>


//  val messages: Flow<List<Message>>

  // Getter methods
  suspend fun getUser(userId: String): User?
  suspend fun getPartner(membersId: MutableList<String>): User?
  suspend fun getChatWithChatId(chatId: String): Chat?
  suspend fun getChatWithPostUserId(postUserId: String): Chat?



  // User methods
  suspend fun saveUser(userId: String, user: User)
  suspend fun updateUser(user: User)
  suspend fun updateCurrentUser(chatId: String, taskId: String)
  suspend fun deleteAllForUser(userId: String)
  suspend fun incrementCommitCount(incrementCommitCount: Long)
  suspend fun incrementTasksScheduled(membersId: Array<String>)
  suspend fun incrementTasksCompleted()

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

  suspend fun saveChat(chat: Chat): String

  suspend fun saveMessage(message: Message, chatId: String): String
  suspend fun updateMessage(message: Message, chatId: String)

  // return list of filtered posts based on interest

  suspend fun hasProfile(): Boolean


}
