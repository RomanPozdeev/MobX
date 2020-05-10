package com.example.mobxexample.domain

import com.example.mobxexample.presentation.main.util.TaskGenerator
import mobx.collections.toObservableList
import mobx.core.action
import mobx.core.computed
import mobx.core.observable

class TodoListStore {
    var todoList by observable(mutableListOf<Task>().toObservableList())
        private set

    val count by computed { todoList.size }
    val pending by computed { todoList.count { !it.done } }
    val done by computed { count - pending }

    init {
        todoList.addAll(TaskGenerator.getTasks())
    }

    override fun toString(): String = todoList.joinToString("\n")

    fun addTask(task: Task) = action("addTask") {
        todoList.add(task)
    }

    fun clearAllTasks() = action("clearAllTasks") {
        todoList.clear()
    }

    fun changeTaskStatus(task: Task) = action("toggle task") {
        task.done = !task.done
    }
}
