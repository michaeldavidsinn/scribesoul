package com.example.scribesoul.ui.screens
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
import com.example.scribesoul.R
import com.example.scribesoul.model.PostData
import com.example.scribesoul.ui.components.InputBar
import com.example.scribesoul.viewModels.CommunityViewModel

@Composable
fun AnonymousReplyScreen(
    navController: NavController,
    communityViewModel: CommunityViewModel,
    postId: Int, // Menerima ID Post untuk menampilkan post yang benar
    isTherapist: Boolean = false
) {
    // Dummy Data untuk Post Utama (Nantinya ambil dari ViewModel berdasarkan postId)
    val mainPost = remember {
        PostData(postId, "Anonymous", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...", 24, 12)
    }

    // Dummy Data untuk Replies
    val replyList = remember {
        mutableStateListOf(
            PostData(101, "Anonymous", "Wah, relate banget kak. Semangat ya!", 5, 0, date = "21-11-2024"),
            PostData(102, "Anonymous", "I feel you, coding emang kadang bikin burn out.", 2, 0, date = "21-11-2024"),
            PostData(103, "Anonymous", "Keep going! You're not alone.", 10, 0, date = "21-11-2024"),
            PostData(104, "Anonymous", "Ada yang mau mabar 2K25 buat refresh otak?", 1, 0, date = "22-11-2024")
        )
    }

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
                    text = "Profile", // Sesuai Screenshot Figma
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color(0xFF2B395B)
                    )
                )
                Spacer(modifier = Modifier.width(48.dp)) // Spacer penyeimbang Back Button
            }

            // --- CONTENT (POST + REPLIES) ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 180.dp) // Ruang untuk Input & BottomBar
            ) {
                // Post Utama
                item {
                    ReplyCard(
                        post = mainPost,
                        isMainPost = true,
                        onLikeClick = { /* Logic Like */ }
                    )
                    // Garis Pemisah (gariswarna)
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
                        onLikeClick = { /* Logic Like Reply */ }
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
                        replyList.add(0, PostData(999, "Anonymous", message, 0, 0, date = "Just Now"))
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
                // MODIFIKASI: Jika reply, padding start ditambah (misal dari 28 ke 52)
                .padding(start = if (isMainPost) 28.dp else 60.dp, end = 24.dp),
            verticalAlignment = Alignment.Top
        ) {
            // --- AVATAR SECTION ---
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

            // --- CONTENT SECTION ---
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
                    // Ikon MoreVert di kanan atas
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Row untuk bungkus Teks dan Like (khusus Reply agar Like bisa di kanan)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = post.description,
                        modifier = Modifier.weight(1f), // Teks ambil sisa ruang
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = if (isMainPost) 14.sp else 13.sp,
                            lineHeight = 18.sp
                        ),
                        color = Color(0xFF2B395B)
                    )

                    // MODIFIKASI: Jika Reply, tampilkan Like di sebelah kanan teks
                    if (!isMainPost) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { onLikeClick() }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.like),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (post.isLiked) Color.Red else Color(0xFF2B395B)
                            )
                            Text(
                                text = "${post.initialLikeCount}",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = Color(0xFF2B395B)
                            )
                        }
                    }
                }

                // MODIFIKASI: Jika Post Utama, Like & Comment tetap di bawah teks
                if (isMainPost) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(painter = painterResource(id = R.drawable.comment), contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF2B395B))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${post.commentCount}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2B395B))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onLikeClick() }) {
                            Icon(painter = painterResource(id = R.drawable.like), contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF2B395B))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${post.initialLikeCount}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2B395B))
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
    // Mock NavController untuk preview
    val dummyController = rememberNavController()

    // Gunakan factory yang sama dengan yang ada di Screen utama kamu
    val dummyViewModel: CommunityViewModel = viewModel(factory = CommunityViewModel.Factory)

    AnonymousReplyScreen(
        navController = dummyController,
        communityViewModel = dummyViewModel,
        postId = 1, // Dummy ID
        isTherapist = false // Bisa kamu ganti true untuk cek tampilan terapis
    )
}