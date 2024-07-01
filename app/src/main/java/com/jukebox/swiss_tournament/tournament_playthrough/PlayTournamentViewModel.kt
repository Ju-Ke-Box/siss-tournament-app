package com.jukebox.swiss_tournament.tournament_playthrough

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.Tournament
import com.jukebox.swiss_tournament.data.pairing_engine.JaVaFoHandler
import com.jukebox.swiss_tournament.data.pairing_engine.TRFxFileHandler
import java.io.File


class PlayTournamentViewModel(
    filesDir: File,
): ViewModel() {
    var tournamentInfo: Tournament = Tournament("", 0, "")
    var players: List<Player> = listOf()
    var currentRound by mutableIntStateOf(0)
    var currentPairings: SnapshotStateMap<Pair<Int, Int>, String> = mutableStateMapOf() //(white,black)->result

    private val trFxFileHandler = TRFxFileHandler(filesDir)
    private val javafoHandler = JaVaFoHandler()
    fun getPlayerById(id: Int): Player {
        return if (id == 0) {
            Player.byePlayer
        } else {
            players[id-1]
        }
    }

    fun initialize(tournamentInfo: Tournament, players: List<Player>) {
        this.tournamentInfo = tournamentInfo
        this.players = players
        val filename = trFxFileHandler.createInitialTRFxFile(this.tournamentInfo, this.players)
        val pairings = javafoHandler.getPairings(filename)
        pairings.forEach { (k, v) -> currentPairings[k]=v }

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