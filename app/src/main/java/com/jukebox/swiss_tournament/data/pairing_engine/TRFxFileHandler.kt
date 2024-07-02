package com.jukebox.swiss_tournament.data.pairing_engine

import com.jukebox.swiss_tournament.data.model.Player
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
        players.forEach { player ->
            val dataIdForPlayerData = "001"
            val startingRank = "${player.id}".padStart(4)
            val sex = " "
            val title = player.title.padStart(2)
            val name = StringUtils.abbreviate("${player.lastName}, ${player.firstName}", 32).padStart(32)
            val fideRating = "RRRR"
            val fideFederation = StringUtils.left(tournamentInfo.federation, 3).padStart(3)
            val fideNumber = "NNNNNNNNNNN"
            val birthdate = "YYYY/MM/DD"
            val points = " 0.0"
            buffer.append("$dataIdForPlayerData $startingRank $sex $title $name $fideRating $fideFederation $fideNumber $birthdate $points       \n")
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
        val pointsIndexStart = 80
        val pointsIndexEnd = 83

        val content = StringBuffer()
        for (line in file.readLines()) {
            var newLine: String = line
            if (line.startsWith("001")) {
                val playerId = line.substring(idIndexStart, idIndexEnd).trim().toInt()
                val player: Player? = players.find { p -> p.id == playerId }
                if (player != null) {
                    val points = "${player.points}"
                    newLine = line.replaceRange(pointsIndexStart, pointsIndexEnd, points)
                }
            }
            content.append(newLine).append("\n")
        }
        file.writeText(content.toString())
    }
}