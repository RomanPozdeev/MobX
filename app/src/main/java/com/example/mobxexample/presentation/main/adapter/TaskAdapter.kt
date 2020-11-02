package com.example.mobxexample.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mobxexample.databinding.ItemTaskBinding
import com.example.mobxexample.domain.Task

class TaskAdapter(private val listener: (Task) -> Unit) : RecyclerView.Adapter<TaskViewHolder>() {

    private object DiffCallback : DiffUtil.ItemCallback<Task>() {

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.description == newItem.description &&
                oldItem.done == newItem.done &&
                oldItem.title == newItem.title
        }
    }

    private val differ = AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding) { position -> listener.invoke(differ.currentList[position]) }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) =
        holder.bind(differ.currentList[position])

    override fun onViewRecycled(holder: TaskViewHolder) {
        holder.unbind()
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun setTasks(tasks: List<Task>) {
        differ.submitList(tasks.toList())
    }
}
