package com.example.scribesoul.viewModels

import JournalSection
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class TodoPageViewModel(

) : ViewModel()  {
    val todos = mutableStateListOf<Pair<String, Boolean>>()

    init {
        // Example preload (could come from DB)
        todos.addAll(
            listOf(
                "Buy groceries" to false,
                "Write journal entry" to true
            )
        )
    }

    fun addTodo(task: String) {
        todos.add(task to false)
    }

    fun toggleTodo(index: Int) {
        val item = todos[index]
        todos[index] = item.copy(second = !item.second)
    }

//    fun toPage(): JournalPage.TodoPage {
//        return JournalPage.TodoPage(
//            id = pageId,
//            todoList = todos.toList()
//        )
//    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { TodoPageViewModel() }
        }
    }
}