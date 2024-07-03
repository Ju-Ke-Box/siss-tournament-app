package com.jukebox.swiss_tournament.tournament_results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jukebox.swiss_tournament.data.model.Player

class TournamentResultsViewModel(
): ViewModel() {
    lateinit var players: List<Player>
    companion object {
        fun Factory() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TournamentResultsViewModel() as T
            }
        }
    }

}