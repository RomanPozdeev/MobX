package com.example.mobxexample.presentation.main.util

import com.example.mobxexample.domain.Task

object TaskGenerator {
    private const val DefaultTaskCount = 5
    fun getTasks(): MutableList<Task> {
        val tasks = mutableListOf<Task>()
        repeat(DefaultTaskCount) {
            tasks.add(
                Task(
                    it,
                    "Task_$it",
                    "Description_$it"))
        }
        return tasks
    }
}
