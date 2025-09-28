package com.example.scribesoul.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scribesoul.R

// Inisialisasi FontFamily Poppins (Dinonaktifkan)
// val Poppins = FontFamily(
//     Font(R.font.poppins_regular, FontWeight.Normal),
//     Font(R.font.poppins_bold, FontWeight.Bold),
//     Font(R.font.poppins_medium, FontWeight.Medium),
//     Font(R.font.poppins_light, FontWeight.Light),
//     Font(R.font.poppins_semibold, FontWeight.SemiBold)
// )

// Inisialisasi FontFamily Verdana
val Verdana = FontFamily(
    Font(R.font.verdana_regular, FontWeight.Normal),
    Font(R.font.verdana_bold, FontWeight.Bold),
    Font(R.font.verdana_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.verdana_bold_italic, FontWeight.Bold, FontStyle.Italic)
)

// Typography kustom pakai Verdana
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Verdana, // Diubah
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Verdana, // Diubah
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Verdana, // Diubah
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Verdana, // Diubah
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Verdana, // Diubah
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)