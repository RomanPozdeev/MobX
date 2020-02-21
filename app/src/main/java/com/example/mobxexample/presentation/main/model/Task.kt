package com.example.mobxexample.presentation.main.model

import mobx.core.observable

class Task(val id: Int, title: String, description: String, done: Boolean = false) {
    var title by observable(title)

    var description by observable(description)

    var done by observable(done)
}
