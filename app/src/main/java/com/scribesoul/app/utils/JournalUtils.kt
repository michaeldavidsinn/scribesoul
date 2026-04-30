package com.scribesoul.app.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NameInputDialog(onDismissRequest: () -> Unit, onNameCreate: (String) -> Unit){
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Input New Page Name") },
        text = {
            TextField(value = name, onValueChange = {
                name = it
            })
        },
        confirmButton = { Button(onClick = { onNameCreate(name) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}

@Composable
fun JournalNameChangeDialog(onDismissRequest: () -> Unit, onNameChange: (String) -> Unit, initial: String){
    var name by remember { mutableStateOf(initial) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Change Journal Name") },
        text = {
            TextField(value = name, onValueChange = {
                name = it
            })
        },
        confirmButton = { Button(onClick = { onNameChange(name) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}

@Composable
fun DeleteWarningDialog(onDismissRequest: () -> Unit, onDelete: () -> Unit){

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Are you sure you want to DELETE?") },

        confirmButton = { Button(onClick = { onDelete() }) { Text("YES") } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("NO") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoodDialog(
    onDismiss: () -> Unit,
    onSave: (LocalDate, Float) -> Unit
) {
    var moodValue by remember { mutableStateOf(2f) }
    var dateText by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf<String?>(null) }

    val todayString = remember {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        LocalDate.now().format(formatter)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("How do you feel?") },
        text = {
            Column {

                // Mood slider
                Slider(
                    value = moodValue,
                    onValueChange = { moodValue = it },
                    valueRange = 1f..4f,
                    steps = 2
                )

                Spacer(Modifier.height(16.dp))

                // TextField date input
                OutlinedTextField(
                    value = dateText,
                    onValueChange = {
                        dateText = it
                        dateError = null
                    },
                    label = { Text("Date (dd/MM/yyyy)") },
                    isError = dateError != null,
                    singleLine = true,
                    placeholder = { Text(todayString) }
                )

                if (dateError != null) {
                    Text(
                        text = dateError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                try {
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val date = LocalDate.parse(dateText.ifEmpty { todayString }, formatter)

                    onSave(date, moodValue)
                    onDismiss()

                } catch (e: Exception) {
                    dateError = "Invalid date format"
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}



@Composable
fun ChangeInputDialog(onDismissRequest: () -> Unit, onNameChange: (String) -> Unit, initial: String){
    var name by remember { mutableStateOf(initial) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Change Page Name") },
        text = {
            TextField(value = name, onValueChange = {
                name = it
            })
        },
        confirmButton = { Button(onClick = { onNameChange(name) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}
