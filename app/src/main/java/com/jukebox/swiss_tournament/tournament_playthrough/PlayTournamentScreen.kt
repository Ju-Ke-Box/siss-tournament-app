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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jukebox.swiss_tournament.data.model.PossibleDisplayResults
import com.jukebox.swiss_tournament.data.model.PossibleStoreResult

@Composable
fun PlayTournamentScreen(
    viewModel: PlayTournamentViewModel,
    navController: NavController,
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
            items(viewModel.currentPairings.keys.toList()) {
                val whitePlayer = viewModel.getPlayerById(it.first)
                val blackPlayer = viewModel.getPlayerById(it.second)
                val initialResultPickerOption =
                    when (viewModel.currentPairings[it]) {
                        PossibleStoreResult.blackWon -> PossibleDisplayResults.blackWon
                        PossibleStoreResult.whiteWon -> PossibleDisplayResults.whiteWon
                        else -> PossibleDisplayResults.ongoing
                    }

                Row (
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(4.dp)
                ){
                    Text(
                        text = "${whitePlayer.lastName}, ${whitePlayer.firstName}",
                        modifier = Modifier.weight(1f)
                            .border(1.dp, MaterialTheme.colorScheme.tertiary)
                            .padding(4.dp)
                    )
                    Text(
                        text = " vs. ",
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${blackPlayer.lastName}, ${blackPlayer.firstName}",
                        modifier = Modifier.weight(1f)
                            .border(1.dp, MaterialTheme.colorScheme.tertiary)
                            .padding(4.dp)
                    )
                    ResultPicker(
                        options = listOf(PossibleDisplayResults.whiteWon, PossibleDisplayResults.blackWon, PossibleDisplayResults.remis),
                        onResultChange = {newResult ->
                            when(newResult) {
                                PossibleDisplayResults.whiteWon -> {
                                    viewModel.currentPairings[it] = PossibleStoreResult.whiteWon
                                }
                                PossibleDisplayResults.blackWon -> {
                                    viewModel.currentPairings[it] = PossibleStoreResult.blackWon
                                }
                                PossibleDisplayResults.remis -> {
                                    viewModel.currentPairings[it] = PossibleStoreResult.remis
                                }
                            }
                        },
                        inititalOption = initialResultPickerOption,
                        modifier = Modifier.width(120.dp)
                            .padding(start = 4.dp, end = 4.dp)
                    )
                }
            }
        }

    }

}




