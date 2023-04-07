

package com.example.commutual.model.service.impl

import com.example.commutual.model.User
import com.example.commutual.model.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) : AccountService {

  // Get current UserId
  override val currentUserId: String
    get() = auth.currentUser?.uid.orEmpty()

  // Get whether FireStore has current user
  override val hasUser: Boolean
    get() = auth.currentUser != null


  override val currentUser: Flow<User>
    get() = callbackFlow {
      val listener =
        FirebaseAuth.AuthStateListener { auth ->
          launch {
            try {
              auth.currentUser?.let { user ->
                val docRef = firestore.collection("users").document(user.uid)
                val document = docRef.get().await()
                if (document.exists()) {
                  val user = document.toObject(User::class.java)
                  user?.let { this@callbackFlow.trySend(it) }
                } else {
                  this@callbackFlow.trySend(User(user.uid))
                }
              }
            } catch (e: Exception) {
              // handle the error here, e.g. log it or show a toast message
            }
          }
        }
      auth.addAuthStateListener(listener)
      awaitClose { auth.removeAuthStateListener(listener) }
    }

  override suspend fun sendRecoveryEmail(email: String) {
    auth.sendPasswordResetEmail(email).await()
  }

  override suspend fun createAnonymousAccount() {
    auth.signInAnonymously().await()
  }

  override suspend fun createAccount(email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password).await()
  }

  override suspend fun signIn(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password).await()
  }

  override suspend fun deleteAccount() {
    auth.currentUser!!.delete().await()
  }

  override suspend fun signOut() {
    auth.signOut()
  }

}