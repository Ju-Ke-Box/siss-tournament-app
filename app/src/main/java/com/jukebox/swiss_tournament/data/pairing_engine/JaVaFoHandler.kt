package com.jukebox.swiss_tournament.data.pairing_engine

import com.jukebox.swiss_tournament.data.model.StoreResult
import javafo.api.JaVaFoApi
import java.io.File
import java.io.IOException
import java.nio.file.Files

class JaVaFoHandler {
    fun getPairings(filename: String): Map<Pair<Int, Int>, String> {
        val input = File(filename)
        if (!input.exists()) {
            throw IllegalArgumentException("file $filename does not exist or is inaccessible")
        }
        var pairings = HashMap<Pair<Int, Int>, String>()
        try {
            val inputStream = Files.newInputStream(input.toPath())
            val outputRows = JaVaFoApi.exec(PAIRING, inputStream).split("\n")
            val numberOfPairings = outputRows[0].toInt()
            val pairingRows = outputRows.subList(1, numberOfPairings+1)
            for (row in pairingRows) {
                val (white:Int , black:Int ) = row.split(" ").map {it.toInt()}
                //startingId 0 means "bye (auto win)"
                pairings[Pair(white, black)] =
                if (white == 0) {
                    StoreResult.byeForBlack
                } else if (black == 0) {
                    StoreResult.byeForWhite
                } else {
                    StoreResult.ongoing
                }
            }
        } catch (e: IOException) {
            println(e.message)
            pairings = HashMap()
        }
        return pairings
    }

    @Suppress("unused")
    companion object {
        const val PAIRING = 1000
        const val PAIRING_WITH_BAKU = 1001
        const val PRE_PAIRING_CHECKLIST = 1100
        const val POST_PAIRING_CHECKLIST = 1110
        const val POST_PAIRING_WITH_BAKU_CHECKLIST = 1111
        const val CHECK_TOURNAMENT = 1200
        const val CHECK_ONE_ROUND = 1210
        const val RANDOM_GENERATOR = 1300
        const val RANDOM_GENERATOR_WITH_BAKU = 1301
    }
}