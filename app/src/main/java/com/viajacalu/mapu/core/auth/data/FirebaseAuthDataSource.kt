package com.viajacalu.mapu.core.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.viajacalu.mapu.core.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// FirebaseAuth holds the user's Mapu account and keeps them signed in to the app
class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthDataSource {

    override fun getCurrentUser(): User? = firebaseAuth.currentUser?.toUser()

    override suspend fun signInWithGoogle(idToken: String): User {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        val user = result.user ?: throw IllegalStateException("Firebase returned no user after sign in")
        return user.toUser()
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    private fun FirebaseUser.toUser() = User(
        id = uid,
        name = displayName,
        email = email,
        photoUrl = photoUrl?.toString()
    )
}
