package com.example.mobxexample.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Joke(
    @Json(name = "value")
    val value: String
)