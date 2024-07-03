package com.jukebox.swiss_tournament.tournament_playthrough

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.StoreResult

@Composable
fun PlayTournamentScreen(
    viewModel: PlayTournamentViewModel,
    showResults : (players: List<Player>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = viewModel.tournamentInfo.name,
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "${viewModel.currentRound + 1} / ${viewModel.tournamentInfo.numOfRounds}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(viewModel.currentPairings.keys.toList()) { pair ->
                val whitePlayer = viewModel.getPlayerById(pair.first)
                val blackPlayer = viewModel.getPlayerById(pair.second)

                //Hack to reset selectedOption at start of new round
                //noinspection UnrememberedMutableState
                val selectedOption = mutableStateOf(viewModel.currentPairings[pair]!!)

                Row (
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(4.dp)
                ){
                    Text(
                        text = "${whitePlayer.lastName}, ${whitePlayer.firstName}",
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, MaterialTheme.colorScheme.tertiary)
                            .padding(4.dp)
                    )
                    Text(
                        text = " vs. ",
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${blackPlayer.lastName}, ${blackPlayer.firstName}",
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, MaterialTheme.colorScheme.tertiary)
                            .padding(4.dp)
                    )
                    ResultPicker(
                        options = listOf(StoreResult.whiteWon, StoreResult.blackWon, StoreResult.remis),
                        onResultChange = { viewModel.currentPairings[pair] = it },
                        selectedOption = selectedOption,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(start = 4.dp, end = 4.dp)
                    )
                }
            }
        }
        if (viewModel.currentPairings.values.none { it == StoreResult.ongoing }) {
            if (viewModel.currentRound +1 >= viewModel.tournamentInfo.numOfRounds) {
                Button(onClick = {
                    viewModel.startNextRound()
                    showResults(viewModel.players)
                }) {
                    Text(text = "Ergebnisse")
                }
            } else {
                Button(onClick = {
                    viewModel.startNextRound()
                }) {
                    Text(text = "Nächste Runde")
                }
            }
        }
    }

}




