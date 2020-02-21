package com.example.mobxexample.presentation.main.adapter

import android.text.SpannableString
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mobxexample.presentation.main.model.Task
import com.example.mobxexample.presentation.main.util.setStrikeThroughSpan
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_task.view.description
import kotlinx.android.synthetic.main.item_task.view.title
import mobx.core.Disposable
import mobx.core.autorun

class TaskViewHolder(
    override val containerView: View,
    clickListener: (Int) -> Unit
) : ViewHolder(containerView), LayoutContainer {

    private val disposables = mutableSetOf<Disposable>()

    init {
        containerView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                clickListener.invoke(adapterPosition)
            }
        }
    }

    fun bind(task: Task) {
        autorun {
            containerView.title.text = task.title
            containerView.description.text = task.description

            val title = SpannableString(task.title)
            val description = SpannableString(task.description)
            if (task.done) {
                title.setStrikeThroughSpan()
                description.setStrikeThroughSpan()
            }
            containerView.title.text = title
            containerView.description.text = description
        }.apply {
            disposables.add(this)
        }
    }

    fun unbind() {
        disposables.forEach { it.dispose() }
        disposables.clear()
    }
}
