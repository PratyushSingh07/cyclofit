package com.example.cyclofit.model

data class HeartRate(
    val channel: Channel,
    val feeds: List<Feed>
)