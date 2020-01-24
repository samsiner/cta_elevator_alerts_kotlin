package com.github.cta_elevator_alerts_kotlin.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object for Room Database (station_table)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

@Dao
interface Dao {

    @get:Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1")
    val allAlertStations: LiveData<List<Station>>

    @get:Query("SELECT * FROM line_table")
    val allLines: LiveData<List<Line>>

    @get:Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1")
    val allAlertStationIDs: List<String>

    @get:Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND red = 1")
    val allRedLineAlertStations: LiveData<List<Station>>

    @get:Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND blue = 1")
    val allBlueLineAlertStations: LiveData<List<Station>>

    @get:Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND brown = 1")
    val allBrownLineAlertStations: LiveData<List<Station>>

    @get:Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND green = 1")
    val allGreenLineAlertStations: LiveData<List<Station>>

    @get:Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND orange = 1")
    val allOrangeLineAlertStations: LiveData<List<Station>>

    @get:Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND pink = 1")
    val allPinkLineAlertStations: LiveData<List<Station>>

    @get:Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND purple = 1")
    val allPurpleLineAlertStations: LiveData<List<Station>>

    @get:Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1 AND yellow = 1")
    val allYellowLineAlertStations: LiveData<List<Station>>

    @get:Query("SELECT * FROM station_table WHERE isFavorite = 1")
    val allFavorites: LiveData<List<Station>>

    @get:Query("SELECT * FROM station_table WHERE isFavorite = 1")
    val allFavoritesNotLiveData: List<Station>

    @get:Query("SELECT COUNT(isFavorite) FROM station_table WHERE isFavorite = 1")
    val favoritesCount: Int

    @get:Query("SELECT COUNT(*) FROM station_table")
    val stationCount: Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(station: Station)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(line: Line)

    @Query("UPDATE station_table SET isFavorite = 1, nickname = NULL WHERE stationID = :id")
    fun addFavorite(id: String)

    @Query("UPDATE station_table SET isFavorite = 0 WHERE stationID = :id")
    fun removeFavorite(id: String)

    @Query("UPDATE station_table SET name = :name WHERE stationID = :stationID")
    fun updateName(stationID: String, name: String)

    @Query("SELECT * FROM station_table WHERE stationID = :stationID")
    fun getStation(stationID: String): Station

    @Query("SELECT name FROM station_table WHERE stationID = :stationID")
    fun getName(stationID: String): String

    @Query("SELECT shortDescription FROM station_table WHERE stationID = :stationID")
    fun getShortDescription(stationID: String): String

    @Query("SELECT hasElevator FROM station_table WHERE stationID = :stationID")
    fun getHasElevator(stationID: String): Boolean

    @Query("SELECT isFavorite FROM station_table WHERE stationID = :stationID")
    fun getIsFavoriteLiveData(stationID: String): LiveData<Boolean>

    @Query("UPDATE station_table SET hasElevator = 1 WHERE stationID = :stationID")
    fun setHasElevator(stationID: String)

    @Query("UPDATE station_table SET hasElevatorAlert = 1, shortDescription = :shortDesc WHERE stationID = :stationID")
    fun setAlert(stationID: String, shortDesc: String)

    @Query("UPDATE station_table SET hasElevatorAlert = 0, shortDescription = '' WHERE stationID = :stationID")
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