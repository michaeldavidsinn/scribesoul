package com.scribesoul.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.scribesoul.app.models.FirestoreScribbleMeta
import com.scribesoul.app.models.ScribbleDataDTO
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FirebaseScribbleRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
    }

    suspend fun saveScribble(scribbleId: String, title: String, data: ScribbleDataDTO): Result<Unit> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))

        return try {
            val filePath = "users/$uid/scribbles/scribble_$scribbleId.json"
            val storageRef = storage.reference.child(filePath)

            // 1. Save Canvas Data to Firebase Storage
            val jsonString = json.encodeToString(data)
            storageRef.putBytes(jsonString.toByteArray()).await()

            // 2. Save Metadata to Firestore
            val meta = FirestoreScribbleMeta(
                id = scribbleId,
                uid = uid,
                title = title,
                lastUpdated = System.currentTimeMillis(),
                storageFilePath = filePath
            )

            db.collection("users").document(uid)
                .collection("scribbles").document(scribbleId)
                .set(meta, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllScribbles(): List<FirestoreScribbleMeta> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = db.collection("users").document(uid).collection("scribbles").get().await()
            snapshot.toObjects(FirestoreScribbleMeta::class.java).sortedByDescending { it.lastUpdated }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteScribble(scribbleId: String): Result<Unit> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))

        return try {
            // 1. Delete from Storage
            val filePath = "users/$uid/scribbles/scribble_$scribbleId.json"
            storage.reference.child(filePath).delete().await()

            // 2. Delete from Firestore
            db.collection("users").document(uid)
                .collection("scribbles").document(scribbleId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}