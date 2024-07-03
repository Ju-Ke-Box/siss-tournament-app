package com.jukebox.swiss_tournament.tournament_playthrough

import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.StoreResult
import org.junit.Assert.assertEquals
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

        val result = viewModel.getPlayerById(2)

        assertEquals(Player.byePlayer, result)
    }

    @Test
    fun calculatePoints_test() {
        viewModel.players = listOf(
            Player(1, "A", "a"),
            Player(2, "B", "b"),
            Player(3, "C", "c"),
            Player(4, "D", "d"),
            Player(5, "E", "e"),
            Player(6, "F", "f"),
        )
        viewModel.currentPairings.put(Pair(1, 2), StoreResult.whiteWon)
        viewModel.currentPairings.put(Pair(3, 4), StoreResult.blackWon)
        viewModel.currentPairings.put(Pair(5, 6), StoreResult.remis)

        viewModel.calculatePoints()

        val expected = mutableListOf(1.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f)
        val result = viewModel.players.map { p -> p.points }
        assertEquals(expected, result)
    }

    @Test
    fun calculateRanks_all_different() {
        viewModel.players = listOf(
            Player(1, "A", "a", points = 1.0f, rank = 1),
            Player(2, "B", "b", points = 0.0f, rank = 1),
            Player(3, "C", "c", points = 2.0f, rank = 1),
            Player(4, "D", "d", points = 3.0f, rank = 1),
        )

        viewModel.calculateRanks()
        val result = viewModel.players.map { p -> p.rank }
        val expected = mutableListOf(3, 4, 2, 1)

        assertEquals(expected, result)

    }
    @Test
    fun calculateRanks_two_not_different() {
        viewModel.players = listOf(
            Player(1, "A", "a", points = 1.0f, rank = 1),
            Player(2, "B", "b", points = 0.0f, rank = 1),
            Player(3, "C", "c", points = 1.0f, rank = 1),
            Player(4, "D", "d", points = 2.0f, rank = 1),
        )

        viewModel.calculateRanks()
        val result = viewModel.players.map { p -> p.rank }
        val expected = mutableListOf(2, 4, 2, 1)

        assertEquals(expected, result)
    }
}