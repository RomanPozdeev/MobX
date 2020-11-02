package com.example.mobxexample.domain

import com.example.mobxexample.data.Joke

sealed class JokeState {
    object Loading : JokeState()
    class Ready(val joke: Joke) : JokeState()
}
