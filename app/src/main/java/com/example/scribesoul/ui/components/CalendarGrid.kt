package com.example.scribesoul.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun CalendarGrid(
    month: YearMonth,
    reminders: SnapshotStateMap<LocalDate, SnapshotStateList<String>>
) {
    // --- INTERNAL STATE FOR THE POP-UP ---
    // These control the dialog only within this component
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var textInput by remember { mutableStateOf("") }

    val daysInMonth = month.lengthOfMonth()
    val firstDay = month.atDay(1).dayOfWeek.value % 7
    val totalCells = firstDay + daysInMonth
    val rows = (totalCells + 6) / 7
    val daysOfWeek = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

        // --- 1. DAY HEADERS (SUN, MON...) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B395B)
                )
            }
        }

        // --- 2. CALENDAR CELLS ---
        repeat(rows) { rowIndex ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(7) { colIndex ->
                    val index = rowIndex * 7 + colIndex
                    val dayNumber = index - firstDay + 1

                    if (dayNumber in 1..daysInMonth) {
                        val date = month.atDay(dayNumber)
                        val dailyReminders = reminders[date]

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .clickable {
                                    // Trigger the internal dialog logic
                                    selectedDate = date
                                    textInput = ""
                                    showDialog = true
                                }
                                .padding(4.dp)
                        ) {
                            // Date Number
                            Text(
                                text = dayNumber.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            // Preview of Events (Max 2 lines on the grid)
                            if (!dailyReminders.isNullOrEmpty()) {
                                Column(modifier = Modifier.padding(top = 2.dp)) {
                                    dailyReminders.take(2).forEach { eventText ->
                                        Text(
                                            text = eventText,
                                            fontSize = 8.sp,
                                            lineHeight = 10.sp,
                                            color = Color(0xFF2B395B),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis, // Handles long text gracefully
                                            modifier = Modifier
                                                .background(Color(0xFFEEF0F5), RoundedCornerShape(2.dp))
                                                .padding(horizontal = 2.dp, vertical = 1.dp)
                                                .fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                    }
                                    // Indicator if more events exist
                                    if (dailyReminders.size > 2) {
                                        Text(
                                            text = "+${dailyReminders.size - 2} more",
                                            fontSize = 7.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Empty box for spacing (e.g. before the 1st of the month)
                        Box(modifier = Modifier.weight(1f).height(60.dp))
                    }
                }
            }
        }
    }

    // --- 3. POP-UP DIALOG (Shows List + Add Field) ---
    if (showDialog && selectedDate != null) {
        // Get the list for the selected date, or an empty list if none exists yet
        val dateEvents = reminders[selectedDate] ?: SnapshotStateList()

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Column {
                    Text(
                        text = selectedDate!!.format(DateTimeFormatter.ofPattern("EEEE, MMM d")),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B395B)
                    )
                    Text("Events", fontSize = 14.sp, color = Color.Gray)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // A. LIST OF EXISTING EVENTS
                    if (dateEvents.isEmpty()) {
                        Text(
                            "No events yet.",
                            fontSize = 14.sp,
                            color = Color.LightGray,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 200.dp) // Limits height
                                .fillMaxWidth()
                        ) {
                            items(dateEvents) { event ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "• $event",
                                        fontSize = 16.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { dateEvents.remove(event) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Delete",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                                Divider(color = Color.LightGray.copy(alpha = 0.3f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // B. ADD NEW EVENT INPUT
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = { Text("Add event...") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (textInput.isNotBlank()) {
                                    // Create the list in the map if it doesn't exist, then add item
                                    val list = reminders.getOrPut(selectedDate!!) { SnapshotStateList() }
                                    list.add(textInput)
                                    textInput = "" // Clear input
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp) // Square button
                        ) {
                            Icon(Icons.Default.Add, "Add")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Done")
                }
            }
        )
    }
}