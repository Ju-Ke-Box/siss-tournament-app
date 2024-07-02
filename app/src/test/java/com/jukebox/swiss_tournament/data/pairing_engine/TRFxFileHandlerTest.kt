package com.jukebox.swiss_tournament.data.pairing_engine

import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.Tournament
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class TRFxFileHandlerTest {
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

        val handler = TRFxFileHandler(File("sampledata"), tournament)
        val resultFilename = handler.createInitialTRFxFile(players)
        val resultContent = String(File(resultFilename).readBytes())

        val expected = """
            012 minimal
            062 2
            XXR 1
            001    1                        Eins, Spieler1 RRRR     NNNNNNNNNNN YYYY/MM/DD  0.0   1  
            001    2                        Zwei, Spieler2 RRRR     NNNNNNNNNNN YYYY/MM/DD  0.0   1  

        """.trimIndent()
        assertEquals(expected, resultContent)

    }

    @Test
    fun addPointsToFile_positive() {
        //Assemble
        val tournament = Tournament(
            name = "addTest",
            numOfRounds = 1
        )
        val players = listOf(
            Player(1, "Spieler1", "Eins", points = 1.0f),
            Player(2, "Spieler2", "Zwei", points = 0.0f),
        )

        File("sampledata/tournament_addTest").mkdirs()
        val file = File("sampledata/tournament_addTest/round100.trfx.txt")
        file.writeText("""
            012 addTest
            062 2
            XXR 1
            001    1                        Eins, Spieler1 RRRR     NNNNNNNNNNN YYYY/MM/DD  0.0       
            001    2                        Zwei, Spieler2 RRRR     NNNNNNNNNNN YYYY/MM/DD  0.0       

        """.trimIndent())

        //Act
        val handler = TRFxFileHandler(File("sampledata"), tournament)
        handler.addPointsToFile(100, players)

        //Assert
        val expected = """
            012 addTest
            062 2
            XXR 1
            001    1                        Eins, Spieler1 RRRR     NNNNNNNNNNN YYYY/MM/DD  1.0       
            001    2                        Zwei, Spieler2 RRRR     NNNNNNNNNNN YYYY/MM/DD  0.0       

        """.trimIndent()
        val result = file.readText()
        assertEquals(expected, result)
    }
    @Test
    fun addRanksToFile_positive() {
        //Assemble
        val tournament = Tournament(
            name = "addTest",
            numOfRounds = 1
        )
        val players = listOf(
            Player(1, "Spieler1", "Eins", rank = 1),
            Player(2, "Spieler2", "Zwei", rank = 2),
        )

        File("sampledata/tournament_addTest").mkdirs()
        val file = File("sampledata/tournament_addTest/round101.trfx.txt")
        file.writeText("""
            012 addTest
            062 2
            XXR 1
            001    1                        Eins, Spieler1 RRRR     NNNNNNNNNNN YYYY/MM/DD  1.0       
            001    2                        Zwei, Spieler2 RRRR     NNNNNNNNNNN YYYY/MM/DD  0.0       

        """.trimIndent())

        //Act
        val handler = TRFxFileHandler(File("sampledata"), tournament)
        handler.addRanksToFile(101, players)

        //Assert
        val expected = """
            012 addTest
            062 2
            XXR 1
            001    1                        Eins, Spieler1 RRRR     NNNNNNNNNNN YYYY/MM/DD  1.0    1  
            001    2                        Zwei, Spieler2 RRRR     NNNNNNNNNNN YYYY/MM/DD  0.0    2  

        """.trimIndent()
        val result = file.readText()
        assertEquals(expected, result)
    }
}