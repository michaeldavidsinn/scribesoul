package com.example.scribesoul.ui.components.journalPages

import JournalPage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scribesoul.R
import com.example.scribesoul.viewModels.TodoPageViewModel

@Composable
fun TodoPage(
    page: JournalPage.TodoPage,

){
    var showTextInput by remember { mutableStateOf(false) }

    if (showTextInput) {
        TodoInputDialog(
            onTodoCreate = { name ->
                showTextInput = false
                page.todoList.add(Pair(name, false))
            },
            onDismissRequest = { showTextInput = false }
        )
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFFFFDB4), shape = RoundedCornerShape(size = 23.dp))
            .height(640.dp)
            .fillMaxWidth(fraction=0.8f)
            .clip(RoundedCornerShape(23.dp))
            .clipToBounds()
            .padding(20.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(-50.dp)
        ) {
            Text("TO DO", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 50.sp,
                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                    fontWeight = FontWeight(600),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                )
            )
            Text("List", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 96.sp,
                    fontFamily = FontFamily(Font(R.font.palace_script)),
                    fontWeight = FontWeight(500),
                    color = Color.Black,
                    letterSpacing = 1.sp,
                )
            )

        }
        LazyColumn(

        ) {
            items(page.todoList.size) { index ->
                var (task, done) = page.todoList[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { page.todoList[index] = Pair(page.todoList[index].first, !page.todoList[index].second) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = done,
                        onCheckedChange = { page.todoList[index] = Pair(page.todoList[index].first, !page.todoList[index].second) }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            task,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.verdana)),
                                fontWeight = FontWeight(400),
                                color = Color(0XFF2B395B),
                                letterSpacing = 1.sp,
                            )
                        )
                        Text(
                            "Today", // You can replace with a timestamp if needed
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.verdana)),
                                fontWeight = FontWeight(400),
                                color = Color(0XFF2B395B),
                                letterSpacing = 1.sp,
                            )
                        )
                    }
                }
            }
            item{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Column(
                        modifier = Modifier.clip(CircleShape).background(Color.White).size(30.dp).clickable{
                            showTextInput = true
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center


                    ) {
                        Text("+",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.verdana_bold)),
                                fontWeight = FontWeight(600),
                                color = Color.Black,
                                letterSpacing = 1.sp,
                            )
                        )
                    }

                }
            }
        }

        }
    }

@Composable
fun TodoInputDialog(onDismissRequest: () -> Unit, onTodoCreate: (String) -> Unit){
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Input New Todo") },
        text = {
            TextField(value = name, onValueChange = {
                name = it
            })
        },
        confirmButton = { Button(onClick = { onTodoCreate(name) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoPageView(){
    TodoPage(page = JournalPage.TodoPage(id = 2))
}