package com.example.scribesoul.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scribesoul.R

@Composable
fun TherapistBookSessionScreen(navController: NavController) {

    // --- BRUSH DEFINITIONS ---
    val gradientBrushs = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF74A8FF),
            Color(0xFF82D9D2)
        )
    )
    val borderBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFFF47A),
            Color(0xFFFFA8CF),
            Color(0xFFA774FF)
        )
    )

    // --- MAIN LAYOUT CONTAINER ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE7DBFC),
                        Color(0xFFE7DBFC),
                        Color(0xFFFFFFFF),
                        Color(0xFFE7DBFC),
                        Color.Black.copy(alpha = 0.25f),
                    )
                )
            )
    ) {
        // --- SCROLLABLE CONTENT ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Item
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clip(RoundedCornerShape(50))
                            .clickable { navController.popBackStack() }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight(650),
                            fontSize = 25.sp
                        ),
                        color = Color(0xFF2B395B),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }

            // Main Profile Content Item
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Doctor Info and Avatar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 1.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .border(
                                        width = 2.dp,
                                        brush = borderBrush,
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                    .background(Color.White)
                                    .padding(horizontal = 12.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.star),
                                    contentDescription = "Rating star",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "4.5/5",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2B395B)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Dr. Lisa Hermawan S.Psi, M.Psi, Psikolog",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2B395B)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Psikolog klinis", fontSize = 13.sp, color = Color(0xFF2B395B))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Gangguan Kecemasan dan PTSD",
                                fontSize = 13.sp,
                                color = Color(0xFF2B395B)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = buildAnnotatedString {
                                    append("Rp. 60.000")
                                    withStyle(style = SpanStyle(color = Color(0xFF2B395B).copy(alpha = 0.6f))) {
                                        append("/session")
                                    }
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.cat2),
                            contentDescription = "Therapist Avatar",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(24.dp))
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val gradientBrush = Brush.horizontalGradient(listOf(Color(0xFF74A8FF), Color(0xFF82D9D2)))
                        // Box Experience
                        Box(
                            modifier = Modifier
                                .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
                                .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                                .padding(1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.White)
                                    .padding(horizontal = 18.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy((-5).dp)

                            ) {
                                Text(text = "8+ Yrs", style = MaterialTheme.typography.titleMedium.copy(fontSize = 23.sp, fontWeight = FontWeight.Bold), color = Color(0xFF2B395B))
                                Text(text = "Experience", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Color(0xFF2B395B))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // Box Total Client
                        Box(
                            modifier = Modifier
                                .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
                                .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                                .padding(1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.White)
                                    .padding(horizontal = 18.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy((-5).dp)
                            ) {
                                Text(text = "20", style = MaterialTheme.typography.titleMedium.copy(fontSize = 23.sp, fontWeight = FontWeight.Bold), color = Color(0xFF2B395B))
                                Text(text = "Total Client", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Color(0xFF2B395B))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // Box Reviews
                        Box(
                            modifier = Modifier
                                .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
                                .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                                .padding(1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.White)
                                    .padding(horizontal = 18.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy((-5).dp)
                            ) {
                                Text(text = "8k+", style = MaterialTheme.typography.titleMedium.copy(fontSize = 23.sp, fontWeight = FontWeight.Bold), color = Color(0xFF2B395B))
                                Text(text = "Reviews", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Color(0xFF2B395B))
                            }
                        }
                    }

                    // "Hi, I'm Clara" Description Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .border(width = 2.dp, brush = gradientBrushs, shape = RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .padding(18.dp),
                    ) {
                        Text(
                            text = "Hi, I’m Clara. I’ve been working with individuals and families for over 8 years, focusing on anxiety, stress management, and trauma recovery. My goal is to create a safe, compassionate space where you can share your story and begin healing.",
                            fontSize = 14.sp,
                            color = Color(0xFF2B395B),
                            textAlign = TextAlign.Justify
                        )
                    }

                    // Qualifications & Experience Card
                    InfoCard(
                        title = "Qualifications & Experience",
                        gradientBrush = gradientBrushs
                    ) {
                        Text(text = buildAnnotatedString { withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Education:\n") }; append("M.Psi in Clinical Psychology, University of Indonesia") }, fontSize = 14.sp, color = Color(0xFF2B395B))
                        Text(text = buildAnnotatedString { withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Experience:\n") }; append("8+ years in private practice, community mental health, and online therapy") }, fontSize = 14.sp, color = Color(0xFF2B395B))
                        Text(text = buildAnnotatedString { withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Specializations:\n") }; append("Anxiety Disorders, Depression, PTSD, Adolescents") }, fontSize = 14.sp, color = Color(0xFF2B395B))
                        Text(text = buildAnnotatedString { withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Languages:\n") }; append("English, Bahasa Indonesia") }, fontSize = 14.sp, color = Color(0xFF2B395B))
                    }

                    // Therapy Approach Card
                    InfoCard(
                        title = "Therapy Approach",
                        gradientBrush = gradientBrushs
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { Text(text = "CBT (Cognitive Behavioral Therapy)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2B395B)); Text(text = "restructuring negative thought patterns", fontSize = 14.sp, color = Color(0xFF2B395B)) }
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { Text(text = "Art Therapy", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2B395B)); Text(text = "expressive healing through creative methods", fontSize = 14.sp, color = Color(0xFF2B395B)) }
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { Text(text = "Person-Centered Therapy", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2B395B)); Text(text = "building trust and empathy", fontSize = 14.sp, color = Color(0xFF2B395B)) }
                    }

                    // Spacer at the end of the list to prevent overlap with the button
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }

        // --- FLOATING ACTION BUTTON ---
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) {
            Box(
                modifier = Modifier
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF82D9D2),
                                Color(0xFF7CC3E6),
                                Color(0xFF74A8FF)
                            )
                        )
                    )
                    .clickable { /* TODO: Handle book session action */ }
                    .padding(horizontal = 50.dp, vertical = 13.dp)
            ) {
                Text(
                    text = "BOOK SESSION",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

/**
 * A reusable card with a gradient border for displaying profile information.
 */
@Composable
fun InfoCard(
    title: String,
    modifier: Modifier = Modifier,
    gradientBrush: Brush,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 2.dp,
                brush = gradientBrush,
                shape = RoundedCornerShape(24.dp)
            )
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2B395B)
        )
        Spacer(modifier = Modifier.height(16.dp))


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun TherapistBookSessionPreview() {
    val context = LocalContext.current
    val navController = remember { NavController(context) }
    Surface(modifier = Modifier.fillMaxSize()) {
        TherapistBookSessionScreen(navController = navController)
    }
}