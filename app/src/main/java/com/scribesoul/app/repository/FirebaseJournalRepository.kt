package com.scribesoul.app.repository

import FirestoreJournalMeta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.scribesoul.app.models.JournalDTO
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.google.firebase.storage.StorageException

class FirebaseJournalRepository : JournalRepository {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
    }

    override suspend fun saveJournal(journalDTO: JournalDTO) {
        val uid = auth.currentUser?.uid ?: return

        val filePath = "users/$uid/journals/journal_${journalDTO.id}.json"
        val storageRef = storage.reference.child(filePath)

        // 1. Upload heavy Canvas Data to Cloud Storage
        val jsonString = json.encodeToString(journalDTO)
        storageRef.putBytes(jsonString.toByteArray()).await()

        // 2. Save lightweight Metadata to Firestore
        val meta = FirestoreJournalMeta(
            id = journalDTO.id,
            uid = uid,
            name = journalDTO.name,
            lastUpdated = System.currentTimeMillis(),
            storageFilePath = filePath
        )

        db.collection("users").document(uid)
            .collection("journals").document(journalDTO.id.toString())
            .set(meta, SetOptions.merge())
            .await()
    }

    override suspend fun getJournal(journalId: Int): JournalDTO? {
        val uid = auth.currentUser?.uid ?: return null

        return try {
            val filePath = "users/$uid/journals/journal_$journalId.json"
            val storageRef = storage.reference.child(filePath)

            val maxDownloadSizeBytes: Long = 15 * 1024 * 1024
            val bytes = storageRef.getBytes(maxDownloadSizeBytes).await()

            json.decodeFromString<JournalDTO>(String(bytes))

        } catch (e: StorageException) {
            // Check if the error is just a missing file (404)
            if (e.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                println("Firebase: No existing save file found. Starting a fresh journal.")
                null
            } else {
                // If it's a different storage error (like missing permissions), print it
                e.printStackTrace()
                null
            }
        } catch (e: Exception) {
            // Catch any JSON parsing errors
            e.printStackTrace()
            null
        }
    }

    override suspend fun getAllJournals(): List<FirestoreJournalMeta> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        return try {
            // Fetch ONLY metadata to display the list quickly
            val snapshot = db.collection("users").document(uid)
                .collection("journals")
                .get()
                .await()

            snapshot.toObjects(FirestoreJournalMeta::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun deleteJournal(journalId: Int) {
        val uid = auth.currentUser?.uid ?: return

        try {
            // 1. Delete from Firestore
            db.collection("users").document(uid)
                .collection("journals").document(journalId.toString())
                .delete().await()

            // 2. Delete file from Cloud Storage
            val filePath = "users/$uid/journals/journal_$journalId.json"
            storage.reference.child(filePath).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}