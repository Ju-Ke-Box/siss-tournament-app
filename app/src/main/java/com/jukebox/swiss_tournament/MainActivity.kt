package com.jukebox.swiss_tournament

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jukebox.swiss_tournament.tournament_creation.CreateTournamentScreen
import com.jukebox.swiss_tournament.tournament_creation.CreateTournamentViewModel
import com.jukebox.swiss_tournament.ui.theme.SwissTournamentTheme

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
                    val createTournamentViewModel: CreateTournamentViewModel by viewModels(
                        factoryProducer = { object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return CreateTournamentViewModel() as T
                            }
                        }}
                    )

                    Scaffold {
                        CreateTournamentScreen(
                            viewModel = createTournamentViewModel,
                            modifier = Modifier.padding(it)
                        )
                    }
                }
            }
        }
    }
}