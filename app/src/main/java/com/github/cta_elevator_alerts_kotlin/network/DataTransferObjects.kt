package com.github.cta_elevator_alerts_kotlin.network

import com.github.cta_elevator_alerts_kotlin.database.DatabaseStation

/**
 * Holds a list of Stations.
 */
//@JsonClass(generateAdapter = true)
data class NetworkStationContainer(
        val stations: List<NetworkStation>)

/**
 * Each CTA El station.
 */
//@JsonClass(generateAdapter = true)
data class NetworkStation(
        val map_id: String,
        val ada: Boolean,
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

fun List<NetworkStation>.asDatabaseModel(): Array<DatabaseStation> {

    val databaseStations = HashMap<String, DatabaseStation>()

    this.forEach{
        var ada = it.ada
        if (it.map_id == "40040") {
            ada = true
        }

        //In the JSON file, stations are listed twice sometimes
        //with different line colors. We need all the line colors.
        databaseStations[it.map_id] = DatabaseStation (
                stationID = it.map_id,
                hasElevator = ada,
                hasElevatorAlert = false,
                isFavorite = false,
                red = it.red || databaseStations[it.map_id]?.red ?: it.red,
                blue = it.blue || databaseStations[it.map_id]?.blue ?: it.blue,
                brown = it.brn || databaseStations[it.map_id]?.brown ?: it.brn,
                green = it.g || databaseStations[it.map_id]?.green ?: it.g,
                orange = it.o || databaseStations[it.map_id]?.orange ?: it.o,
                pink = it.pnk || databaseStations[it.map_id]?.pink ?: it.pnk,
                purple = it.p || it.pexp || databaseStations[it.map_id]?.purple ?: it.p || it.pexp,
                yellow = it.y || databaseStations[it.map_id]?.yellow ?: it.y,
                name = shortenStationName(it.map_id) ?: it.station_name,
                alertDescription = "")
    }
    return databaseStations.values.toTypedArray()

//    return map {
//        //fix incorrect data for Quincy/Wells
//        var ada = it.ada
//        if (it.map_id == "40040") {
//            ada = true
//        }
//
//        DatabaseStation (
//                stationID = it.map_id,
//                hasElevator = ada,
//                hasElevatorAlert = false,
//                isFavorite = false,
//                red = it.red,
//                blue = it.blue,
//                brown = it.brn,
//                green = it.g,
//                orange = it.o,
//                pink = it.pnk,
//                purple = it.p || it.pexp,
//                yellow = it.y,
//                name = shortenStationName(it.map_id) ?: it.station_name,
//                alertDescription = "")
//    }
//            .toTypedArray()
}

fun shortenStationName(mapID: String): String?{
    return when (mapID){
        "40850" -> "Harold Wash. Library"
        "40670" -> "Western (O'Hare)"
        "40220" -> "Western (Forest Pk)"
        "40750" -> "Harlem (O'Hare)"
        "40980" -> "Harlem (Forest Pk)"
        "40810" -> "IL Med. District"
        "41690" -> "Cermak-McCorm. Pl."
        else -> null
    }
}