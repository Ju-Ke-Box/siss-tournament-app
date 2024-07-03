package com.jukebox.swiss_tournament.tournament_results

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TournamentResultsScreen(
    viewModel: TournamentResultsViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        item { 
            Text(text = "Rang\tPunkte\tNachname, Vorname")
        }
        items(viewModel.players.sortedBy { it.rank }) {player ->
            Text(text = "${player.rank}\t${player.points}\t${player.lastName}, ${player.firstName}")
        }
    }
}