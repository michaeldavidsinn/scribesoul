package com.example.scribesoul.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scribesoul.R
import com.example.scribesoul.utils.softShadow

@Composable
fun ProblemBubble(
    problem: String,
    onClick: () -> Unit = {}
){
    Column (
        modifier = Modifier.clickable(onClick = {
            onClick
        })
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .padding(3.dp)
                .softShadow(
                    radius = 10f,
                    offsetY = 5f,
                    alpha = 0.18f
                )
                .align(Alignment.CenterHorizontally),
        ) {
            Box(
                modifier = Modifier.clip(CircleShape).background(Color.White).padding(10.dp).align(Alignment.Center)
            ){
                Image(
                    painter = painterResource(R.drawable.cat2),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp),
                    contentScale = ContentScale.Crop
                )
            }

        }
        Text(text = problem,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight(400),
                fontSize = 12.sp,
                color = Color(0xFF5F5F5F)
            ))

    }
}