package com.example.scribesoul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scribesoul.R
import com.example.scribesoul.ui.components.JournalCover
import com.example.scribesoul.ui.navigation.BottomNavItem
import com.example.scribesoul.utils.NameInputDialog
import com.example.scribesoul.viewModels.JournalListViewModel
import com.example.scribesoul.viewModels.JournalViewModel

@Composable
fun JournalListScreen(navController: NavController, journalListViewModel: JournalListViewModel, journalViewModel: JournalViewModel){
    var showTextInput by remember { mutableStateOf(false) }

    if (showTextInput) {
        JournalInputDialog(
            onNameCreate = { name ->
                showTextInput = false
                journalListViewModel.addJournal(name)
                journalViewModel.loadJournal(journal = journalListViewModel.journals.last(), navController = navController)
            },
            onDismissRequest = { showTextInput = false }
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFF4FFFE)
                    )
                )
            )
        ,
        contentAlignment = Alignment.TopCenter
    ){
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 60.dp)
            ) {
                Text("Bulletjournal",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontFamily = FontFamily(Font(R.font.verdana_bold)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF2B395B),

                        )
                    )
                Text("Customize your own journal as you wish",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.verdana)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF2B395B),
                    )
                    )
            }
            Spacer(modifier = Modifier.height(140.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding( top = 5.dp).align(alignment = Alignment.CenterHorizontally), contentPadding = PaddingValues(start = 100.dp, end = 100.dp)
            ) {
                items(journalListViewModel.journals) { item->
                    JournalCover(navController, journalTitle = item.name, journalDate = "21-11-2024", journalId = item.id.toString(), onClick = {journalViewModel.loadJournal(journal = item, navController)})
                }
//                item{
//                    JournalCover(navController = navController, journalTitle = "My Mental Journey", journalDate = "21-11-2024", journalId = "123")
//                }
//                item{
//                    JournalCover(navController = navController, journalTitle = "My Mental Journey", journalDate = "21-11-2024", journalId = "123")
//                }
//                item{
//                    JournalCover(navController = navController, journalTitle = "My Mental Journey", journalDate = "21-11-2024", journalId = "123")
//                }
                item{
                    Box(
                        modifier =
                            Modifier
                                .width(190.dp)
                                .height(250.dp)
                                .align(alignment = Alignment.CenterHorizontally)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFA774FF),
                                            Color(0xFFFFA8CF),
                                            Color(0xFFFFF47A)
                                        )
                                    )
                                )
                                .clickable{
                                   showTextInput = true
                                },
                        contentAlignment = Alignment.Center

                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            verticalArrangement = Arrangement.spacedBy(-30.dp)
                        ) {
                            Text("+",

                                style = TextStyle(
                                    fontSize = 100.sp,
                                    fontFamily = FontFamily(Font(R.font.verdana_bold)),
                                    fontWeight = FontWeight(600),
                                    color = Color.White,

                                    )
                            )
                        }
                    }
                }

            }

        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 0.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {

            BottomBarJournal(navController)
        }

    }


}

@Composable
fun BottomBarJournal(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, top = 6.dp, bottom = 40.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(30.dp),
                clip = false
            )
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .height(70.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(R.drawable.home_icon, "Home", 28.dp) {
                navController.navigate("home") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.therapist_icon, "Therapist", 25.dp) {
                navController.navigate("therapist") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.explore_icon, "Explore", 25.dp) {
                navController.navigate("explore") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.scribble_icon, "Scribble", 28.dp) {
                navController.navigate("scribble") {
                    launchSingleTop = true
                }
            }
            BottomNavItem(R.drawable.journal_icon_clicked, "Journal", 25.dp) {
                navController.navigate("journalList") {
                    launchSingleTop = true
                }
            }
        }
    }
}

@Composable
fun JournalInputDialog(onDismissRequest: () -> Unit, onNameCreate: (String) -> Unit){
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Input New Journal Name") },
        text = {
            TextField(value = name, onValueChange = {
                name = it
            })
        },
        confirmButton = { Button(onClick = { onNameCreate(name) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}


@Preview(showBackground = true)
@Composable
fun JournalListview() {
    // Gunakan dummy NavController untuk preview
    JournalListScreen(navController = NavController(LocalContext.current), journalViewModel = viewModel(factory = JournalViewModel.Factory), journalListViewModel = viewModel() )
}