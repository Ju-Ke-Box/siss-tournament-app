package com.jukebox.swiss_tournament.data.pairing_engine

import org.junit.Assert
import org.junit.Before
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
        println("file '$filename' exists: ${file.exists()}")
        val javafoHandler = JaVaFoHandler(dir)
        val result = javafoHandler.getPairings(filename)

        val expected = HashMap<Int, Int>()
        expected[2] = 4
        expected[3] = 1
        Assert.assertEquals(
            expected,
            result
        )
    }
}