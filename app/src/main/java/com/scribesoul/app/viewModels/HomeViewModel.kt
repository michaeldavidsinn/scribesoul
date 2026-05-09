package com.scribesoul.app.viewModels

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.scribesoul.app.models.Habit
import com.scribesoul.app.models.User
import com.scribesoul.app.models.toDTO
import com.scribesoul.app.models.toUIModel
import com.scribesoul.app.repository.FirebaseHabitRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class HomeViewModel(
    private val habitRepository: FirebaseHabitRepository
) : ViewModel() {
    var userName by mutableStateOf("User")
        private set

    val currentTime = LocalTime.now()


    private val _habits = mutableStateListOf<Habit>()
    val habits: SnapshotStateList<Habit> get() = _habits

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
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            // Use the display name if they have one, otherwise use the first part of their email
            userName = firebaseUser.displayName?.takeIf { it.isNotBlank() }
                ?: firebaseUser.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() }
                        ?: "User"
        }
        // Create a 7-day week rangez
        val today = currentDay
        val daysToSubtract = today.dayOfWeek.value % 7L
        val startOfWeek = today.minusDays(daysToSubtract)
        for (i in 0..6) {
            dates.add(startOfWeek.plusDays(i.toLong()))
        }

        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            val loadedDTOs = habitRepository.getHabits()
            _habits.clear()
            _habits.addAll(loadedDTOs.map { it.toUIModel() })
        }
    }

    fun getGreetinng(): String{
        if(currentTime < LocalTime.of(12,0) && currentTime > LocalTime.of(0,0)){
            return "Good Morning"
        }else if(currentTime >= LocalTime.of(12,0) && currentTime < LocalTime.of(18,0)){
            return "Good Afternoon"
        }else if(currentTime >= LocalTime.of(18,0) && currentTime <= LocalTime.of(23,59)){
            return "Good Night"
        }else{
            return "How's Your Day?"
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
        viewModelScope.launch {
            habitRepository.saveHabit(newHabit.toDTO())
        }
    }

    // ────────────────────────────────────────────────────────────────────────────────
    // NEW: Automatically update steps if a "Steps" habit exists
    // ────────────────────────────────────────────────────────────────────────────────
    fun updateStepHabitIfExists(stepsToday: Int) {
        // Find the habit where the metric is exactly "Steps"
        val stepHabit = _habits.find { it.metric.equals("Steps", ignoreCase = true) }

        if (stepHabit != null) {
            // Only auto-update if the user is currently viewing today's date
            // This prevents the sensor from accidentally overwriting historical data
            if (currentDay == LocalDate.now()) {
                updateHabitForDay(stepHabit, currentDay, stepsToday)
            }
        }
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
    fun updateHabitForDay(habit: Habit, day: LocalDate, newValue: Int, syncToCloud: Boolean = true) {
        val existing = habit.value.firstOrNull { it.first == day }

        if (existing != null) {
            // Replace existing entry
            val index = habit.value.indexOf(existing)
            habit.value[index] = day to newValue
        } else {
            // Create new entry
            habit.value.add(day to newValue)
        }

        if (syncToCloud) {
            viewModelScope.launch {
                habitRepository.saveHabit(habit.toDTO())
            }
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
            initializer {
                // 5. Provide the repository to the ViewModel
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.scribesoul.app.ScribeSoulApplication)
                HomeViewModel(application.container.habitRepository)
            }
        }
    }
}
