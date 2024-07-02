package com.jukebox.swiss_tournament.data.model

data object PossibleDisplayResults {
    val whiteWon = "1-0"
    val blackWon = "0-1"
    val remis = "½-½"
    val ongoing = "tbd."
}
data object PossibleStoreResult {
    val whiteWon = "1"
    val blackWon = "0"
    val remis = "="
    val ongoing = ""
    val bye = "F"
}