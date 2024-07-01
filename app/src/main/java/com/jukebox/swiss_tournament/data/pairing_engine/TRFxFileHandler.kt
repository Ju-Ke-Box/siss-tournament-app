package com.jukebox.swiss_tournament.data.pairing_engine

import com.jukebox.swiss_tournament.data.model.Player
import com.jukebox.swiss_tournament.data.model.Tournament
import org.apache.commons.lang3.StringUtils
import java.io.File

class TRFxFileHandler (
    private val filesDir: File
){
    fun createInitialTRFxFile(
        tournamentInfo: Tournament,
        players: List<Player>,
    ): String {
        val dirname = "$filesDir/tournament_${tournamentInfo.name.replace(" ","")}"
        val filename = "$dirname/initial.trfx.txt"
        val dir = File(dirname)
        dir.mkdirs()
        val file = File(filename)
        val content = createInitialFileContent(tournamentInfo, players)
        file.writeText(content)
        return filename
    }

    private fun createInitialFileContent(
        tournamentInfo: Tournament,
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
}