package com.scribesoul.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(0.9f)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
            .clip(RoundedCornerShape(50)),
        placeholder = {
            Text(text = placeholder, color = Color(0xFF2B395B).copy(alpha = 0.5f))
        },
        leadingIcon = {
            Box(
                modifier = Modifier
                    .padding(start = 20.dp, end = 10.dp)
                    .width(2.dp)
                    .height(24.dp)
                    .background(Color(0xFF2B395B))
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun OnboardingButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val buttonGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF82D9D2),
            Color(0xFF7CC3E6),
            Color(0xFF74A8FF)
        ),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    Box(
        modifier = modifier
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .background(brush = buttonGradient)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
    }
}

@Composable
fun OnboardingSelectableItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Gradasi Border RBW (Yellow, Pink, Purple) - Tetap digunakan sesuai request
    val rbwGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFF47A), // Yellow
            Color(0xFFFFA8CF), // Pink
            Color(0xFFA774FF)  // Purple
        )
    )

    // 2. Gradasi Latar Belakang Biru (hanya muncul saat diklik/isSelected)
    val selectedGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFF47A), // Yellow
            Color(0xFFFFA8CF), // Pink
            Color(0xFFA774FF)  // Purple
        ),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    Box(
        modifier = modifier
            .padding(8.dp) // Jarak antar kotak (efek dempet)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(50.dp))
            .clip(RoundedCornerShape(50.dp))
            // LOGIK BACKGROUND: Putih jika tidak dipilih, Biru Gradient jika dipilih
            .background(
                brush = if (isSelected) selectedGradient else Brush.linearGradient(listOf(Color.White, Color.White))
            )
            // LOGIK BORDER: Selalu muncul (Yellow-Pink-Purple) di atas background
            .border(
                width = 2.dp,
                brush = rbwGradient,
                shape = RoundedCornerShape(50.dp)
            )
            .clickable { onClick() }
            .padding(13.dp), // Padding dalam teks
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            // Warna teks jadi putih saat dipilih agar kontras dengan background biru
            color = if (isSelected) Color.White else Color(0xFF2B395B),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}