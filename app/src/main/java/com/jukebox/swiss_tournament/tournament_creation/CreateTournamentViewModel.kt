package com.jukebox.swiss_tournament.tournament_creation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.Tournament

class CreateTournamentViewModel: ViewModel() {
    var name by mutableStateOf("")
    var numOfRounds by mutableStateOf(0)
    var city by mutableStateOf("")
    var federation by mutableStateOf("")
    var startDate by mutableStateOf("")
    var endDate by mutableStateOf(startDate)
    var arbiters by mutableStateOf("")

    val players : SnapshotStateList<Player> = mutableStateListOf()

    var isEditingPlayer by mutableStateOf(false)
    var isAddingPlayer by mutableStateOf(false)

    var addedPlayerFirstName by mutableStateOf("")
    var addedPlayerLastName by mutableStateOf("")

    fun addPlayer() {
        if (addedPlayerFirstName.isNotBlank() && addedPlayerLastName.isNotBlank()) {
            players.add(
                Player(
                    findNextPlayerId(players),
                    addedPlayerFirstName,
                    addedPlayerLastName
                )
            )
        }
        isAddingPlayer = false
        addedPlayerFirstName = ""
        addedPlayerLastName = ""
    }

    fun editPlayer() {

    }

    fun deletePlayer() {

    }

    private fun findNextPlayerId(players: List<Player>): Int {
        if (players.isEmpty()) {
            return 0
        }
        var max = players[0].id
        for (player in players) {
            if (player.id > max) {
                max = player.id
            }
        }
        return max + 1
    }

    fun generateTournamentInfo(): Tournament {
        return Tournament(
            name, numOfRounds, startDate, endDate, city, federation, arbiters.split(","))
    }

    companion object {
        val Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CreateTournamentViewModel() as T
            }
        }
    }
}