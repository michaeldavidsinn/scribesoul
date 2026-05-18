package com.scribesoul.app.ui.screens.user

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.scribesoul.R
import com.scribesoul.app.ui.components.InputBar
import com.scribesoul.app.model.PostData
import com.scribesoul.app.viewModels.PostViewModel // IMPORT POSTVIEWMODEL

@Composable
fun AnonymousReplyScreen(
    navController: NavController,
    postViewModel: PostViewModel, // GANTI JADI PostViewModel
    postId: String,
    isTherapist: Boolean = false
) {
    val postList by postViewModel.posts.collectAsState()
    val replyList by postViewModel.comments.collectAsState() // GANTI DUMMY KE INI

    // 2. Ambil data saat pertama kali layar dibuka
    LaunchedEffect(postId) {
        postViewModel.fetchComments(postId.toString())
    }

    // 3. Cari Post Utama (Perbaikan agar tidak error merah)
    val mainPost = postList.find { it.id == postId } ?: PostData(
        id = postId,
        title = "Anonymous",
        description = "Loading...",
        initialLikeCount = 0,
        commentCount = 0,
        isLiked = false,
        date = "..."
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Lingkaran gradient (Background Glow)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.8f }
        ) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFE0ECFF), Color.Transparent),
                    center = Offset(size.width / 2, size.height / 2),
                    radius = size.minDimension * 0.6f
                )
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // --- HEADER BAR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF2B395B)
                    )
                }
                Text(
                    text = "Profile",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color(0xFF2B395B)
                    )
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            // --- CONTENT (POST + REPLIES) ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 180.dp)
            ) {
                // Post Utama
                item {
                    ReplyCard(
                        post = mainPost,
                        isMainPost = true,
                        onLikeClick = {
                            // HUBUNGKAN LIKE KE VIEWMODEL
                            postViewModel.toggleLike(mainPost)
                        }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.gariswarna),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(12.dp).padding(horizontal = 28.dp)
                    )
                }

                // List Replies
                items(replyList) { reply ->
                    ReplyCard(
                        post = reply,
                        isMainPost = false,
                        onLikeClick = { postViewModel.toggleLike(reply) }
                    )
                    Divider(
                        color = Color(0xFF2B395B).copy(alpha = 0.1f),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 40.dp)
                    )
                }
            }
        }

        // --- BOTTOM SECTION (Input + Nav) ---
        val density = LocalDensity.current
        val imeBottom = WindowInsets.ime.getBottom(density)

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
                .padding(bottom = with(density) { (imeBottom * 0.3f).toDp() }),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            InputBar(
                onSend = { message ->
                    if (message.isNotBlank()) {
                        // 5. Kirim komen ke ViewModel (Firebase)
                        postViewModel.addComment(mainPost, message)
                    }
                }
            )
            if (isTherapist) {
                BottomBarTherapistAnonymous(navController = navController)
            } else {
                BottomBarAnonymous(navController = navController)
            }
        }
    }
}

@Composable
fun ReplyCard(
    post: PostData,
    isMainPost: Boolean,
    onLikeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(start = if (isMainPost) 28.dp else 60.dp, end = 24.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(modifier = Modifier.size(if (isMainPost) 52.dp else 38.dp)) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        brush = Brush.linearGradient(listOf(Color(0xFF82D9D2), Color(0xFF74A8FF))),
                        style = Stroke(width = 3f)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.cat2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(if (isMainPost) 36.dp else 26.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isMainPost) 16.sp else 14.sp
                        ),
                        color = Color(0xFF2B395B)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = post.date,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = post.description,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = if (isMainPost) 14.sp else 13.sp,
                            lineHeight = 18.sp
                        ),
                        color = Color(0xFF2B395B)
                    )

                    if (!isMainPost) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { onLikeClick() }
                        ) {
                            val iconColor = if (post.isLiked) Color(0xFFE91E63) else Color(0xFF2B395B)
                            val iconRes = if (post.isLiked) R.drawable.ic_heart_filled else R.drawable.like
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = iconColor
                            )
                            val displayLikes = if (post.isLiked) post.initialLikeCount + 1 else post.initialLikeCount
                            Text(
                                text = "$displayLikes",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = iconColor
                            )
                        }
                    }
                }

                if (isMainPost) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(painter = painterResource(id = R.drawable.comment), contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF2B395B))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${post.commentCount}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2B395B))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onLikeClick() }) {
                            val likeIconColor = if (post.isLiked) Color(0xFFE91E63) else Color(0xFF2B395B)
                            val likeIconRes = if (post.isLiked) R.drawable.ic_heart_filled else R.drawable.like
                            Icon(painter = painterResource(id = likeIconRes), contentDescription = null, modifier = Modifier.size(18.dp), tint = likeIconColor)
                            Spacer(modifier = Modifier.width(4.dp))
                            val displayLikes = if (post.isLiked) post.initialLikeCount + 1 else post.initialLikeCount
                            Text("$displayLikes", style = MaterialTheme.typography.bodySmall, color = likeIconColor)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AnonymousReplyPreview() {
    val dummyController = rememberNavController()

    // GANTI PREVIEW INI UNTUK MENGGUNAKAN PostViewModel
    AnonymousReplyScreen(
        navController = dummyController,
        postViewModel = viewModel(factory = PostViewModel.Factory),
        postId = "1",
        isTherapist = false
    )
}