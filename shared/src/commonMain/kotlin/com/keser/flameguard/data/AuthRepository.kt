package com.keser.flameguard.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth

class AuthRepository {
  private val auth
    get() = Firebase.auth

  val currentUser: FirebaseUser?
    get() = auth.currentUser

  suspend fun login(email: String, password: String): Result<FirebaseUser> {
    return try {
      val result = auth.signInWithEmailAndPassword(email, password)
      Result.success(result.user!!)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun register(email: String, password: String): Result<FirebaseUser> {
    return try {
      val result = auth.createUserWithEmailAndPassword(email, password)
      Result.success(result.user!!)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun logout() {
    try {
      auth.signOut()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}
