package com.example.mobxexample.presentation.main.adapter

import android.text.SpannableString
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mobxexample.databinding.ItemTaskBinding
import com.example.mobxexample.domain.Task
import com.example.mobxexample.presentation.main.util.setStrikeThroughSpan
import mobx.core.Disposable
import mobx.core.autorun

class TaskViewHolder(
    private val binding: ItemTaskBinding,
    clickListener: (Int) -> Unit
) : ViewHolder(binding.root) {

    private val disposables = mutableSetOf<Disposable>()

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                clickListener.invoke(adapterPosition)
            }
        }
    }

    fun bind(task: Task) {
        unbind()

        autorun {
            binding.title.text = task.title
        }.also {
            disposables.add(it)
        }

        autorun {
            binding.description.text = task.description
        }.also {
            disposables.add(it)
        }

        autorun {
            val title = SpannableString(task.title)
            val description = SpannableString(task.description)
            if (task.done) {
                title.setStrikeThroughSpan()
                description.setStrikeThroughSpan()
            }
            binding.title.text = title
            binding.description.text = description
        }.also {
            disposables.add(it)
        }
    }

    fun unbind() {
        disposables.forEach { it.dispose() }
        disposables.clear()
    }
}
