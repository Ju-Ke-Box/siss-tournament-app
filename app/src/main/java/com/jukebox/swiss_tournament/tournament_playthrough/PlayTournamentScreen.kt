package com.jukebox.swiss_tournament.tournament_playthrough

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.Tournament

@Composable
fun PlayTournamentScreen(
    tournamentInfo: Tournament,
    players: List<Player>,
    viewModel: PlayTournamentViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = true) {
        //create TRFx File from tournamentInfo an players
        //calculate initial pairings
        //show initial pairings
        // wait for results
        //calculate standings (points + rank) -> update in TRFx File + store in ViewModel for faster access?
        //for each remaining round -> repeat

    }

    Column {
        Text(viewModel.tournamentInfo.name)
        Text(""+viewModel.tournamentInfo.numOfRounds)
        for (player in viewModel.players) {
            Text(text = "${player.id}: ${player.firstName} ${player.lastName}")
        }
    }

}