package com.example.mobxexample.domain

import android.os.CountDownTimer
import com.example.mobxexample.data.Joke
import com.example.mobxexample.data.JokesApi
import mobx.core.observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class JokeStore {
    private val jockesService: JokesApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        jockesService = retrofit.create(JokesApi::class.java)
    }

    private val counter = object : CountDownTimer(Long.MAX_VALUE, TimeUnit.SECONDS.toMillis(5L)) {
        override fun onFinish() {}

        override fun onTick(millisUntilFinished: Long) {
            jockesService.randomJoke().enqueue(object : Callback<Joke> {
                override fun onFailure(call: Call<Joke>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<Joke>, response: Response<Joke>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            //should be called on the main thread
                            jokeState = JokeState.Ready(it)
                        }
                    }
                }
            })
        }

    }

    var jokeState by observable<JokeState>(JokeState.Loading)
        private set

    fun subscribe() {
        counter.start()
    }

    fun unsubscribe() {
        counter.cancel()
    }
}