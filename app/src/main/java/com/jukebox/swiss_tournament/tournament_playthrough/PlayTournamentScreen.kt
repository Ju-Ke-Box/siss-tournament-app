package com.jukebox.swiss_tournament.tournament_playthrough

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.TRFxFileHandler
import com.jukebox.swiss_tournament.data.model.Tournament
import org.apache.commons.lang3.StringUtils
import java.io.File

@Composable
fun PlayTournamentScreen(
    tournamentInfo: Tournament,
    players: List<Player>,
    viewModel: PlayTournamentViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    filesDir: File
) {
    val trFxFileHandler = TRFxFileHandler()
    LaunchedEffect(key1 = true) {
        trFxFileHandler.createInitialTRFxFile(tournamentInfo, players, filesDir)
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




