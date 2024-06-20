package com.jukebox.swiss_tournament.data.model

import org.apache.commons.lang3.StringUtils
import java.io.File

class TRFxFileHandler {
    fun createInitialTRFxFile(
        tournamentInfo: Tournament,
        players: List<Player>,
        filesDir: File
    ) {
        val dirname = "$filesDir/tournament_${tournamentInfo.name.replace(" ","")}"
        val filename = "$dirname/initial.trfx.txt"
        val dir = File(dirname)
        dir.mkdirs()
        val file = File(filename)
        val tournamentPart = """
            012 ${tournamentInfo.name}
            022 ${tournamentInfo.city}
            032 ${tournamentInfo.federation}
            042 ${tournamentInfo.startDate}
            052 ${tournamentInfo.endDate}
            062 ${players.size}
            102 ${tournamentInfo.arbiters[0]}
            XXR ${tournamentInfo.numOfRounds}
            
            """.trimIndent()
        val buffer = StringBuffer(tournamentPart)
        players.forEachIndexed { index, player ->
            val dataIdForPlayerData = "001"
            val startingRank = "$index".padStart(4)
            val sex = "-"
            val title = player.title
            val name = StringUtils.abbreviate("${player.lastName}, ${player.firstName}", 32) //TODO exact 32 (padding front)
            val fideRating = "RRRR"
            val fideFederation = StringUtils.left(tournamentInfo.federation, 3) //TODO exact 3 (padding front)
            val fideNumber = "NNNNNNNNNNN"
            val birthdate = "YYYY/MM/DD"
            val points = " 0.0"
            val rank = "0001"
            buffer.append("$dataIdForPlayerData $startingRank $sex $title $name $fideRating $fideFederation $fideNumber $birthdate $points $rank  \n")
        }
        file.writeText(buffer.toString())
    }
}