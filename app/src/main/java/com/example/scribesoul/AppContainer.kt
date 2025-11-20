package com.example.scribesoul

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.scribesoul.repository.JournalRepository
import com.example.scribesoul.repository.NetworkJournalRepository
import com.example.scribesoul.service.JournalService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
 val journalRepository: JournalRepository
}

class DefaultAppContainer(
    private val userDataStore: DataStore<Preferences>
): AppContainer {
    private val baseUrl = "http://10.0.2.2:3000/"

    private val journalRetrofitService: JournalService by lazy {
        val retrofit = initRetrofit()
        retrofit.create(JournalService::class.java)
    }

    override val journalRepository: JournalRepository by lazy {
        NetworkJournalRepository(journalRetrofitService)
    }

    private fun initRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)

        return Retrofit
            .Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .client(client.build())
            .baseUrl(baseUrl)
            .build()
    }

}