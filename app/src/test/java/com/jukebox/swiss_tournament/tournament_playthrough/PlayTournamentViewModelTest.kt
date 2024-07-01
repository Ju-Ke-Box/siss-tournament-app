package com.jukebox.swiss_tournament.tournament_playthrough

import com.jukebox.swiss_tournament.data.model.Player
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

class PlayTournamentViewModelTest {
    private var viewModel = PlayTournamentViewModel(File(""))

    @Before
    fun setUp() {
        viewModel = PlayTournamentViewModel(File("sampledata"))
    }

    @Test
    fun getPlayerById_existing() {
        val players = listOf(
            Player(1, "first", "name"),
        )
        viewModel.players = players

        val result = viewModel.getPlayerById(1)

        assertEquals(players[0], result)
    }

    @Test
    fun getPlayerById_0() {
        val players = listOf(
            Player(1, "first", "name"),
        )
        viewModel.players = players

        val result = viewModel.getPlayerById(0)

        assertEquals(Player.byePlayer, result)
    }

    @Test
    fun getPlayerById_nonexisting() {
        val players = listOf(
            Player(1, "first", "name"),
        )
        viewModel.players = players


        assertThrows(IndexOutOfBoundsException().javaClass) {
            viewModel.getPlayerById(2)
        }


    }
}