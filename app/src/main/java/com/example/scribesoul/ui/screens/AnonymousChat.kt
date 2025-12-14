package com.example.scribesoul.ui.screens

import com.example.scribesoul.ui.components.InputBar
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.scribesoul.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search

import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.navigation.compose.rememberNavController
import com.example.scribesoul.ui.navigation.BottomNavItem
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.mutableStateListOf
import com.example.scribesoul.model.PostData
import androidx.compose.foundation.lazy.items


@Composable
fun AnonymousChatScreen(navController: NavController) {

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var showCommentDialog by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    var selectedPostIndex by remember { mutableStateOf(-1) }

    val postList = remember {
        mutableStateListOf(
            PostData(1, "Anonymous", "Sometimes I feel like...", 24, 12),
            PostData(2, "Anonymous", "Coding is fun but tiring...", 10, 5),
            PostData(3, "Anonymous", "Anyone knows good coffee in Sby?", 5, 2),
            PostData(4, "Anonymous", "Just want to share my story...", 100, 45),
            PostData(5, "Anonymous", "Is it okay to cry?", 50, 20),
        )
    }

    val filteredPosts = if (searchQuery.isEmpty()) {
        postList
    } else {
        postList.filter {
            it.description.contains(searchQuery, ignoreCase = true) ||
                    it.title.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Lingkaran gradient
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleY = 1.15f // cukup sedikit, jangan terlalu besar
                    alpha = 0.9f // transparan agar teks tidak ketutupan
                }
        ) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE0ECFF),
                        Color.Transparent
                    ),
                    center = Offset(size.width / 2, size.height / 2),
                    radius = size.minDimension * 0.5f
                )
            )
        }


        // Gunakan LazyColumn langsung sebagai konten scrollable
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 25.dp),
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isSearchActive) {
                        // --- TAMPILAN SEARCH BAR ---
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp), // Samakan padding atas
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // TextField untuk input pencarian
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search posts...", color = Color.Gray) },
                                modifier = Modifier.weight(1f),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color(0xFF2B395B),
                                    unfocusedIndicatorColor = Color.Gray,
                                    focusedTextColor = Color.Black, // REVISI: Text jadi Hitam
                                    unfocusedTextColor = Color.Black, // REVISI: Text jadi Hitam
                                    cursorColor = Color.Black // REVISI: Cursor jadi Hitam
                                ),
                                maxLines = 1,
                                singleLine = true
                            )
                            // Tombol close untuk menutup search bar
                            IconButton(onClick = {
                                isSearchActive = false
                                searchQuery = "" // Reset teks pencarian
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Search",
                                    tint = Color(0xFF2B395B)
                                )
                            }
                        }
                    } else {
                        // --- TAMPILAN HEADER ASLI ---
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp, start = 4.dp, end = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Ikon Kiri (Search)
                            IconButton(
                                onClick = { isSearchActive = true }, // <-- MODIFIKASI Aksi
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color(0xFF2B395B),
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            // Judul
                            Text(
                                text = "SoulFess",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight(650),
                                    fontSize = 32.sp
                                ),
                                color = Color(0xFF2B395B),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )

                            // Ikon Kanan (Profile)
                            IconButton(
                                onClick = { navController.navigate("join_chat") }, // <-- MODIFIKASI Aksi
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = Color(0xFF2B395B),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                    // --- AKHIR HEADER BARU ---

                    Text(
                        text = "Share your story anonymously",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 10.sp
                        ),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .offset(y = (-7).dp) // naikkan sedikit
                            .padding(bottom = 16.dp) // opsional, untuk jarak bawah
                    )
                }
            }

            items(filteredPosts) { post ->
                val originalIndex = postList.indexOf(post)

                Column {
                    ChatCard(
                        title = post.title,
                        description = post.description,
                        likeCount = if (post.isLiked) post.initialLikeCount + 1 else post.initialLikeCount,
                        commentCount = post.commentCount,
                        isLiked = post.isLiked,

                        onLikeClick = {
                            if (originalIndex != -1) {
                                postList[originalIndex] = post.copy(isLiked = !post.isLiked)
                            }
                        },

                        onCommentClick = {
                            // Logika dialog komentar (seperti sebelumnya)
                            selectedPostIndex = originalIndex
                            commentText = ""
                            showCommentDialog = true
                        }
                    )

                    Image(painter = painterResource(id = R.drawable.gariswarna), contentDescription = "Divider", modifier = Modifier.fillMaxWidth().height(12.dp))
                }
            }

            // Spacer untuk memberi ruang agar item terakhir tidak tertutup BottomBar
            item {
                Spacer(modifier = Modifier.height(170.dp))
            }
        }

        // Konten yang menempel di bawah (InputBar + BottomBar)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            InputBar(
                onSend = { message ->
                    if (message.isNotBlank()) {
                        // Tambahkan postingan baru ke index 0 (paling atas)
                        postList.add(0, PostData(
                            id = (postList.maxOfOrNull { it.id } ?: 0) + 1,
                            title = "Anonymous", // Default user
                            description = message,
                            initialLikeCount = 0,
                            commentCount = 0,
                            isLiked = false
                        ))
                    }
                }
            )
            BottomBarAnonymous(navController = navController)
        }
        if (showCommentDialog) {
            AlertDialog(
                onDismissRequest = { showCommentDialog = false },
                containerColor = Color.White,
                title = {
                    Text(
                        text = "Add Comment",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF2B395B)
                    )
                },
                text = {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text("Type your comment...") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF2B395B),
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = Color(0xFF2B395B),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // --- LOGIKA UPDATE JUMLAH KOMEN ---
                            if (selectedPostIndex != -1 && commentText.isNotEmpty()) {
                                val currentPost = postList[selectedPostIndex]
                                // Update post di list: copy post lama, tapi commentCount + 1
                                postList[selectedPostIndex] = currentPost.copy(commentCount = currentPost.commentCount + 1)
                            }
                            showCommentDialog = false // Tutup dialog
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B395B))
                    ) {
                        Text("Send", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCommentDialog = false }) {
                        Text("Cancel", color = Color(0xFF2B395B))
                    }
                }
            )
        }
    }
}

@Composable
fun ChatCard(
    title: String,
    description: String,
    likeCount: Int,
    commentCount: Int,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    // State untuk dropdown menu Report
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8FF).copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar
            Box(modifier = Modifier.size(52.dp)) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        brush = Brush.linearGradient(colors = listOf(Color(0xFF82D9D2), Color(0xFF74A8FF)), start = Offset(0f, 0f), end = Offset(size.width, size.height)),
                        style = Stroke(width = 4f)
                    )
                }
                Image(painter = painterResource(id = R.drawable.cat2), contentDescription = null, modifier = Modifier.size(36.dp).align(Alignment.Center).clip(CircleShape))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                // Header Post
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, color = Color(0xFF2B395B))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Just Now", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2B395B))
                    Spacer(modifier = Modifier.weight(1f))

                    // --- DROPDOWN MENU REPORT ---
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.Gray)
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            containerColor = Color.White
                        ) {
                            DropdownMenuItem(
                                text = { Text("Report Post", color = Color.Red) },
                                onClick = { showMenu = false /* TODO: Logic Report */ }
                            )
                            DropdownMenuItem(
                                text = { Text("Not Interested", color = Color(0xFF2B395B)) },
                                onClick = { showMenu = false }
                            )
                        }
                    }
                }

                Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF2B395B))
                Spacer(modifier = Modifier.height(8.dp))

                // Actions (Like & Comment)
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(top = 4.dp)) {
                    // Like
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onLikeClick() }) {
                        val likeIconResId = if (isLiked) R.drawable.ic_heart_filled else R.drawable.like
                        val likeIconColor = if (isLiked) Color(0xFFE91E63) else Color(0xFF2B395B)
                        Icon(painter = painterResource(id = likeIconResId), contentDescription = "Like", modifier = Modifier.size(20.dp), tint = likeIconColor)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$likeCount", style = MaterialTheme.typography.bodySmall, color = likeIconColor)
                    }
                    // Comment
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onCommentClick() }) {
                        Icon(painter = painterResource(id = R.drawable.comment), contentDescription = "Comment", modifier = Modifier.size(20.dp), tint = Color(0xFF2B395B))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$commentCount", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2B395B))
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarAnonymous(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, top = 6.dp, bottom = 20.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(30.dp),
                clip = false
            )
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .height(70.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(R.drawable.home_icon, "Home", iconSize = 28.dp) {
                navController.navigate("home") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.therapist_icon, "Therapist", iconSize = 25.dp) {
                navController.navigate("therapist") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.explore_icon_clicked, "Explore", iconSize = 40.dp) {
                navController.navigate("explore") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.scribble_icon, "Scribble", iconSize = 28.dp) {
                navController.navigate("addScribble") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.journal_icon, "Journal", iconSize = 25.dp) {
                navController.navigate("journalList") {
                    launchSingleTop = true
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnonymousChatPreview() {
    val dummyController = rememberNavController()
    AnonymousChatScreen(navController = dummyController)
}