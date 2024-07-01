package com.jukebox.swiss_tournament.tournament_playthrough

import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.PossibleResults
import com.jukebox.swiss_tournament.data.model.Tournament
import com.jukebox.swiss_tournament.data.pairing_engine.JaVaFoHandler
import com.jukebox.swiss_tournament.data.pairing_engine.TRFxFileHandler
import java.io.File


class PlayTournamentViewModel(
    var filesDir: File,
): ViewModel() {
    var tournamentInfo: Tournament = Tournament("", 0, "")
    var players: List<Player> = listOf()
    var currentRound by mutableIntStateOf(0)
    var currentPairings: SnapshotStateMap<Pair<Int, Int>, String> = mutableStateMapOf() //(white,black)->result

    val trFxFileHandler = TRFxFileHandler(filesDir)
    val javafoHandler = JaVaFoHandler()

    fun getPlayersByIds(ids: Pair<Int, Int>): Pair<Player, Player> {
        val whiteId = ids.first
        val blackId = ids.second

        val whitePlayer: Player =
            if(blackId == 0) {
                Player(0, "bye", "(spielfrei)")
            } else {
                players[whiteId-1]
            }
        val blackPlayer: Player =
            if(whiteId == 0) {
                Player(0, "bye", "(spielfrei)")
            } else {
                players[blackId-1]
            }
        return Pair(whitePlayer, blackPlayer)
    }

    fun initialize() {
        val filename = trFxFileHandler.createInitialTRFxFile(tournamentInfo, players)
        val pairings = javafoHandler.getPairings(filename)


        //show initial pairings
        // wait for results
        //calculate standings (points + rank) -> update in TRFx File + store in ViewModel for faster access?
        //for each remaining round -> repeat

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