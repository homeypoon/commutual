

package com.example.commutual.model.service.impl

import com.example.commutual.model.User
import com.example.commutual.model.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AccountService {

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
          this.trySend(auth.currentUser?.let { User(it.uid) } ?: User())
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

  companion object {
    private const val LINK_ACCOUNT_TRACE = "linkAccount"
  }
}
