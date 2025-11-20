package com.example.scribesoul.service

import Journal
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JournalService {

    @GET("/journals/{userId}")
    suspend fun getAllJournals(@Path("userId") userId: Int): List<Journal>

    @GET("/journal/{id}")
    suspend fun getJournal(@Path("id") id: Int): Journal

    @POST("/journal")
    suspend fun addJournal(@Body journal: Journal)

    @PUT("/journal/{id}")
    suspend fun updateJournal(
        @Path("id") id: Int,
        @Body journal: Journal
    )

    @DELETE("/journal/{id}")
    suspend fun deleteJournal(@Path("id") id: Int)
}