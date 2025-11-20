package com.example.scribesoul.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color

@Composable
fun BottomNavItem(
    iconId: Int,
    label: String,
    iconSize: Dp,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = label,
                modifier = Modifier.size(iconSize)
            )
        }

        Text(
            modifier = Modifier.offset(y = -6.dp),
            text = label,
            fontSize = 10.sp,
            color = Color(0xFF2B395B),
            textAlign = TextAlign.Center
        )
    }
}
