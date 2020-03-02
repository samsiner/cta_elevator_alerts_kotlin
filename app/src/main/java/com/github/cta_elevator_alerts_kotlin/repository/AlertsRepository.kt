package com.github.cta_elevator_alerts_kotlin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.cta_elevator_alerts_kotlin.database.AlertsDatabase
import com.github.cta_elevator_alerts_kotlin.database.DatabaseLine
import com.github.cta_elevator_alerts_kotlin.database.asLineDomainModel
import com.github.cta_elevator_alerts_kotlin.database.asStationDomainModel
import com.github.cta_elevator_alerts_kotlin.domain.Station
import com.github.cta_elevator_alerts_kotlin.network.AlertNetwork
import com.github.cta_elevator_alerts_kotlin.network.StationNetwork
import com.github.cta_elevator_alerts_kotlin.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * Repository to interact with Room database and
 * pull data from external API (stations and alerts)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class AlertsRepository(private val database: AlertsDatabase) {

    val lines = Transformations.map(database.alertsDao.getAllLines()){
        it.asLineDomainModel()
    }

    val alertStations = Transformations.map(database.alertsDao.getAllAlertStations()){
        it.asStationDomainModel()
    }

    val favoriteStations = Transformations.map(database.alertsDao.getFavoriteStations()){
        it.asStationDomainModel()
    }

    val lastUpdatedTime = database.alertsDao.getLastUpdatedTime()

    fun getStationById(stationID: String): LiveData<Station> = Transformations.map(database.alertsDao.getStation(stationID)) {
        it.asStationDomainModel()
    }

    fun getStationsByLine(name: String): LiveData<List<Station>> = Transformations.map(
        when (name){
            "Red Line" -> database.alertsDao.allRedLineStations()
            "Blue Line" -> database.alertsDao.allBlueLineStations()
            "Brown Line" -> database.alertsDao.allBrownLineStations()
            "Green Line" -> database.alertsDao.allGreenLineStations()
            "Orange Line" -> database.alertsDao.allOrangeLineStations()
            "Pink Line" -> database.alertsDao.allPinkLineStations()
            "Purple Line" -> database.alertsDao.allPurpleLineStations()
            "Yellow Line" -> database.alertsDao.allYellowLineStations()
            else -> database.alertsDao.allRedLineStations()
        }
    ){
        it.asStationDomainModel()
    }

    suspend fun buildAllLines(){
        withContext(Dispatchers.IO) {
            database.alertsDao.insertAll(
                DatabaseLine("red", "Red Line"),
                DatabaseLine("blue", "Blue Line"),
                DatabaseLine("brn", "Brown Line"),
                DatabaseLine("g", "Green Line"),
                DatabaseLine("o", "Orange Line"),
                DatabaseLine("pnk", "Pink Line"),
                DatabaseLine("p", "Purple Line"),
                DatabaseLine("y", "Yellow Line")
            )
        }
    }

    suspend fun buildAllStations(){
        withContext(Dispatchers.IO){
            val allStations = StationNetwork.stations.getAllStations()
            database.alertsDao.insertAll(*allStations.asDatabaseModel())
        }
    }

    suspend fun buildAllAlerts(){
        withContext(Dispatchers.IO){
            val allAlerts = AlertNetwork.alerts.getAllAlerts()
            database.alertsDao.removeAllAlerts()

            for (alert in allAlerts.CTAAlerts.alerts.asDatabaseModel()){
                database.alertsDao.addAlert(alert.first, alert.second)
            }

            //TODO: Fix adding updated time to database
            //Set last updated time
            val dateFormat = SimpleDateFormat("'Last updated:\n'MMMM' 'dd', 'yyyy' at 'h:mm a", Locale.US)
            val date = Date(System.currentTimeMillis())
            database.alertsDao.setLastUpdatedTime(dateFormat.format(date))
            Log.d("Time Set", dateFormat.format(date))
        }
    }

    suspend fun switchFavorite(stationID: String){
        withContext(Dispatchers.IO){
            val isFavorite = database.alertsDao.isFavoriteStation(stationID)
            val hasElevator = database.alertsDao.getHasElevator(stationID)
            if (isFavorite && hasElevator) database.alertsDao.updateFavorite(stationID, false)
            else if (hasElevator) database.alertsDao.updateFavorite(stationID, true)
        }
    }

    suspend fun getAllAlertStationIDs(): List<String> {
        return withContext(Dispatchers.IO){
            database.alertsDao.allAlertStationIDs()
        }
    }
}
