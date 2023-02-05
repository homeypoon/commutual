/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.commutual.model.service.impl

import com.example.commutual.model.Chat
import com.example.commutual.model.Message
import com.example.commutual.model.Post
import com.example.commutual.model.User
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.StorageService
import com.example.commutual.model.service.trace
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService) :
    StorageService {


    @OptIn(ExperimentalCoroutinesApi::class)
    override val posts: Flow<List<Post>>
        get() =
            auth.currentUser.flatMapLatest {
                currentPostCollection().snapshots().map { snapshot -> snapshot.toObjects() }
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userPosts: Flow<List<Post>>
        get() =
            auth.currentUser.flatMapLatest {
                currentPostCollection().whereEqualTo(USER_ID, auth.currentUserId).snapshots()
                    .map { snapshot -> snapshot.toObjects() }
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val chats: Flow<List<Chat>>
        get() =
            auth.currentUser.flatMapLatest {
                currentChatCollection().whereArrayContains(MEMBERS_ID_FIELD, auth.currentUserId).snapshots()
                    .map { snapshot -> snapshot.toObjects() }
            }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMessages(chatId: String): Flow<List<Message>> {
        return auth.currentUser.flatMapLatest {
            currentMessageCollection(chatId)
                .snapshots().map { snapshot -> snapshot.toObjects() }
        }
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    override val messages: Flow<List<Message>>
//        get() =
//            auth.currentUser.flatMapLatest {
//                currentMessageCollection(chatId).whereArrayContains(MEMBERS_ID_FIELD, auth.currentUserId).snapshots()
//                    .map { snapshot -> snapshot.toObjects() }
//            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun filteredPosts(interest: String): Flow<List<Post>> {
        return auth.currentUser.flatMapLatest {
            currentPostCollection().whereArrayContains(INTERESTS_FIELD, interest)
                .snapshots().map { snapshot -> snapshot.toObjects() }
        }
    }



//    currentMessageCollection(chatId).document(messageId).get().await().toObject()




    override suspend fun deletePost(postId: String) {
        currentPostCollection().document(postId).delete().await()
    }

    override suspend fun getUser(userId: String): User? =
        currentUserCollection().document(userId).get().await().toObject()

    // save user and generate an sender for the user document
    override suspend fun saveUser(userId: String, user: User): Unit =
        trace(SAVE_POST_TRACE) { currentUserCollection().document(userId).set(user).await() }

    override suspend fun updateUser(user: User): Unit =
        trace(UPDATE_POST_TRACE) {
            currentUserCollection().document(auth.currentUserId).set(user).await()
        }

    override suspend fun getPost(postId: String): Post? =
        currentPostCollection().document(postId).get().await().toObject()

    // save user and generate an postId for the user document
    override suspend fun savePost(post: Post): String =
        trace(SAVE_POST_TRACE) { currentPostCollection().add(post).await().id }

    override suspend fun updatePost(post: Post): Unit =
        trace(UPDATE_POST_TRACE) {
            currentPostCollection().document(post.postId).set(post).await()
        }

    // save user and generate an sender for the user document
    override suspend fun saveChat(chat: Chat): String =
        trace(SAVE_POST_TRACE) { currentChatCollection().add(chat).await().id }

    override suspend fun saveMessage(message: Message, chatId: String): String =
        trace(SAVE_POST_TRACE) { currentMessageCollection(chatId).add(message).await().id }

    override suspend fun updateMessage(message: Message, chatId: String): Unit =
        trace(UPDATE_POST_TRACE) {
            currentMessageCollection(chatId).document(message.messageId).set(message).await()
        }

    override suspend fun hasProfile(): Boolean =
        trace(UPDATE_POST_TRACE) {
            val userRef = currentUserCollection().document(auth.currentUserId).get().await()
            return userRef.exists()
        }


    // TODO: It's not recommended to delete on the client:
    // https://firebase.google.com/docs/firestore/manage-data/delete-data#kotlin+ktx_2
    override suspend fun deleteAllForUser(userId: String) {
        val matchingPosts = currentPostCollection().get().await()
        matchingPosts.map { it.reference.delete().asDeferred() }.awaitAll()
    }

    private fun currentUserCollection(): CollectionReference =
        firestore.collection(USER_COLLECTION)

    private fun currentPostCollection(): CollectionReference =
        firestore.collection(POST_COLLECTION)

    private fun currentChatCollection(): CollectionReference =
        firestore.collection(CHAT_COLLECTION)

    private fun currentMessageCollection(chatId: String): CollectionReference =
        firestore.collection(CHAT_COLLECTION).document(chatId).collection(MESSAGE_COLLECTION)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val POST_COLLECTION = "posts"
        private const val SAVE_POST_TRACE = "savePost"
        private const val UPDATE_POST_TRACE = "updatePost"
        private const val USER_ID = "userId"
        private const val MESSAGE_ID = "messageId"
        private const val USERNAME = "username"

        private const val INTERESTS_FIELD = "interests"
        private const val MEMBERS_ID_FIELD = "membersId"

        private const val CHAT_COLLECTION = "chats"

        private const val MESSAGE_COLLECTION = "messages"
    }

}