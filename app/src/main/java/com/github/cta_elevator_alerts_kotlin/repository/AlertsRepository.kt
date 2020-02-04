package com.github.cta_elevator_alerts_kotlin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.github.cta_elevator_alerts_kotlin.database.*
import com.github.cta_elevator_alerts_kotlin.domain.Line
import com.github.cta_elevator_alerts_kotlin.domain.Station
import com.github.cta_elevator_alerts_kotlin.network.StationNetwork
import com.github.cta_elevator_alerts_kotlin.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Repository to interact with Room database and
 * pull data from external API (stations and alerts)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class AlertsRepository(private val database: AlertsDatabase) {

    val lines: LiveData<List<Line>> = Transformations.map(database.alertsDao.getAllLines()){
        it.asLineDomainModel()
    }

    val alertStations: LiveData<List<Station>> = Transformations.map(database.alertsDao.getAllAlertStations()){
        it.asStationDomainModel()
    }

    val favoriteStations: LiveData<List<Station>> = Transformations.map(database.alertsDao.getFavoriteStations()){
        it.asStationDomainModel()
    }

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

    val lastUpdatedTime = database.alertsDao.getLastUpdatedTime()

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

    suspend fun switchFavorite(stationID: String){
        withContext(Dispatchers.IO){
            val isFavorite = database.alertsDao.isFavoriteStation(stationID)
            val hasElevator = database.alertsDao.getHasElevator(stationID)
            if (isFavorite && hasElevator) database.alertsDao.updateFavorite(stationID, false)
            else if (hasElevator) database.alertsDao.updateFavorite(stationID, true)
        }
    }

    suspend fun refreshAllAlerts(){
        withContext(Dispatchers.IO){
            buildAlerts()
        }
    }

    private val executor: ExecutorService
    private val connectionStatusLD: MutableLiveData<Boolean>
    private val updateAlertsTimeLD: MutableLiveData<String>
    private val stationCountLD: MutableLiveData<Int>

    private var hasElevator = false

    private var hasElevatorAlert = false

    private var isFavorite = false

    init {
        updateAlertsTimeLD = MutableLiveData()
        connectionStatusLD = MutableLiveData()
        stationCountLD = MutableLiveData()
        executor = Executors.newFixedThreadPool(4)
    }

//    fun mGetHasElevator(stationID: String): Boolean {
//        val thread = object : Thread() {
//            override fun run() {
//                hasElevator = database.alertsDao.getHasElevator(stationID)
//            }
//        }
//        thread.start()
//        try {
//            thread.join()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//
//        return hasElevator
//    }
//
//    fun mGetHasElevatorAlert(stationID: String): Boolean {
//        val thread = object : Thread() {
//            override fun run() {
//                hasElevatorAlert = database.alertsDao.getHasElevatorAlert(stationID)
//            }
//        }
//        thread.start()
//        try {
//            thread.join()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//
//        return hasElevatorAlert
//    }

    //    private String convertDateTime(String s){
    //        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH:mm:ss", Locale.US);
    //        try {
    //            Date originalDate = dateFormat.parse(s);
    //            SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM' 'dd', 'yyyy' at 'h:mm a", Locale.US);
    //            return dateFormat2.format(originalDate);
    //        } catch (ParseException e) {
    //            return s;
    //        }
    //    }
//
//    fun getAlertDetails(stationID: String): List<String> {
//        val list2 = ArrayList<String>()
//
//        val thread = object : Thread() {
//            override fun run() {
//                list2.add(0, database.alertsDao.getName(stationID))
//                val shortDesc = database.alertsDao.getShortDescription(stationID)
//
//                if (shortDesc != null)
//                    list2.add(1, database.alertsDao.getShortDescription(stationID))
//                else
//                    list2.add(1, "")
//            }
//        }
//        thread.start()
//        try {
//            thread.join()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//
//        return list2
//    }
//
////    fun addFavorite(stationID: String) {
//        executor.execute { database.alertsDao.addFavorite(stationID) }
//    }
//
//    fun removeFavorite(stationID: String) {
//        val thread = object : Thread() {
//            override fun run() {
//                database.alertsDao.removeFavorite(stationID)
//            }
//        }
//        thread.start()
//        try {
//            thread.join()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//
//    }

    fun isFavorite(stationID: String): Boolean {
        val thread = object : Thread() {
            override fun run() {
                isFavorite = database.alertsDao.isFavoriteStation(stationID)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return isFavorite
    }

    fun buildAlerts() {
        val jsonstring = pullJSONFromWebService("https://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON")

        //Set internet connection status
        connectionStatusLD.postValue(jsonstring != "NO INTERNET")
        if (jsonstring == "NO INTERNET") return

        val currentAlerts = ArrayList<String>() //For multiple alerts
        val beforeStationsOut = ArrayList(database.alertsDao.allAlertStationIDs())

        try {
            val outer = JSONObject(jsonstring)
            val inner = outer.getJSONObject("CTAAlerts")
            val arrAlerts = inner.getJSONArray("Alert")

            for (i in 0 until arrAlerts.length()) {
                val alert = arrAlerts.get(i) as JSONObject
                val impact = alert.getString("Impact")
                if (impact != "Elevator Status") continue

                val service: JSONArray
                try {
                    val impactedService = alert.getJSONObject("ImpactedService")
                    service = impactedService.getJSONArray("Service")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    continue
                }

                for (j in 0 until service.length()) {
                    val serviceInstance = service.get(j) as JSONObject
                    if (serviceInstance.getString("ServiceType") == "T") {
                        val id = serviceInstance.getString("ServiceId")
                        val headline = alert.getString("Headline")

                        if (headline.contains("Back in Service")) break

                        //End up with beforeStationsOut only containing alerts that no longer exist
                        beforeStationsOut.remove(id)

                        //Looking for multiple alerts for the same station.
                        var shortDesc = alert.getString("ShortDescription")
                        if (currentAlerts.contains(id)) {
                            shortDesc += "\n\n"
                            shortDesc += database.alertsDao.getShortDescription(id)
                        } else {
                            currentAlerts.add(id)
                        }

                        database.alertsDao.setAlert(id, shortDesc)
                        break
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        for (id in beforeStationsOut) {
            database.alertsDao.removeAlert(id)
        }

        val dateFormat = SimpleDateFormat("'Last updated:\n'MMMM' 'dd', 'yyyy' at 'h:mm a", Locale.US)
        val date = Date(System.currentTimeMillis())
        updateAlertsTimeLD.postValue(dateFormat.format(date))
    }

    private fun pullJSONFromWebService(url: String): String {
        val sb = StringBuilder()
        try {
            val urlStations = URL(url)
            val scan = Scanner(urlStations.openStream())
            while (scan.hasNext()) sb.append(scan.nextLine())
            scan.close()
        } catch (e: IOException) {
            sb.delete(0, sb.length)
            sb.append("NO INTERNET")
        }

        return sb.toString()
    }

    fun getAllLineAlerts(line: String): LiveData<List<DatabaseStation>> {
//        val thread = object : Thread() {
//            override fun run() {
//                when (line) {
//                    "Red Line" -> lineAlertList = mDao.allRedLineAlertStations
//                    "Blue Line" -> lineAlertList = mDao.allRedLineAlertStations
//                    "Brown Line" -> lineAlertList = mDao.allRedLineAlertStations
//                    "Green Line" -> lineAlertList = mDao.allRedLineAlertStations
//                    "Orange Line" -> lineAlertList = mDao.allRedLineAlertStations
//                    "Pink Line" -> lineAlertList = mDao.allRedLineAlertStations
//                    "Purple Line" -> lineAlertList = mDao.allRedLineAlertStations
//                    "Yellow Line" -> lineAlertList = mDao.allRedLineAlertStations
//                    else -> {
//                        lineAlertList = MutableLiveData<List<Station>>(listOf())
//                    }
//                }
//            }
//        }
//        thread.start()
//        try {
//            thread.join()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }

//        return lineAlertList
        Log.d("repositoryline", line)
        return when (line) {
            "Red Line" -> database.alertsDao.allRedLineAlertStations()
            "Blue Line" -> database.alertsDao.allBlueLineAlertStations()
            "Brown Line" -> database.alertsDao.allBrownLineAlertStations()
            "Green Line" -> database.alertsDao.allGreenLineAlertStations()
            "Orange Line" -> database.alertsDao.allOrangeLineAlertStations()
            "Pink Line" -> database.alertsDao.allPinkLineAlertStations()
            "Purple Line" -> database.alertsDao.allPurpleLineAlertStations()
            "Yellow Line" -> database.alertsDao.allYellowLineAlertStations()
            else -> MutableLiveData<List<DatabaseStation>>(listOf())
        }
    }
}
