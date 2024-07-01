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
    filesDir: File,
): ViewModel() {
    var tournamentInfo: Tournament = Tournament("", 0, "")
    var players: List<Player> = listOf()
    var points: MutableList<Float> = mutableStateListOf()
    var ranks: MutableList<Int> = mutableStateListOf()
    var currentRound by mutableIntStateOf(0)
    var currentPairings: SnapshotStateMap<Pair<Int, Int>, String> = mutableStateMapOf() //(white,black)->result

    private val trFxFileHandler = TRFxFileHandler(filesDir)
    private val javafoHandler = JaVaFoHandler()
    fun getPlayerById(id: Int): Player {
        return players.find { player -> player.id == id }?: Player.byePlayer
    }

    fun initialize(tournamentInfo: Tournament, players: List<Player>) {
        this.tournamentInfo = tournamentInfo
        this.players = players
        players.forEach { _ -> this.points.add(0.0f)}
        players.forEach { _ -> this.ranks.add(1)}
        val filename = trFxFileHandler.createInitialTRFxFile(this.tournamentInfo, this.players)
        val pairings = javafoHandler.getPairings(filename)
        pairings.forEach { (k, v) -> currentPairings[k]=v }
    }

    fun startNextRound() {
        calculatePoints()
        calculateRanks()
    }

    fun calculatePoints() {
        for (pair in currentPairings.keys) {
            val whiteIndex = pair.first -1
            val blackIndex = pair.second -1
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
            if (whiteIndex != -1) {
                points[whiteIndex] += whitePoints
            }
            if (blackIndex != -1) {
                points[blackIndex] += blackPoints
            }
        }
    }

    fun calculateRanks() {
        val sortedPlayers = players.sortedByDescending { player -> points[player.id-1] }
        var rank = 1
        var inc = 0 // 0 because the first player will match its score and inc++
        var lastPoints = points[sortedPlayers[0].id -1]
        for (player in sortedPlayers) {
            val index = player.id-1
            if (points[index] == lastPoints) {
                ranks[index] = rank
                inc++
            } else {
                rank += inc
                ranks[index] = rank
                inc = 1
                lastPoints = points[index]
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