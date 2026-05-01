package com.scribesoul.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputBar(
    modifier: Modifier = Modifier,
    onSend: (String) -> Unit = {} // REVISI: Tambahkan callback ini
) {
    var inputText by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
            .drawWithContent {
                drawContent()

                val shadowColor = Color.Black.copy(alpha = 0.08f)
                val shadowSize = 10f

                // Inner shadow effect
                drawRect(
                    brush = Brush.verticalGradient(colors = listOf(shadowColor, Color.Transparent)),
                    topLeft = Offset(0f, 0f),
                    size = Size(size.width, shadowSize)
                )
                drawRect(
                    brush = Brush.verticalGradient(colors = listOf(Color.Transparent, shadowColor)),
                    topLeft = Offset(0f, size.height - shadowSize),
                    size = Size(size.width, shadowSize)
                )
                drawRect(
                    brush = Brush.horizontalGradient(colors = listOf(shadowColor, Color.Transparent)),
                    topLeft = Offset(0f, 0f),
                    size = Size(shadowSize, size.height)
                )
                drawRect(
                    brush = Brush.horizontalGradient(colors = listOf(Color.Transparent, shadowColor)),
                    topLeft = Offset(size.width - shadowSize, 0f),
                    size = Size(shadowSize, size.height)
                )
            }
            .border(
                width = 0.4.dp,
                color = Color(0xFF2B395B),
                shape = RoundedCornerShape(25.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // REVISI: Menggunakan BasicTextField agar tidak ada error contentPadding
            // dan teks bisa benar-benar di tengah vertikal.
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(end = 8.dp),
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = Color.Black // Warna teks input Hitam
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        // Placeholder manual
                        if (inputText.isEmpty()) {
                            Text(
                                text = "Write yours...",
                                fontSize = 13.sp,
                                color = Color.Black
                            )
                        }
                        // Input field asli
                        innerTextField()
                    }
                }
            )

            // Tombol Kirim (Pesawat)
            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        onSend(inputText)
                        inputText = "" // Kosongkan input setelah kirim
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color(0xFF2B395B)
                )
            }
        }
    }
}