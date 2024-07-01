package com.jukebox.swiss_tournament.data.pairing_engine

import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.Tournament
import org.junit.Assert
import org.junit.Test
import java.io.File

class TRFxFileHandlerTest {
    val handler = TRFxFileHandler(File("sampledata"))
    @Test
    fun createInititalTRFxFile_minimal() {

        val tournament = Tournament(
            name = "minimal",
            numOfRounds = 1
        )
        val players = listOf(
            Player(1, "Spieler1", "Eins"),
            Player(2, "Spieler2", "Zwei"),
        )
        val resultFilename = handler.createInitialTRFxFile(tournament, players)
        val resultContent = String(File(resultFilename).readBytes())

        val expected = """
            012 minimal
            062 2
            XXR 1
            001    1                        Eins, Spieler1 RRRR     NNNNNNNNNNN YYYY/MM/DD  0.0       
            001    2                        Zwei, Spieler2 RRRR     NNNNNNNNNNN YYYY/MM/DD  0.0       

        """.trimIndent()
        Assert.assertEquals(expected, resultContent)

    }
}