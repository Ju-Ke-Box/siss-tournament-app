package com.jukebox.swiss_tournament.tournament_results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TournamentResultsScreen(
    viewModel: TournamentResultsViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        item {
            Text(
                text = "Ergebnisse",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        item {
            Row {
                Text(text = "# ")
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Pkte")
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Nachname, Vorname")
            }
        }
        item { Divider() }
        items(viewModel.players.sortedBy { it.rank }) {player ->
            Row (
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(text = "${player.rank}".padEnd(4))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "${player.points}".padStart(4))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "${player.lastName}, ${player.firstName}")
            }
        }
    }
}