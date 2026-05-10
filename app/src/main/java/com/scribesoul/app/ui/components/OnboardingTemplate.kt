package com.scribesoul.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingTemplate(
    title: String,
    subtitle: String? = null,
    backgroundColor: Color,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, backgroundColor)
                )
            )
            .padding(horizontal = 30.dp)
            .padding(bottom = 50.dp) // Jarak tombol dari paling bawah layar
    ) {

        val darkBlue = Color(0xFF2B395B)
        val lightBlueSubtitle = Color(0xFF74A8FF)

        // Konten Utama (Title + Input)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 32.sp,
                color = darkBlue,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(8.dp)) // Jarak "mepet" yang konsisten
                Text(
                    text = subtitle,
                    fontSize = 13.sp, // Sesuaikan ukuran
                    color = lightBlueSubtitle,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Bagian ini akan berisi TextField-mu
            content()
        }

        // Tombol Navigasi yang "PATEN" di bawah
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Mengunci posisi di tengah bawah Box
                .fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OnboardingButton(
                text = "BACK",
                modifier = Modifier.weight(1f),
                onClick = onBackClick
            )
            OnboardingButton(
                text = "NEXT",
                modifier = Modifier.weight(1f),
                onClick = onNextClick
            )
        }
    }
}