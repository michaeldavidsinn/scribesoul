package com.example.scribesoul.ui.screens


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scribesoul.R
import com.example.scribesoul.utils.softShadow

@Composable
fun AddScribbleScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFFE6ED)
                    )
                )
            )
    ) {
        // GradientCard sedikit di atas tengah layar
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            GradientCard(modifier = Modifier.offset(y = (-32).dp), navController) // naik 32dp
        }

        // Bottom bar tetap di bawah
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            BottomBarScribble(navController = navController)
        }
    }
}

@Composable
fun GradientCard(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = modifier
            .clickable{
                navController.navigate("scribbleDraw")
            }
            .width(200.dp)
            .height(300.dp)
            .softShadow(
                radius = 20f,
                offsetY = 12f,
                alpha = 0.18f
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.2f to Color(0xFFFFF47A),
                        0.4f to Color(0xFFFFA8CF),
                        0.6f to Color(0xFFA774FF)
                    ),
                    start = Offset(0f, 1000f),
                    end = Offset(1000f, 0f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Add, // ujungnya bulat
            contentDescription = "Add",
            tint = Color.White,
            modifier = Modifier.size(115.dp)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun AddScribblereview() {
    val dummyController = rememberNavController()
    AddScribbleScreen(navController = dummyController)
}
