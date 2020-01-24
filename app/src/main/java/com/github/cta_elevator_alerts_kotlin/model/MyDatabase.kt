package com.github.cta_elevator_alerts_kotlin.model

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database (station_table) for stations
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

@Database(entities = [Station::class, Line::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    val dao: Dao
        get() = stationDao()

    abstract fun stationDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        internal fun getDatabase(context: Context): MyDatabase? {
            if (INSTANCE == null) {
                synchronized(RoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                MyDatabase::class.java, "station_database")
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}