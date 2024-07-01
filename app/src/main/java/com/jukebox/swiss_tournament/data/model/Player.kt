package com.jukebox.swiss_tournament.data.model

data class Player(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val title: String = ""
) {
    companion object {
        val byePlayer = Player(0, "bye", "(spielfrei)")
    }
}

