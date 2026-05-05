package com.scribesoul.app.repository

import com.scribesoul.app.model.PostData
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<List<PostData>>

    fun getCommentsByPostId(postId: String): Flow<List<PostData>>

    suspend fun createPost(post: PostData)
    suspend fun likePost(postId: String, userId: String)
    suspend fun addComment(postId: String, comment: String)
}