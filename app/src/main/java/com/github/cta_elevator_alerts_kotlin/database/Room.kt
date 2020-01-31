package com.github.cta_elevator_alerts_kotlin.database

import android.content.Context
import androidx.room.*

/**
 * Room database (station_table) for stations
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

@Database(entities = [Station::class, Line::class], version = 1)
abstract class AlertsDatabase : RoomDatabase() {
    abstract val alertsDao: AlertsDao
}

private lateinit var INSTANCE: AlertsDatabase

fun getDatabase(context: Context): AlertsDatabase{
    synchronized(AlertsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AlertsDatabase::class.java,
                    "alerts").build()
        }
    }
    return INSTANCE
}


