package com.scribesoul.app.ui.components.journalPages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scribesoul.app.ui.components.CalendarGrid
import java.time.LocalDate

//(0XFFFFFEE4)

@Composable
fun CalendarPage(
    page: JournalPage.CalendarPage,
    color: Color
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var textInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(23.dp))
            .height(640.dp)
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(23.dp))
            .clipToBounds()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp)
        ) {

            // --- MONTH NAVIGATION ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "◀",
                    fontSize = 32.sp,
                    color = Color(0xFF2B395B),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            page.currentMonth = page.currentMonth.minusMonths(1)
                        }
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        page.currentMonth.month.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B)
                    )
                    Text(
                        page.currentMonth.year.toString(),
                        fontSize = 22.sp,
                        color = Color.DarkGray
                    )
                }

                Text(
                    "▶",
                    fontSize = 32.sp,
                    color = Color(0xFF2B395B),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            page.currentMonth = page.currentMonth.plusMonths(1)
                        }
                )
            }

            Spacer(Modifier.height(8.dp))

            // --- IMPROVED GRID WITH SPACING ---
            CalendarGrid(
                month = page.currentMonth,
                reminders = page.reminders,
//                onDateClick = { date ->
//                    selectedDate = date
//                    textInput = ""
//                    showDialog = true
//                }
            )
        }
    }

    // --- ADD EVENT POP-UP ---
    if (showDialog && selectedDate != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Event for $selectedDate") },
            text = {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Type reminder here") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            val list = page.reminders.getOrPut(selectedDate!!) { SnapshotStateList() }
                            list.add(textInput)


                        }
                        showDialog = false
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarPageView(){
    CalendarPage(page = JournalPage.CalendarPage(id = 1), color = Color(0XFFFFCCE3))
}
