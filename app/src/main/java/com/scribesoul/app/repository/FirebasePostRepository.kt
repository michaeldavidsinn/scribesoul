package com.scribesoul.app.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.scribesoul.app.model.PostData
import com.scribesoul.app.models.PostDTO
import com.scribesoul.app.models.toDTO
import com.scribesoul.app.models.toUIModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebasePostRepository(
    private val firestore: FirebaseFirestore
) : PostRepository {

    private val postCollection = firestore.collection("posts")

    override fun getPosts(): Flow<List<PostData>> = callbackFlow {
        val subscription = postCollection
            // Mengurutkan postingan, yang terbaru di atas.
            // Pastikan nanti kamu mengisi field 'date' di PostDTO saat create post
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    // Ambil id dokumen sebagai id post, lalu map ke UI Model
                    val posts = snapshot.documents.mapNotNull { doc ->
                        val dto = doc.toObject(PostDTO::class.java)
                        // Inject id dokumen ke DTO agar bisa dipakai untuk Like/Comment
                        dto?.copy(id = doc.id)?.toUIModel(currentUserId = "")
                    }
                    trySend(posts)
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getCommentsByPostId(postId: String): Flow<List<PostData>> = callbackFlow {
        // Komentar disimpan di sub-collection di dalam dokumen post
        val subscription = postCollection.document(postId).collection("comments")
            .orderBy("date", Query.Direction.ASCENDING) // Komentar terlama di atas
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val comments = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(PostDTO::class.java)?.toUIModel(currentUserId = "")
                    }
                    trySend(comments)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun createPost(post: PostData) {
        try {
            // Kita biarkan Firestore yang generate ID
            val postDTO = post.toDTO().copy(id = "", date = System.currentTimeMillis().toString())
            postCollection.add(postDTO).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun addComment(postId: String, comment: String) {
        try {
            // Buat objek komentar baru
            val commentDTO = PostDTO(
                title = "Anonymous",
                description = comment,
                date = System.currentTimeMillis().toString()
            )
            // Tambahkan ke sub-collection "comments"
            postCollection.document(postId).collection("comments").add(commentDTO).await()

            // Tambah count comment di dokumen post utama
            val postRef = postCollection.document(postId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val newCommentCount = (snapshot.getLong("commentCount") ?: 0) + 1
                transaction.update(postRef, "commentCount", newCommentCount)
            }.await()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun likePost(postId: String, userId: String) {
        // Implementasi sederhana: tambah 1 ke initialLikeCount
        try {
            val postRef = postCollection.document(postId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val newLikeCount = (snapshot.getLong("initialLikeCount") ?: 0) + 1
                transaction.update(postRef, "initialLikeCount", newLikeCount)
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}