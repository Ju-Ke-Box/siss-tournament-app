package com.jukebox.swiss_tournament.data.model

data class RoundResult (
    val playerId: Int,
    val playerColor: String,
    val opponentId: Int,
    var result: String
)

data object PossibleResults {
    val whiteWon = "1-0"
    val blackWon = "0-1"
    val remis = "½-½"
    val ongoing = ""
}

data object PossibleColors {
    val white = "weiß"
    val black = "schwarz"
}