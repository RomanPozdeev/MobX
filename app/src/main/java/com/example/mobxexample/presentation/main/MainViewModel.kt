package com.example.mobxexample.presentation.main

import androidx.lifecycle.ViewModel
import com.example.mobxexample.domain.TodoListStore
import com.example.mobxexample.presentation.main.model.Task

class MainViewModel(val todoListStore: TodoListStore) : ViewModel() {

    val tasks = todoListStore.todoList

    fun onAddTaskClicked(task: Task) {
        todoListStore.addTask(task)
    }

    fun onClearAllTasksClicked() {
        todoListStore.clearAllTasks()
    }

    fun taskCount(): Int = todoListStore.count

    fun onTaskClicked(task: Task) {
        todoListStore.changeTaskStatus(task)
    }
}
