package com.scribesoul.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scribesoul.app.models.TherapistDTO
import com.scribesoul.app.models.UserDTO
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance() // Tambahkan ini
    private val storage = com.google.firebase.storage.FirebaseStorage.getInstance()

    // Fungsi untuk simpan data profil (Teks)
    suspend fun saveUserProfile(user: UserDTO): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")

            // Simpan ke collection "therapists"
            firestore.collection("users")
                .document(uid)
                .set(user.copy(id = uid))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<UserDTO?> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
            val document = firestore.collection("users").document(uid).get().await()
            Result.success(document.toObject(UserDTO::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}