package com.example.scribesoul.ui.components.journalPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scribesoul.R

@Composable
fun CreationPage(){
    Column(
        modifier = Modifier
            .background(Color(0xFF74A8FF), shape = RoundedCornerShape(size = 23.dp))
            .height(680.dp)
            .fillMaxWidth(fraction=0.8f)
            .clip(RoundedCornerShape(23.dp))
            .clipToBounds()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.plain_page),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("PLAIN",
                    style = TextStyle(
                        fontSize = 6.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                    )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("WIDE LINED",
                    style = TextStyle(
                        fontSize = 6.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("WIDE LINED\r\nSMALL MARGINS",
                    style = TextStyle(
                        fontSize = 6.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("WIDE LINED\r\nLARGE MARGINS",
                    style = TextStyle(
                        fontSize = 6.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.plain_page),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("NARROW LINED",
                    style = TextStyle(
                        fontSize = 6.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("NARROW LINED\r\nSMALL MARGINS",
                    style = TextStyle(
                        fontSize = 6.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("NARROW LINED\r\nLARGE MARGINS",
                    style = TextStyle(
                        fontSize = 6.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("SMALL GRID",
                    style = TextStyle(
                        fontSize = 6.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.plain_page),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("LARGE GRID",
                    style = TextStyle(
                        fontSize = 6.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.plain_page),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.plain_page),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Image(
                    painter = painterResource(R.drawable.wide_lined),
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp),
                )
                Text("Wide Lined",
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        letterSpacing = 1.sp,
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreationPageView(){
    CreationPage()
}