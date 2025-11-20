package com.example.scribesoul.models

import JournalSection
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalDate
import java.util.Date

data class Habit(
    val id: Int,
    val uid: Int,
    val habitName: String,
    val metric: String,
    val iconChoice: Int,
    val value: SnapshotStateList<Pair<LocalDate, Int>>,
    val goal: Int,
)