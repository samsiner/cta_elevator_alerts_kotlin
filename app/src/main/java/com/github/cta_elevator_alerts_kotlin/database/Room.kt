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

    @Query("SELECT * FROM station_table")
    fun getAllStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT timeValue FROM last_updated_table WHERE timeKey=1")
    fun getLastUpdatedTime(): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg stations: DatabaseStation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg lines: DatabaseLine)

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

    @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND red = 1")
    fun allRedLineAlertStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND blue = 1")
    fun allBlueLineAlertStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND brown = 1")
    fun allBrownLineAlertStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND green = 1")
    fun allGreenLineAlertStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND orange = 1")
    fun allOrangeLineAlertStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND pink = 1")
    fun allPinkLineAlertStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND purple = 1")
    fun allPurpleLineAlertStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND yellow = 1")
    fun allYellowLineAlertStations(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE isFavorite = 1")
    fun allFavorites(): LiveData<List<DatabaseStation>>

    @Query("SELECT * FROM station_table WHERE isFavorite = 1")
    fun allFavoritesNotLiveData(): List<DatabaseStation>

    @Query("SELECT COUNT(isFavorite) FROM station_table WHERE isFavorite = 1")
    fun favoritesCount(): Int

    @Query("SELECT COUNT(*) FROM station_table")
    fun stationCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(station: DatabaseStation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(line: DatabaseLine)

    @Query("UPDATE station_table SET isFavorite = :isFavorite WHERE stationID = :id")
    fun updateFavorite(id: String, isFavorite: Boolean)

//    @Query("UPDATE station_table SET isFavorite = 0 WHERE stationID = :id")
//    fun removeFavorite(id: String)

    @Query("UPDATE station_table SET name = :name WHERE stationID = :stationID")
    fun updateName(stationID: String, name: String)

    @Query("SELECT * FROM station_table WHERE stationID = :stationID")
    fun getStation(stationID: String): LiveData<DatabaseStation>

    @Query("SELECT name FROM station_table WHERE stationID = :stationID")
    fun getName(stationID: String): String

    @Query("SELECT alertDescription FROM station_table WHERE stationID = :stationID")
    fun getShortDescription(stationID: String): String

    @Query("SELECT hasElevator FROM station_table WHERE stationID = :stationID")
    fun getHasElevator(stationID: String): Boolean

    @Query("SELECT isFavorite FROM station_table WHERE stationID = :stationID")
    fun getIsFavoriteLiveData(stationID: String): LiveData<Boolean>

    @Query("UPDATE station_table SET hasElevator = 1 WHERE stationID = :stationID")
    fun setHasElevator(stationID: String)

    @Query("UPDATE station_table SET hasElevatorAlert = 1, alertDescription = :shortDesc WHERE stationID = :stationID")
    fun setAlert(stationID: String, shortDesc: String)

    @Query("UPDATE station_table SET hasElevatorAlert = 0, alertDescription = '' WHERE stationID = :stationID")
    fun removeAlert(stationID: String)

    @Query("SELECT hasElevatorAlert FROM station_table WHERE stationID = :stationID")
    fun getHasElevatorAlert(stationID: String): Boolean

    @Query("SELECT isFavorite FROM station_table WHERE stationID = :stationID")
    fun isFavoriteStation(stationID: String): Boolean

    @Query("SELECT red FROM station_table WHERE stationID = :stationID")
    fun getRed(stationID: String): Boolean

    @Query("SELECT blue FROM station_table WHERE stationID = :stationID")
    fun getBlue(stationID: String): Boolean

    @Query("SELECT brown FROM station_table WHERE stationID = :stationID")
    fun getBrown(stationID: String): Boolean

    @Query("SELECT green FROM station_table WHERE stationID = :stationID")
    fun getGreen(stationID: String): Boolean

    @Query("SELECT orange FROM station_table WHERE stationID = :stationID")
    fun getOrange(stationID: String): Boolean

    @Query("SELECT pink FROM station_table WHERE stationID = :stationID")
    fun getPink(stationID: String): Boolean

    @Query("SELECT purple FROM station_table WHERE stationID = :stationID")
    fun getPurple(stationID: String): Boolean

    @Query("SELECT yellow FROM station_table WHERE stationID = :stationID")
    fun getYellow(stationID: String): Boolean

    @Query("UPDATE station_table SET red = 1 WHERE stationID = :stationID")
    fun setRedTrue(stationID: String)

    @Query("UPDATE station_table SET blue = 1 WHERE stationID = :stationID")
    fun setBlueTrue(stationID: String)

    @Query("UPDATE station_table SET brown = 1 WHERE stationID = :stationID")
    fun setBrownTrue(stationID: String)

    @Query("UPDATE station_table SET green = 1 WHERE stationID = :stationID")
    fun setGreenTrue(stationID: String)

    @Query("UPDATE station_table SET orange = 1 WHERE stationID = :stationID")
    fun setOrangeTrue(stationID: String)

    @Query("UPDATE station_table SET pink = 1 WHERE stationID = :stationID")
    fun setPinkTrue(stationID: String)

    @Query("UPDATE station_table SET purple = 1 WHERE stationID = :stationID")
    fun setPurpleTrue(stationID: String)

    @Query("UPDATE station_table SET yellow = 1 WHERE stationID = :stationID")
    fun setYellowTrue(stationID: String)
}


