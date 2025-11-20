package com.example.scribesoul.repository


import Journal
import com.example.scribesoul.service.JournalService

interface JournalRepository {
    suspend fun getAllJournals(userId: Int): List<Journal>
    suspend fun getJournal(journalId: Int): Journal?
    suspend fun addJournal(journal: Journal)
    suspend fun updateJournal(journal: Journal)
    suspend fun deleteJournal(journalId: Int)
}

class NetworkJournalRepository(
    private val journalService: JournalService
): JournalRepository{
    override suspend fun getAllJournals(userId: Int): List<Journal> {
        TODO("Not yet implemented")
    }

    override suspend fun getJournal(journalId: Int): Journal? {
        TODO("Not yet implemented")
    }

    override suspend fun addJournal(journal: Journal) {
        TODO("Not yet implemented")
    }

    override suspend fun updateJournal(journal: Journal) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteJournal(journalId: Int) {
        TODO("Not yet implemented")
    }

}