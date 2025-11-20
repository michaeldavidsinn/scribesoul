package com.example.scribesoul.utils

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
