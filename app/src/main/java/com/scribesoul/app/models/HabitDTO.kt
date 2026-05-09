package com.scribesoul.app.models

import androidx.compose.runtime.mutableStateListOf
import java.time.LocalDate

// 1. The DTO that Firebase can read/write
data class HabitDTO(
    val id: Int = 0,
    val habitName: String = "",
    val metric: String = "",
    val iconChoice: Int = 1,
    val goal: Int = 0,
    val values: Map<String, Int> = emptyMap() // Maps "YYYY-MM-DD" string to the value
)

// 2. Mapper: UI -> DTO (Saving)
fun Habit.toDTO(): HabitDTO {
    // Convert List<Pair<LocalDate, Int>> to a Map<String, Int>
    val valuesMap = this.value.associate { it.first.toString() to it.second }

    return HabitDTO(
        id = this.id,
        habitName = this.habitName,
        metric = this.metric,
        iconChoice = this.iconChoice,
        goal = this.goal,
        values = valuesMap
    )
}

// 3. Mapper: DTO -> UI (Loading)
fun HabitDTO.toUIModel(): Habit {
    val snapshotList = mutableStateListOf<Pair<LocalDate, Int>>()

    // Convert strings back to LocalDate
    this.values.forEach { (dateStr, intValue) ->
        snapshotList.add(LocalDate.parse(dateStr) to intValue)
    }

    return Habit(
        id = this.id,
        uid = 0, // Not needed locally
        habitName = this.habitName,
        metric = this.metric,
        iconChoice = this.iconChoice,
        value = snapshotList,
        goal = this.goal
    )
}