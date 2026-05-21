package com.scribesoul.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scribesoul.app.models.ClientInfoDTO
import com.scribesoul.app.models.TherapistDTO
import com.scribesoul.app.models.TherapySessionDTO
import kotlinx.coroutines.tasks.await

class TherapistRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance() // Tambahkan ini
    private val storage = com.google.firebase.storage.FirebaseStorage.getInstance()

    // Fungsi untuk simpan data profil (Teks)
    suspend fun saveTherapistProfile(therapist: TherapistDTO): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")

            // Simpan ke collection "therapists"
            firestore.collection("therapists")
                .document(uid)
                .set(therapist.copy(id = uid))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fungsi untuk upload gambar (File)
    suspend fun uploadLicenseImage(bitmap: android.graphics.Bitmap): String {
        val uid = auth.currentUser?.uid ?: return ""
        val storageRef = storage.reference.child("licenses/$uid.jpg")

        val baos = java.io.ByteArrayOutputStream()
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()

        storageRef.putBytes(data).await()
        return storageRef.downloadUrl.await().toString()
    }

    suspend fun getTherapistProfile(): Result<TherapistDTO?> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
            val document = firestore.collection("therapists").document(uid).get().await()
            Result.success(document.toObject(TherapistDTO::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTherapistSessions(): Result<List<TherapySessionDTO>> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
            val snapshot = firestore.collection("sessions")
                .whereEqualTo("therapistId", uid)
                .get().await()
            val sessions = snapshot.toObjects(TherapySessionDTO::class.java)
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTherapistBirthday(newBirthday: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            firestore.collection("therapists")
                .document(uid)
                .update("birthday", newBirthday) // Hanya update field birthday
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getClientsForTherapist(): Result<List<ClientInfoDTO>> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
            val snapshot = firestore.collection("clients")
                .whereEqualTo("therapistId", uid)
                .get().await()
            val clients = snapshot.toObjects(ClientInfoDTO::class.java)
            Result.success(clients)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getClientSessionHistory(clientId: String): Result<List<TherapySessionDTO>> {
        return try {
            val snapshot = firestore.collection("sessions")
                .whereEqualTo("clientId", clientId)
                .whereEqualTo("status", "COMPLETED") // Hanya yang sudah selesai
                .get().await()
            Result.success(snapshot.toObjects(TherapySessionDTO::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getClientNotes(clientId: String): Result<List<TherapySessionDTO>> {
        // Biasanya notes disimpan di dalam objek session yang sudah selesai
        return getClientSessionHistory(clientId)
    }

    suspend fun getAllTherapists(): Result<List<TherapistDTO>> {
        return try {
            val snapshot = firestore.collection("therapists").get().await()
            val therapists = snapshot.documents.mapNotNull { doc ->
                doc.toObject(TherapistDTO::class.java)?.copy(id = doc.id)
            }
            Result.success(therapists)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}