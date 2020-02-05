package com.github.cta_elevator_alerts_kotlin.network

import com.github.cta_elevator_alerts_kotlin.database.DatabaseStation
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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

data class NetworkData(
        val CTAAlerts: NetworkAlertContainer)

data class NetworkAlertContainer(
        @Json(name = "Alert") val alerts: List<NetworkAlert>)

data class NetworkAlert(
        @Json(name = "ShortDescription") val shortDescription: String,
        @Json(name = "Headline") val headline: String,
        @Json(name = "Impact") val impact: String,
        @Json(name = "ImpactedService") val impactedService: ImpactedServiceContainer)

//TODO: fix array or object variably
data class ImpactedServiceContainer (
        val servicesObject: List<ImpactedService>,
        val servicesArray: ImpactedService)

data class ImpactedService(
        @Json(name = "ServiceType") val type: String,
        @Json(name = "ServiceId") val stationID: String)


fun List<NetworkAlert>.asDatabaseModel(): Array<Pair<String, String>> {
    val pairs: ArrayList<Pair<String, String>> = ArrayList()

    this.forEach{
        if (it.impact != "Elevator Status" || it.headline.contains("Back in Service")){
            return@forEach
        }

        //"T" = Train station alert
        for (impactedService in it.impactedService.servicesObject){
            if (impactedService.type == "T"){
                val stationID = impactedService.stationID
                val desc = it.shortDescription
                pairs.add(Pair(stationID, desc))
            }
        }

        val stationID = it.impactedService.servicesArray.stationID
        val desc = it.shortDescription
        pairs.add(Pair(stationID, desc))
    }
    return pairs.toTypedArray()
}

