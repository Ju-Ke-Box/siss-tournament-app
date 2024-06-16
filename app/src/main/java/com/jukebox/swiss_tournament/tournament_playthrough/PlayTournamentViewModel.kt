package com.jukebox.swiss_tournament.tournament_playthrough

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.RoundResult
import com.jukebox.swiss_tournament.data.model.Tournament


class PlayTournamentViewModel(
    val tournament: Tournament,
    val players: List<Player>
): ViewModel() {
    var currentRound by mutableIntStateOf(0)
    var currentPairings: SnapshotStateMap<Int, Int> = mutableStateMapOf()
    var currentRoundResults = mutableStateListOf(listOf<RoundResult>())

    val isLoading by mutableStateOf(false)

    private fun storeResults() {

    }

    private fun createPairings() {

    }

    fun startNewRound() {

    }
}