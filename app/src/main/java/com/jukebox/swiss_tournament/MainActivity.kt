package com.jukebox.swiss_tournament

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jukebox.swiss_tournament.tournament_creation.CreateTournamentScreen
import com.jukebox.swiss_tournament.tournament_creation.CreateTournamentViewModel
import com.jukebox.swiss_tournament.tournament_playthrough.PlayTournamentScreen
import com.jukebox.swiss_tournament.tournament_playthrough.PlayTournamentViewModel
import com.jukebox.swiss_tournament.ui.theme.SwissTournamentTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwissTournamentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val createTournamentViewModel: CreateTournamentViewModel by viewModels(
                        factoryProducer = { CreateTournamentViewModel.Factory}
                    )
                    val playTournamentViewModel: PlayTournamentViewModel by viewModels(
                        factoryProducer = { PlayTournamentViewModel.Factory}
                    )

                    Scaffold(
                        topBar = { TopAppBar(
                            title = { Text(text = "Swiss Tournament App") },
                        )},
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screens.StartScreen,
                            modifier = Modifier.padding(it),
                        ) {
                            composable(
                                route = Screens.StartScreen,
                                content = { 
                                    StartScreen(
                                        createTournament = {
                                            createTournamentViewModel.reset()
                                            navController.navigate(Screens.CreateTournamentScreen)
                                        }
                                    )
                                }
                            )
                            composable(
                                route = Screens.CreateTournamentScreen,
                                content = {
                                    CreateTournamentScreen(
                                        viewModel = createTournamentViewModel,
                                        playTournament = {tournamentInfo, players ->
                                            playTournamentViewModel.tournamentInfo = tournamentInfo
                                            playTournamentViewModel.players = players
                                            navController.navigate(Screens.PlayTournamentScreen)
                                        }
                                    )
                                }
                            )
                            composable(
                                route = Screens.PlayTournamentScreen,
                                content = {
                                    PlayTournamentScreen(
                                        tournamentInfo = playTournamentViewModel.tournamentInfo,
                                        players = playTournamentViewModel.players,
                                        viewModel = playTournamentViewModel,
                                        navController = navController
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun StartScreen(
        createTournament: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Button(
                onClick = { createTournament() },
            ) {
                Text(text = "Neues Tunier erstellen")
            }
        }
    }

    object Screens {
        const val StartScreen = "startScreen"
        const val CreateTournamentScreen = "createTournamentScreen"
        const val PlayTournamentScreen = "playTournamentScreen"
    }

}