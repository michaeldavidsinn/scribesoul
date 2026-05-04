package com.scribesoul.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.scribesoul.app.repository.JournalRepository
import com.scribesoul.app.repository.FirebaseJournalRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {

 val journalRepository: JournalRepository
}

class DefaultAppContainer(
    private val context: Context,
    private val userDataStore: DataStore<Preferences>
): AppContainer {
    private val baseUrl = "http://10.0.2.2:3000/"

//    private val journalRetrofitService: JournalService by lazy {
//        val retrofit = initRetrofit()
//        retrofit.create(JournalService::class.java)
//    }
//
//    override val journalRepository: JournalRepository by lazy {
//        DefaultJournalRepository(journalRetrofitService, context)
//    }
//
//    private fun initRetrofit(): Retrofit {
//        val logging = HttpLoggingInterceptor()
//        logging.level = (HttpLoggingInterceptor.Level.BODY)
//
//        val client = OkHttpClient.Builder()
//        client.addInterceptor(logging)
//
//        return Retrofit
//            .Builder()
//            .addConverterFactory(
//                GsonConverterFactory.create()
//            )
//            .client(client.build())
//            .baseUrl(baseUrl)
//            .build()
//    }

    override val journalRepository: JournalRepository by lazy {
        FirebaseJournalRepository()
    }

}