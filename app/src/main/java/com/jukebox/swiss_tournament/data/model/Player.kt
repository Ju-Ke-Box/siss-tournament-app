package com.jukebox.swiss_tournament.data.model

data class Player(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val title: String = ""
)
