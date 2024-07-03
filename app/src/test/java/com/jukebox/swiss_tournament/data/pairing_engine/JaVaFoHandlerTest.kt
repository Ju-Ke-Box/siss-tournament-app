package com.jukebox.swiss_tournament.data.pairing_engine

import com.jukebox.swiss_tournament.data.model.StoreResult
import org.junit.Assert
import org.junit.Test
import java.io.File

class JaVaFoHandlerTest {
    val dir = File("sampledata")

    @Test
    fun getPairings_minimal() {
        val content = """
            012 Test
            XXR 2
            DDD SSSS X TT NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN RRRR FFF IIIIIIIIIII YYYY/MM/DD PPPP RRRR
            001    1                         Eins, Spieler1                                  0.0      
            001    2                         Zwei, Spieler2                                  0.0      
            
        """.trimIndent()
        val filename = "minimal.trfx"
        val file = File( "${dir.absolutePath}/$filename")
        file.writeText(content)
        val javafoHandler = JaVaFoHandler()
        val result = javafoHandler.getPairings(file.absolutePath)

        val expected = HashMap<Pair<Int, Int>, String>()
        expected[Pair(2,1)] = StoreResult.ongoing
        Assert.assertEquals(
            expected,
            result
        )
    }

    @Test
    fun getPairings_minimal_uneven() {
        val content = """
            012 Test
            XXR 2
            DDD SSSS X TT NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN RRRR FFF IIIIIIIIIII YYYY/MM/DD PPPP RRRR
            001    1                         Eins, Spieler1                                  0.0      
            001    2                         Zwei, Spieler2                                  0.0      
            001    3                         Drei, Spieler3                                  0.0      
            
        """.trimIndent()
        val filename = "minimal_uneven.trfx"
        val file = File( "${dir.absolutePath}/$filename")
        file.writeText(content)
        val javafoHandler = JaVaFoHandler()
        val result = javafoHandler.getPairings(file.absolutePath)

        val expected = HashMap<Pair<Int, Int>, String>()
        expected[Pair(3,0)] = StoreResult.byeForWhite
        expected[Pair(2,1)] = StoreResult.ongoing
        Assert.assertEquals(
            expected,
            result
        )
    }
}