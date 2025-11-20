package com.example.scribesoul.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scribesoul.models.Habit
import java.time.LocalDate

class HomeViewModel : ViewModel() {
    private val _habits = mutableStateListOf<Habit>()
    val habits: List<Habit> get() = _habits

    var currentDay by mutableStateOf(LocalDate.now())
        private set

    var dates = mutableStateListOf<LocalDate>()

    // Currently displayed habit values
    var exercise by mutableIntStateOf(0)
    var drink by mutableIntStateOf(0)
    var meditation by mutableIntStateOf(0)
    var running by mutableIntStateOf(0)
    var read by mutableIntStateOf(0)

    init {
        // Create a 7-day week range
        val today = currentDay
        val daysToSubtract = today.dayOfWeek.value % 7L
        val startOfWeek = today.minusDays(daysToSubtract)
        for (i in 0..6) {
            dates.add(startOfWeek.plusDays(i.toLong()))
        }


    }

    fun addHabit(name: String, metric: String, icon: Int, goal: Int) {
        val newHabit = Habit(
            id = _habits.size + 1,
            uid = 0,
            habitName = name,
            metric = metric,
            iconChoice = icon,
            value = mutableStateListOf(),
            goal = goal
        )
        _habits.add(newHabit)
    }

    // ────────────────────────────────────────────────────────────────────────────────
    // 2. Get value for specific habit on a specific day
    // ────────────────────────────────────────────────────────────────────────────────
    fun getValueForDay(habit: Habit, day: LocalDate): Int {
        return habit.value.firstOrNull { it.first == day }?.second ?: 0
    }

    // ────────────────────────────────────────────────────────────────────────────────
    // 3. Update a habit for a specific day
    // ────────────────────────────────────────────────────────────────────────────────
    fun updateHabitForDay(habit: Habit, day: LocalDate, newValue: Int) {
        val existing = habit.value.firstOrNull { it.first == day }

        if (existing != null) {
            // Replace existing entry
            val index = habit.value.indexOf(existing)
            habit.value[index] = day to newValue
        } else {
            // Create new entry
            habit.value.add(day to newValue)
        }
    }

    // ────────────────────────────────────────────────────────────────────────────────
    // 4. Switch selected day
    // ────────────────────────────────────────────────────────────────────────────────
    fun switchDay(newDay: LocalDate) {
        currentDay = newDay
    }

    fun habitProgress(habit: Habit,date: LocalDate): Float {
        val v = getValueForDay(habit, date)
        return if (habit.goal <= 0) 0f else (v / habit.goal.toFloat()).coerceIn(0f, 1f)
    }

    // ────────────────────────────────────────────────────────────────────────────────
    // 5. Update all habits for current day (UI helper)
    // ────────────────────────────────────────────────────────────────────────────────
    fun updateHabitValue(habitId: Int, newValue: Int) {
        val habit = _habits.firstOrNull { it.id == habitId } ?: return
        updateHabitForDay(habit, currentDay, newValue)
    }

    fun overallProgress(date: LocalDate): Float {
        if (habits.isEmpty()) return 0f

        var totalProgress = 0f

        habits.forEach { habit ->
            val todayValue = habit.value.find { it.first == date }?.second ?: 0
            val progress = (todayValue.toFloat() / habit.goal.toFloat()).coerceIn(0f, 1f)
            totalProgress += progress
        }

        return (totalProgress / habits.size).coerceIn(0f, 1f)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { HomeViewModel() }
        }
    }
}
