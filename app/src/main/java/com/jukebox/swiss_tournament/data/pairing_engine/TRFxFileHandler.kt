package com.jukebox.swiss_tournament.data.pairing_engine

import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.PossibleStoreResult
import com.jukebox.swiss_tournament.data.model.Tournament
import org.apache.commons.lang3.StringUtils
import java.io.File

class TRFxFileHandler (
    filesDir: File,
    private val tournamentInfo: Tournament
){
    private val dirname = "$filesDir/tournament_${tournamentInfo.name.replace(" ","")}"
    init {
        val dir = File(dirname)
        dir.mkdirs()
    }
    fun createInitialTRFxFile(
        players: List<Player>,
    ): String {
        val filename = "$dirname/round0.trfx.txt"
        val file = File(filename)
        val content = createInitialFileContent(players)
        file.writeText(content)
        return filename
    }
    fun createNextRoundFile(round: Int): String {
        val newFilename = "$dirname/round$round.trfx.txt"
        val oldFilename = "$dirname/round${round-1}.trfx.txt"
        File(oldFilename).copyTo(File(newFilename))
        return newFilename
    }

    private fun createInitialFileContent(
        players: List<Player>,
    ): String {
        val buffer = StringBuffer()
        buffer.append("012 ${tournamentInfo.name}\n")
        if (tournamentInfo.city.isNotBlank()) { buffer.append("022 ${tournamentInfo.city}\n") }
        if (tournamentInfo.federation.isNotBlank()) { buffer.append("032 ${tournamentInfo.federation}\n") }
        if (tournamentInfo.startDate.isNotBlank()) { buffer.append("042 ${tournamentInfo.startDate}\n") }
        if (tournamentInfo.endDate.isNotBlank()) { buffer.append("052 ${tournamentInfo.endDate}\n") }
        buffer.append("062 ${players.size}\n")
        if (tournamentInfo.arbiters[0].isNotBlank()) { buffer.append("102 ${tournamentInfo.arbiters[0]}\n") }
        buffer.append("XXR ${tournamentInfo.numOfRounds}\n")
        buffer.append("DDD SSSS X TT NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN RRRR FFF IIIIIIIIIII BBBB/BB/BB PPPP RRRR \n")
        players.forEach { player ->
            val dataIdForPlayerData = "001"
            val startingRank = "${player.id}".padStart(4)
            val sex = " " //1
            val title = player.title.padStart(2)//2
            val name = StringUtils.abbreviate("${player.lastName}, ${player.firstName}", 33).padStart(33)
            val fideRating = "    "//4
            val fideFederation = StringUtils.left(tournamentInfo.federation, 3).padStart(3)
            val fideNumber = "           "//11
            val birthdate = "          "//10
            val points = " 0.0"//4
            val rank = "   1"//4
            buffer.append("$dataIdForPlayerData $startingRank $sex $title $name $fideRating $fideFederation $fideNumber $birthdate $points $rank \n")
        }
        return buffer.toString()
    }

    fun addPointsToFile(
        round: Int,
        players: List<Player>,
    ) {
        val filename = "$dirname/round$round.trfx.txt"
        val file = File(filename)

        val idIndexStart = 4
        val idIndexEnd = 8
        val pointsIndexStart = 81
        val pointsIndexEnd = 84

        val content = StringBuffer()
        for (line in file.readLines()) {
            var newLine: String = line
            if (line.startsWith("001")) {
                val playerId = line.substring(idIndexStart, idIndexEnd).trim().toInt()
                val player: Player? = players.find { p -> p.id == playerId }
                if (player != null) {
                    val points = "${player.points}".padStart(3)
                    newLine = line.replaceRange(pointsIndexStart, pointsIndexEnd, points)
                }
            }
            content.append(newLine).append("\n")
        }
        file.writeText(content.toString())
    }

    fun addRanksToFile(
        round: Int,
        players: List<Player>
    ) {
        val filename = "$dirname/round$round.trfx.txt"
        val file = File(filename)

        val idIndexStart = 4
        val idIndexEnd = 8
        val rankIndexStart = 85
        val rankIndexEnd = 89

        val content = StringBuffer()
        for (line in file.readLines()) {
            var newLine: String = line
            if (line.startsWith("001")) {
                val playerId = line.substring(idIndexStart, idIndexEnd).trim().toInt()
                players.find { p -> p.id == playerId }.let {
                    val rank = "${it?.rank}".padStart(4)
                    newLine = line.replaceRange(rankIndexStart, rankIndexEnd, rank)
                }

            }
            content.append(newLine).append("\n")
        }
        file.writeText(content.toString())
    }

    fun appendRoundResultsToFile(
        round: Int,
        pairings: Map<Pair<Int, Int>, String>
    ) {
        val unrolledPairings = unrollPairings(pairings)

        val filename = "$dirname/round$round.trfx.txt"
        val file = File(filename)

        val idIndexStart = 4
        val idIndexEnd = 8

        val content = StringBuffer()
        for (line in file.readLines()) {
            var newLine: String = line
            if (line.startsWith("001")) {
                val playerId = line.substring(idIndexStart, idIndexEnd).trim().toInt()
                val (opponentId, color, result) = unrolledPairings[playerId]!!
                newLine = "$line $opponentId $color $result "
            }
            content.append(newLine).append("\n")
        }
        file.writeText(content.toString())

    }

    private fun unrollPairings(
        pairings: Map<Pair<Int, Int>, String>
    ): Map<Int, Triple<String, Char, String>> {
        val resultMap = HashMap<Int, Triple<String, Char, String>>()
        for (pair in pairings) {
            val white = pair.key.first
            val black = pair.key.second
            val result: String = pair.value

            //white
            val whiteOpponentId = if (black ==0) {"0000"} else {"$black".padStart(4)}
            val whiteResult = if (black==0) PossibleStoreResult.bye else result
            val whiteColor = if (black==0) '-' else 'w'
            resultMap[white] = Triple(whiteOpponentId, whiteColor, whiteResult)
            //black
            val blackOpponentId = if (white ==0) {"0000"} else {"$white".padStart(4)}
            val blackResult =
                if (white==0) {
                    PossibleStoreResult.bye
                } else {
                    when (result) {
                        PossibleStoreResult.whiteWon -> { PossibleStoreResult.blackWon }
                        PossibleStoreResult.blackWon -> { PossibleStoreResult.whiteWon }
                        else -> {result}
                    }
                }

            val blackColor = if (white==0) '-' else 'b'
            resultMap[black] = Triple(blackOpponentId, blackColor, blackResult)
        }
        return resultMap
    }

}