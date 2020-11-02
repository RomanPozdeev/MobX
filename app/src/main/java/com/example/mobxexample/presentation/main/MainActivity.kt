package com.example.mobxexample.presentation.main

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobxexample.R
import com.example.mobxexample.databinding.ActivityMainBinding
import com.example.mobxexample.domain.JokeState
import com.example.mobxexample.domain.Task
import com.example.mobxexample.presentation.main.adapter.TaskAdapter
import com.example.mobxexample.presentation.utils.mobx.observeChanges
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private val taskAdapter = TaskAdapter { task ->
        viewModel.onTaskClicked(task)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory())
            .get(MainViewModel::class.java)

        initViews()
    }

    private fun initViews() {
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        with(binding.todoList) {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }

        binding.addTask.setOnClickListener {
            showNewTaskDialog()
        }

        binding.clear.setOnClickListener {
            viewModel.onClearAllTasksClicked()
        }

        observeChanges(viewModel.todoListStore::count) {
            binding.total.text = getString(R.string.total, it)
        }

        observeChanges(viewModel.todoListStore::done) {
            binding.done.text = getString(R.string.done, it)
        }

        observeChanges({ viewModel.todoListStore.pending }) {
            binding.pending.text = getString(R.string.pending, it)
        }

        observeChanges(viewModel::tasks) {
            taskAdapter.setTasks(it)
        }

        observeChanges(viewModel.jokeStore::jokeState) {
            when (it) {
                JokeState.Loading -> binding.joke.text = "Please, wait"
                is JokeState.Ready -> binding.joke.text = it.joke.value
            }
        }
    }

    private fun showNewTaskDialog() {
        MaterialAlertDialogBuilder(this)
            .setView(R.layout.alert_dialog)
            .setPositiveButton(getString(R.string.save)) { dialog, _ ->
                val title = (dialog as Dialog).findViewById<TextInputEditText>(R.id.titleTask)
                    .text.toString()
                val description =
                    dialog.findViewById<TextInputEditText>(R.id.descriptionTask).text.toString()
                viewModel.onAddTaskClicked(
                    Task(
                        viewModel.taskCount(),
                        title,
                        description
                    )
                )
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .setTitle(getString(R.string.new_task))
            .setCancelable(false)
            .create()
            .show()
    }
}
