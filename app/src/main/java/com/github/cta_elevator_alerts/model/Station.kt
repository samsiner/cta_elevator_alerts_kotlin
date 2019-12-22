package com.github.cta_elevator_alerts.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Database entity (Station)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

@Entity(tableName = "station_table")
class Station(@field:PrimaryKey
              val stationID: String) {
    var hasElevator: Boolean = false
    var hasElevatorAlert: Boolean = false
    var isFavorite: Boolean = false
    var red: Boolean = false
    var blue: Boolean = false
    var brown: Boolean = false
    var green: Boolean = false
    var orange: Boolean = false
    var pink: Boolean = false
    var purple: Boolean = false
    var yellow: Boolean = false
    var name: String? = null
    var shortDescription: String? = null
    var nickname: String? = null
}
