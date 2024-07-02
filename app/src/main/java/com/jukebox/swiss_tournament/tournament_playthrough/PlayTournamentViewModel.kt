package com.jukebox.swiss_tournament.tournament_playthrough

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.PossibleStoreResult
import com.jukebox.swiss_tournament.data.model.Tournament
import com.jukebox.swiss_tournament.data.pairing_engine.JaVaFoHandler
import com.jukebox.swiss_tournament.data.pairing_engine.TRFxFileHandler
import java.io.File


class PlayTournamentViewModel(
    private val filesDir: File,
): ViewModel() {
    var tournamentInfo: Tournament = Tournament("", 0, "")
    var players: List<Player> = listOf()
    var currentRound by mutableIntStateOf(0)
    var currentPairings: SnapshotStateMap<Pair<Int, Int>, String> = mutableStateMapOf() //(white,black)->result

    fun getPlayerById(id: Int): Player {
        return players.find { player -> player.id == id }?: Player.byePlayer
    }

    fun initialize(tournamentInfo: Tournament, players: List<Player>) {
        this.tournamentInfo = tournamentInfo
        this.players = players
        val trFxFileHandler = TRFxFileHandler(this.filesDir, tournamentInfo)
        val javafoHandler = JaVaFoHandler()
        val filename = trFxFileHandler.createInitialTRFxFile(this.players)
        val pairings = javafoHandler.getPairings(filename)
        pairings.forEach { (k, v) -> currentPairings[k]=v }
    }

    fun startNextRound() {
        calculatePoints()
        calculateRanks()
        //add to trfx file
        //increment round
        //calculate next pairings (new file)

    }

    fun calculatePoints() {
        for (pair in currentPairings.keys) {
            val whitePlayer = getPlayerById(pair.first)
            val blackPlayer = getPlayerById(pair.second)
            var whitePoints = 0.0f
            var blackPoints = 0.0f
            when (currentPairings[pair]) {
                PossibleStoreResult.remis -> {
                    whitePoints = 0.5f
                    blackPoints = 0.5f
                }
                PossibleStoreResult.whiteWon -> {
                    whitePoints = 1.0f
                }
                PossibleStoreResult.blackWon -> {
                    blackPoints = 1.0f
                }
            }
            whitePlayer.points += whitePoints
            blackPlayer.points += blackPoints
        }
    }

    fun calculateRanks() {
        val sortedPlayers = players.sortedByDescending { player -> player.points }
        var rank = 1
        var inc = 0 // 0 because the first player will match its score and inc++
        var lastPoints = sortedPlayers[0].points
        for (player in sortedPlayers) {
            if (player.points == lastPoints) {
                player.rank = rank
                inc++
            } else {
                rank += inc
                player.rank = rank
                inc = 1
                lastPoints = player.points
            }
        }
    }


    companion object {
        fun Factory(filesDir: File) : ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayTournamentViewModel(
                        filesDir
                    ) as T
                }
            }
        }
    }
}