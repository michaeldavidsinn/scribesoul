package com.scribesoul.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scribesoul.app.models.HabitDTO
import kotlinx.coroutines.tasks.await

class FirebaseHabitRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getHabits(): List<HabitDTO> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        return try {
            val snapshot = db.collection("users").document(uid)
                .collection("habits").get().await()

            snapshot.toObjects(HabitDTO::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun saveHabit(habitDTO: HabitDTO) {
        val uid = auth.currentUser?.uid ?: return

        try {
            db.collection("users").document(uid)
                .collection("habits").document(habitDTO.id.toString())
                .set(habitDTO).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}