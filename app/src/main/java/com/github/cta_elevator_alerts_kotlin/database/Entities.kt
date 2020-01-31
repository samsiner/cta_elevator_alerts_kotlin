package com.github.cta_elevator_alerts_kotlin.database

import androidx.lifecycle.Transformations.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.cta_elevator_alerts_kotlin.domain.Line
import com.github.cta_elevator_alerts_kotlin.domain.Station

/**
 * Room Database entity (Station)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

@Entity(tableName = "station_table")
data class DatabaseStation constructor(
        @PrimaryKey
        val stationID: String,
        val hasElevator: Boolean,
        val hasElevatorAlert: Boolean,
        val isFavorite: Boolean,
        val red: Boolean,
        val blue: Boolean,
        val brown: Boolean,
        val green: Boolean,
        val orange: Boolean,
        val pink: Boolean,
        val purple: Boolean,
        val yellow: Boolean,
        val name: String,
        val alertDescription: String)

fun List<DatabaseStation>.asStationDomainModel(): List<Station>{
    return map{
        Station(
                stationID = it.stationID,
                hasElevator = it.hasElevator,
                hasElevatorAlert = it.hasElevatorAlert,
                isFavorite = it.isFavorite,
                red = it.red,
                blue = it.blue,
                brown = it.brown,
                green = it.green,
                orange = it.orange,
                pink = it.pink,
                purple = it.purple,
                yellow = it.yellow,
                name = it.name,
                alertDescription = it.alertDescription)
    }
}


//TODO Add functionality to update hasAlert
@Entity(tableName = "line_table")
data class DatabaseLine(
        @PrimaryKey
        val name: String,
        val hasElevatorAlert: Boolean)

fun List<DatabaseLine>.asLineDomainModel(): List<Line>{
    return map{
        Line(
                name = it.name,
                hasElevatorAlert = it.hasElevatorAlert)
    }
}

@Entity(tableName = "last_updated_table")
data class DatabaseLastUpdatedTime(
        //Use a key of 1 to store and access last updated time
        @PrimaryKey
        val timeKey: Int,
        val timeValue: String)


