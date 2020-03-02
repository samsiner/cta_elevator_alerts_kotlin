package com.github.cta_elevator_alerts_kotlin.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Room database (station_table) for stations
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

@Database(entities = [DatabaseStation::class, DatabaseLine::class, DatabaseLastUpdatedTime::class], version = 2)
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

@Dao
interface AlertsDao {
    @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1")
    fun getAllAlertStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE isFavorite = 1")
    fun getFavoriteStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM line_table")
    fun getAllLines(): LiveData<List<DatabaseLine>>

    @Query("SELECT timeValue FROM last_updated_table WHERE timeKey = 1")
    fun getLastUpdatedTime(): LiveData<String>

    @Query("UPDATE last_updated_table SET timeValue = :time WHERE timeKey = 1")
    fun setLastUpdatedTime(time: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg stations: DatabaseStation)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg lines: DatabaseLine)

    @Query("UPDATE station_table SET hasElevatorAlert = 0")
    fun removeAllAlerts()

    @Query("UPDATE station_table SET hasElevatorAlert = 1, alertDescription = :desc WHERE stationID = :stationID")
    fun addAlert(stationID: String, desc: String)

    @Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1")
    fun allAlertStationIDs(): List<String>

    @Query("SELECT * FROM station_table WHERE red = 1")
    fun allRedLineStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE blue = 1")
    fun allBlueLineStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE brown = 1")
    fun allBrownLineStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE green = 1")
    fun allGreenLineStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE orange = 1")
    fun allOrangeLineStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE pink = 1")
    fun allPinkLineStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE purple = 1")
    fun allPurpleLineStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE yellow = 1")
    fun allYellowLineStations(): LiveData<List<DatabaseStation>>

    @Query("UPDATE station_table SET isFavorite = :isFavorite WHERE stationID = :id")
    fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("SELECT * FROM station_table WHERE stationID = :stationID")
    fun getStation(stationID: String): LiveData<DatabaseStation>

    @Query("SELECT hasElevator FROM station_table WHERE stationID = :stationID")
    fun getHasElevator(stationID: String): Boolean

    @Query("SELECT isFavorite FROM station_table WHERE stationID = :stationID")
    fun isFavoriteStation(stationID: String): Boolean
}


