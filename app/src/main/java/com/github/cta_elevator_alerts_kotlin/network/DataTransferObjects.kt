package com.github.cta_elevator_alerts_kotlin.network

import com.github.cta_elevator_alerts_kotlin.database.DatabaseStation
import com.squareup.moshi.JsonClass

/**
 * Holds a list of Stations.
 */
@JsonClass(generateAdapter = true)
data class NetworkStationContainer(
        val stations: List<NetworkStation>)

/**
 * Each CTA El station.
 */
@JsonClass(generateAdapter = true)
data class NetworkStation(
        val stop_id: String,
        val ada: Boolean,
        val hasElevatorAlert: Boolean,
        val isFavorite: Boolean,
        val red: Boolean,
        val blue: Boolean,
        val brn: Boolean,
        val g: Boolean,
        val o: Boolean,
        val pnk: Boolean,
        val p: Boolean,
        val pexp: Boolean,
        val y: Boolean,
        val station_name: String)

fun NetworkStationContainer.asDatabaseModel(): Array<DatabaseStation> {
    return stations.map {

        //fix incorrect data for Quincy/Wells
        var ada = it.ada
        if (it.stop_id == "40040") {
            ada = true
        }

        DatabaseStation (
                stationID = it.stop_id,
                hasElevator = ada,
                hasElevatorAlert = false,
                isFavorite = false,
                red = it.red,
                blue = it.blue,
                brown = it.brn,
                green = it.g,
                orange = it.o,
                pink = it.pnk,
                purple = it.p || it.pexp,
                yellow = it.y,
                name = shortenStationName(it.station_name),
                alertDescription = "")
    }
            .toTypedArray()
}

fun shortenStationName(name: String): String{
    return when (name){
        "40850" -> "Harold Wash. Library"
        "40670" -> "Western (O'Hare)"
        "40220" -> "Western (Forest Pk)"
        "40750" -> "Harlem (O'Hare)"
        "40980" -> "Harlem (Forest Pk)"
        "40810" -> "IL Med. District"
        "41690" -> "Cermak-McCorm. Pl."
        else -> ""
    }
}