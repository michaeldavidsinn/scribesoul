package com.scribesoul.app.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalDate

data class Habit(
    val id: Int,
    val uid: Int,
    val habitName: String,
    val metric: String,
    val iconChoice: Int,
    val value: SnapshotStateList<Pair<LocalDate, Int>>,
    val goal: Int,
)