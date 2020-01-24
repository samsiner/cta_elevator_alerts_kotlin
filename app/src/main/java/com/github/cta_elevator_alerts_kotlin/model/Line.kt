package com.github.cta_elevator_alerts_kotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Database entity (Line)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

@Entity(tableName = "line_table")
data class Line(@field:PrimaryKey
                   val name: String) {
    //TODO Add functionality to update hasAlert
    var hasElevatorAlert: Boolean = false
}
