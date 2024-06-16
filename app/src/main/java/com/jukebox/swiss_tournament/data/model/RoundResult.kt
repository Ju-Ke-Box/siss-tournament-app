package com.jukebox.swiss_tournament.data.model

data class RoundResult (
    val playerId: Int,
    val playerColor: String,
    val opponentId: Int,
    var result: String
)