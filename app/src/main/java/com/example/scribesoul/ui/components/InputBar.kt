package com.example.scribesoul.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scribesoul.R

/**
 * Sebuah Composable yang menampilkan bar input teks dengan tombol kirim.
 *
 * @param onSendMessage Fungsi yang akan dipanggil saat tombol kirim ditekan.
 * Fungsi ini menerima String berisi pesan yang akan dikirim.
 * @param modifier Modifier untuk kustomisasi layout.
 */
@Composable
fun InputBar(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }
    var isToggled by remember { mutableStateOf(false) }

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
                    brush = Brush.verticalGradient(
                        colors = listOf(shadowColor, Color.Transparent),
                    ),
                    topLeft = Offset(0f, 0f),
                    size = Size(size.width, shadowSize)
                )
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, shadowColor),
                    ),
                    topLeft = Offset(0f, size.height - shadowSize),
                    size = Size(size.width, shadowSize)
                )
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(shadowColor, Color.Transparent),
                    ),
                    topLeft = Offset(0f, 0f),
                    size = Size(shadowSize, size.height)
                )
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, shadowColor),
                    ),
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
                .padding(start = 12.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text(
                        text = "write yours",
                        fontSize = 13.sp,
                        color = Color(0xFF2B395B).copy(alpha = 0.6f)
                    )
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                maxLines = 1
            )

            Switch(
                checked = isToggled,
                onCheckedChange = { isToggled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF2B395B),
                    uncheckedThumbColor = Color.LightGray,
                    checkedTrackColor = Color(0xFF2B395B).copy(alpha = 0.5f),
                    uncheckedTrackColor = Color.LightGray.copy(alpha = 0.5f)
                )
            )

            IconButton(
                onClick = { /* TODO: aksi ketika tombol + ditekan */ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus), // ganti dengan ikon plus kamu
                    contentDescription = "Add",
                    tint = Color(0xFF2B395B)
                )
            }
        }
    }
}