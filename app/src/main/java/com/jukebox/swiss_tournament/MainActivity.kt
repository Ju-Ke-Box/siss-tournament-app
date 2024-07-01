package com.jukebox.swiss_tournament

import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jukebox.swiss_tournament.tournament_creation.CreateTournamentScreen
import com.jukebox.swiss_tournament.tournament_creation.CreateTournamentViewModel
import com.jukebox.swiss_tournament.tournament_playthrough.PlayTournamentScreen
import com.jukebox.swiss_tournament.tournament_playthrough.PlayTournamentViewModel
import com.jukebox.swiss_tournament.ui.theme.SwissTournamentTheme
import java.io.File

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
                        factoryProducer = { PlayTournamentViewModel.Factory(
                            filesDir = File("${getExternalStorageDirectory().path}/Documents")
                        )}
                    )

                    Scaffold {
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
                                        viewModel = playTournamentViewModel,
                                        navController = navController,
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
            Text(
                text = "Swiss Tournament App",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 32.dp)
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { createTournament() },
            ) {
                Text(text = "Neues Tunier erstellen")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    object Screens {
        const val StartScreen = "startScreen"
        const val CreateTournamentScreen = "createTournamentScreen"
        const val PlayTournamentScreen = "playTournamentScreen"
    }
}