package com.example.mobxexample.data

import retrofit2.Call
import retrofit2.http.GET

interface JokesApi {
    @GET("jokes/random")
    fun randomJoke(): Call<Joke>
}