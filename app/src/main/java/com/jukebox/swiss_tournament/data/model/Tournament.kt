package com.jukebox.swiss_tournament.data.model

data class Tournament (
    val name: String,
    val numOfRounds: Int,
    val city: String = "",
    val federation: String = "",
    val startDate: String,
    val endDate: String = startDate,
    val arbiters: List<String> = listOf("")
)