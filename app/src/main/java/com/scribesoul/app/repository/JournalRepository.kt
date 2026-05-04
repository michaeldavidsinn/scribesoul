package com.scribesoul.app.repository

import FirestoreJournalMeta
import android.content.Context
import com.scribesoul.app.models.JournalDTO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

interface JournalRepository {
//    suspend fun getAllJournals(userId: Int): List<JournalDTO>
//    suspend fun getJournal(journalId: Int): JournalDTO?
//    suspend fun saveJournal(journalDTO: JournalDTO)

    suspend fun getAllJournals(): List<FirestoreJournalMeta>
    suspend fun getJournal(journalId: Int): JournalDTO?
    suspend fun saveJournal(journalDTO: JournalDTO)
    suspend fun deleteJournal(journalId: Int)
}

//class DefaultJournalRepository(
//    private val journalService: JournalService,
//    private val context: Context
//) : JournalRepository {
//
//    private val json = Json {
//        prettyPrint = true
//        ignoreUnknownKeys = true
//    }
//
//    override suspend fun getAllJournals(userId: Int): List<JournalDTO> {
//        return try {
//            // Try to get from your Node.js backend
//            journalService.getAllJournals(userId)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Return empty or load a local cached list
//        }
//    }
//
//    override suspend fun getJournal(journalId: Int): JournalDTO? {
//        return try {
//            // Try network first
//            journalService.getJournal(journalId)
//        } catch (e: Exception) {
//            // If offline or server is down, load from local storage!
//            loadLocalJournal(journalId)
//        }
//    }
//
//    override suspend fun saveJournal(journalDTO: JournalDTO) {
//        // 1. Save to local device immediately so the user doesn't lose work
//        saveLocalJournal(journalDTO)
//
//        // 2. Try to sync to the backend
//        try {
//            journalService.addJournal(journalDTO)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            // Backend is down, but that's okay, it's saved locally!
//        }
//    }
//
//    // --- Private Local Storage Helpers ---
//
//    private fun saveLocalJournal(journalDTO: JournalDTO) {
//        val fileName = "journal_${journalDTO.id}.json"
//        val jsonString = json.encodeToString(journalDTO)
//        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
//            output.write(jsonString.toByteArray())
//        }
//    }
//
//    private fun loadLocalJournal(journalId: Int): JournalDTO? {
//        val fileName = "journal_$journalId.json"
//        val file = File(context.filesDir, fileName)
//        if (!file.exists()) return null
//        return json.decodeFromString<JournalDTO>(file.readText())
//    }
//}