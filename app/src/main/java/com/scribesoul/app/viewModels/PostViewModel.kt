package com.scribesoul.app.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scribesoul.app.model.PostData
import com.scribesoul.app.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: PostRepository // Kontrak data
) : ViewModel() {

    private val _posts = MutableStateFlow<List<PostData>>(emptyList())
    val posts: StateFlow<List<PostData>> = _posts

    var joined by mutableStateOf(false)

    fun fetchPosts() {
        viewModelScope.launch {
            // Panggil repository untuk ambil data dari Firebase
            repository.getPosts().collect { list ->
                _posts.value = list
            }
        }
    }

    fun uploadPost(content: String) {
        viewModelScope.launch {
            val newPost = PostData(
                id = 0.toString(), // Akan di-generate Firebase
                title = "Anonymous",
                description = content,
                initialLikeCount = 0,
                commentCount = 0
            )
            repository.createPost(newPost)
        }
    }

    fun toggleLike(post: PostData) {
        viewModelScope.launch {
            try {
                // 1. Panggil fungsi di repository
                // Kita ubah post.id menjadi String karena interface kamu meminta String
                // Untuk userId, sementara bisa hardcode atau ambil dari AuthService
                repository.likePost(post.id.toString(), "current_user_id")

                // 2. Refresh data agar UI terupdate dengan jumlah like terbaru
                fetchPosts()
            } catch (e: Exception) {
                // Handle error jika gagal koneksi ke Firebase
            }
        }
    }

    // Tambahkan di dalam class PostViewModel
    private val _comments = MutableStateFlow<List<PostData>>(emptyList())
    val comments: StateFlow<List<PostData>> = _comments

    // Fungsi untuk mengambil komentar berdasarkan ID Postingan Utama
    fun fetchComments(postId: String) {
        viewModelScope.launch {
            // Panggil repository untuk ambil komentar dari sub-collection/filter di Firebase
            repository.getCommentsByPostId(postId).collect { list ->
                _comments.value = list
            }
        }
    }

    // Update fungsi addComment agar juga mengupdate list komentar lokal
    fun addComment(post: PostData, commentContent: String) {
        viewModelScope.launch {
            try {
                repository.addComment(post.id.toString(), commentContent)

                // Setelah kirim, ambil data terbaru agar UI update otomatis
                fetchComments(post.id.toString())
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Dummy Repository untuk keperluan Preview
                val dummyRepository = object : PostRepository {
                    override fun getPosts() = kotlinx.coroutines.flow.flowOf(emptyList<PostData>())

                    // --- TAMBAHKAN BARIS INI ---
                    override fun getCommentsByPostId(postId: String) = kotlinx.coroutines.flow.flowOf(emptyList<PostData>())
                    // ---------------------------

                    override suspend fun createPost(post: PostData) {}
                    override suspend fun likePost(postId: String, userId: String) {}
                    override suspend fun addComment(postId: String, comment: String) {}
                }
                PostViewModel(dummyRepository)
            }
        }
    }
}