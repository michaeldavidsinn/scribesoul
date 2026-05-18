package com.scribesoul.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.firestore.FirebaseFirestore
import com.scribesoul.app.repository.AuthRepository
import com.scribesoul.app.repository.FirebaseAuthRepository
import com.scribesoul.app.repository.FirebaseGroupChatRepository
import com.scribesoul.app.repository.FirebaseHabitRepository
import com.scribesoul.app.repository.JournalRepository
import com.scribesoul.app.repository.FirebaseJournalRepository
import com.scribesoul.app.repository.FirebasePostRepository
import com.scribesoul.app.repository.GroupChatRepository
import com.scribesoul.app.repository.PostRepository
import com.scribesoul.app.repository.TherapistRepository
import com.scribesoul.app.repository.UserRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {

 val journalRepository: JournalRepository
 val groupChatRepository: GroupChatRepository
 val postRepository: PostRepository
 val authRepository: AuthRepository
 val habitRepository: FirebaseHabitRepository
 val therapistRepository: TherapistRepository
 val userRepository: UserRepository
}

class DefaultAppContainer(
    private val context: Context,
    private val userDataStore: DataStore<Preferences>
): AppContainer {

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

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

    override val habitRepository: FirebaseHabitRepository by lazy {
        FirebaseHabitRepository()
    }

    override val journalRepository: JournalRepository by lazy {
        FirebaseJournalRepository()
    }

    // 2. Tambahkan Implementasi untuk GroupChatRepository
    override val groupChatRepository: GroupChatRepository by lazy {
        FirebaseGroupChatRepository(firestore)
    }

    override val postRepository: PostRepository by lazy {
        FirebasePostRepository(firestore)
    }

    override val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository()
    }

    override val userRepository: UserRepository by lazy{
        UserRepository()
    }

    override val therapistRepository: TherapistRepository by lazy {
        TherapistRepository()
    }
}