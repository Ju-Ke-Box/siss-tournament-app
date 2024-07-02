package com.jukebox.swiss_tournament.data.model

data class Player(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val title: String = "",
    var points: Float = 0.0f,
    var rank: Int = 0
) {
    companion object {
        val byePlayer = Player(0, "bye", "(spielfrei)")
    }
}

