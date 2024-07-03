package com.jukebox.swiss_tournament.tournament_playthrough

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.StoreResult
import com.jukebox.swiss_tournament.data.model.Tournament
import com.jukebox.swiss_tournament.data.pairing_engine.JaVaFoHandler
import com.jukebox.swiss_tournament.data.pairing_engine.TRFxFileHandler
import java.io.File


class PlayTournamentViewModel(
    private val filesDir: File,
): ViewModel() {
    lateinit var tournamentInfo: Tournament
    var players: List<Player> = listOf()
    var currentRound by mutableIntStateOf(0)
    var currentPairings: SnapshotStateMap<Pair<Int, Int>, String> = mutableStateMapOf() //(white,black)->result

    private lateinit var trfxFileHandler: TRFxFileHandler
    private val javafoHandler = JaVaFoHandler()

    fun getPlayerById(id: Int): Player {
        return players.find { player -> player.id == id }?: Player.byePlayer
    }

    fun initialize(tournamentInfo: Tournament, players: List<Player>) {
        this.tournamentInfo = tournamentInfo
        this.players = players
        trfxFileHandler = TRFxFileHandler(this.filesDir, tournamentInfo)
        val filename = trfxFileHandler.createInitialTRFxFile(this.players)
        getPairings(filename)
    }

    fun startNextRound() {
        if (currentPairings.values.any { it == StoreResult.ongoing }) {
            return
        }
        calculatePoints()
        trfxFileHandler.addPointsToFile(currentRound, players)
        calculateRanks()
        trfxFileHandler.addRanksToFile(currentRound, players)
        trfxFileHandler.appendRoundResultsToFile(currentRound, currentPairings)
        if (currentRound++ < tournamentInfo.numOfRounds-1) {
            val filename = trfxFileHandler.createNextRoundFile(currentRound)
            getPairings(filename)
        }
        //TODO else show results

    }

    private fun getPairings(filename: String) {
        val pairings = javafoHandler.getPairings(filename)
        currentPairings = mutableStateMapOf()
        pairings.forEach { (k, v) -> currentPairings[k]=v }
    }

    fun calculatePoints() {
        for (pair in currentPairings.keys) {
            val whitePlayer = getPlayerById(pair.first)
            val blackPlayer = getPlayerById(pair.second)
            var whitePoints = 0.0f
            var blackPoints = 0.0f
            when (currentPairings[pair]) {
                StoreResult.remis -> {
                    whitePoints = 0.5f
                    blackPoints = 0.5f
                }
                StoreResult.whiteWon -> {
                    whitePoints = 1.0f
                }
                StoreResult.blackWon -> {
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