package com.github.cta_elevator_alerts_kotlin.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class StationRepository private constructor(application: Application) {

    private val mStationDao: StationDao
    private val executor: ExecutorService
    private val connectionStatusLD: MutableLiveData<Boolean>
    private val updateAlertsTimeLD: MutableLiveData<String>
    private val stationCountLD: MutableLiveData<Int>

    private var s: String? = null

    private var hasElevator = false

    private var hasElevatorAlert = false

    private var isFavorite = false

    val connectionStatus: LiveData<Boolean>
        get() = connectionStatusLD
    val updatedAlertsTime: LiveData<String>
        get() = updateAlertsTimeLD

    private var lineAlertList: List<String>? = null

    init {
        val db = StationRoomDatabase.getDatabase(application)
        mStationDao = db!!.stationDao()
        updateAlertsTimeLD = MutableLiveData()
        connectionStatusLD = MutableLiveData()
        stationCountLD = MutableLiveData()
        executor = Executors.newFixedThreadPool(4)
    }

    fun mGetAllAlertStations(): LiveData<List<Station>> {
        return mStationDao.allAlertStations
    }

    fun mGetAllFavorites(): LiveData<List<Station>> {
        return mStationDao.allFavorites
    }

    fun isFavoriteLiveData(stationID: String): LiveData<Boolean>{
        return mStationDao.getIsFavoriteLiveData(stationID)
    }

    fun mGetStationAlertIDs(): List<String> {
        val list2 = ArrayList<String>()

        val thread = object : Thread() {
            override fun run() {
                list2.addAll(mStationDao.allAlertStationIDs)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return list2
    }

    fun setConnectionStatus(b: Boolean) {
        connectionStatusLD.postValue(b)
    }

    fun mGetAllRoutes(stationID: String): BooleanArray {
        val b = BooleanArray(8)
        val thread = object : Thread() {
            override fun run() {
                b[0] = mStationDao.getRed(stationID)
                b[1] = mStationDao.getBlue(stationID)
                b[2] = mStationDao.getBrown(stationID)
                b[3] = mStationDao.getGreen(stationID)
                b[4] = mStationDao.getOrange(stationID)
                b[5] = mStationDao.getPink(stationID)
                b[6] = mStationDao.getPurple(stationID)
                b[7] = mStationDao.getYellow(stationID)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return b
    }

    fun mGetStationName(stationID: String): String? {
        val thread = object : Thread() {
            override fun run() {
                s = mStationDao.getName(stationID)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return s
    }

    private fun insert(station: Station) {
        val thread = object : Thread() {
            override fun run() {
                mStationDao.insert(station)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun mGetHasElevator(stationID: String): Boolean {
        val thread = object : Thread() {
            override fun run() {
                hasElevator = mStationDao.getHasElevator(stationID)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return hasElevator
    }

    fun mGetHasElevatorAlert(stationID: String): Boolean {
        val thread = object : Thread() {
            override fun run() {
                hasElevatorAlert = mStationDao.getHasElevatorAlert(stationID)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return hasElevatorAlert
    }

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

    fun getAlertDetails(stationID: String): List<String> {
        val list2 = ArrayList<String>()

        val thread = object : Thread() {
            override fun run() {
                list2.add(0, mStationDao.getName(stationID))
                val shortDesc = mStationDao.getShortDescription(stationID)

                if (shortDesc != null)
                    list2.add(1, mStationDao.getShortDescription(stationID))
                else
                    list2.add(1, "")
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return list2
    }

    fun addFavorite(stationID: String) {
        executor.execute { mStationDao.addFavorite(stationID) }
    }

    fun removeFavorite(stationID: String) {
        val thread = object : Thread() {
            override fun run() {
                mStationDao.removeFavorite(stationID)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun isFavorite(stationID: String): Boolean {
        val thread = object : Thread() {
            override fun run() {
                isFavorite = mStationDao.isFavoriteStation(stationID)
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

    fun buildStations() {
        if (mStationDao.stationCount > 0) return

        val jsonstring = pullJSONFromWebService("https://data.cityofchicago.org/resource/8pix-ypme.json")

        try {
            val arr = JSONArray(jsonstring)

            for (i in 0 until arr.length()) {
                val obj = arr.get(i) as JSONObject
                val mapID = obj.getString("map_id")
                var ada = java.lang.Boolean.parseBoolean(obj.getString("ada"))

                //fix incorrect data for Quincy/Wells
                if (mapID == "40040") {
                    ada = true
                }

                val red = java.lang.Boolean.parseBoolean(obj.getString("red"))
                val blue = java.lang.Boolean.parseBoolean(obj.getString("blue"))
                val brown = java.lang.Boolean.parseBoolean(obj.getString("brn"))
                val green = java.lang.Boolean.parseBoolean(obj.getString("g"))
                val orange = java.lang.Boolean.parseBoolean(obj.getString("o"))
                val pink = java.lang.Boolean.parseBoolean(obj.getString("pnk"))
                val purple = java.lang.Boolean.parseBoolean(obj.getString("p")) || java.lang.Boolean.parseBoolean(obj.getString("pexp"))
                val yellow = java.lang.Boolean.parseBoolean(obj.getString("y"))

                val station = mStationDao.getStation(mapID)

                if (station == null) {
                    val newStation = Station(mapID)
                    var stationName: String
                    try {
                        stationName = obj.getString("station_name")
                    } catch (e: JSONException) {
                        stationName = ""
                    }

                    //name length is too long for this station
                    if (mapID == "40850") {
                        stationName = "Harold Wash. Library"
                    }
                    if (mapID == "40670") {
                        stationName = "Western (O'Hare)"
                    }
                    if (mapID == "40220") {
                        stationName = "Western (Forest Pk)"
                    }
                    if (mapID == "40750") {
                        stationName = "Harlem (O'Hare)"
                    }
                    if (mapID == "40980") {
                        stationName = "Harlem (Forest Pk)"
                    }
                    if (mapID == "40810") {
                        stationName = "IL Med. District"
                    }
                    if (mapID == "41690") {
                        stationName = "Cermak-McCorm. Pl."
                    }

                    insert(newStation)
                    mStationDao.updateName(mapID, stationName)
                }

                //Set routes that come to this station
                if (ada) mStationDao.setHasElevator(mapID)
                if (red) {
                    mStationDao.setRedTrue(mapID)
                }
                if (blue) {
                    mStationDao.setBlueTrue(mapID)
                }
                if (brown) {
                    mStationDao.setBrownTrue(mapID)
                }
                if (green) {
                    mStationDao.setGreenTrue(mapID)
                }
                if (orange) {
                    mStationDao.setOrangeTrue(mapID)
                }
                if (pink) {
                    mStationDao.setPinkTrue(mapID)
                }
                if (purple) {
                    mStationDao.setPurpleTrue(mapID)
                }
                if (yellow) {
                    mStationDao.setYellowTrue(mapID)
                }
            }
            stationCountLD.postValue(mStationDao.stationCount)
        } catch (e: JSONException) {
            connectionStatusLD.postValue(false)
        }

    }

    fun buildAlerts() {
        val jsonstring = pullJSONFromWebService("https://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON")

        //Set internet connection status
        connectionStatusLD.postValue(jsonstring != "NO INTERNET")
        if (jsonstring == "NO INTERNET") return

        val currentAlerts = ArrayList<String>() //For multiple alerts
        val beforeStationsOut = ArrayList(mStationDao.allAlertStationIDs)

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
                            shortDesc += mStationDao.getShortDescription(id)
                        } else {
                            currentAlerts.add(id)
                        }

                        mStationDao.setAlert(id, shortDesc)
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
            mStationDao.removeAlert(id)
        }

        val dateFormat = SimpleDateFormat("'Last updated:\n'MMMM' 'dd', 'yyyy' at 'h:mm a", Locale.US)
        val date = Date(System.currentTimeMillis())
        updateAlertsTimeLD.postValue(dateFormat.format(date))
    }

    fun removeAlertKing() {
        val thread = object : Thread() {
            override fun run() {
                mStationDao.removeAlert("41140")
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun addAlertHoward() {
        val thread = object : Thread() {
            override fun run() {
                mStationDao.setAlert("40900", "Elevator is DOWN - TEST!")
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

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

    fun getAllLineAlerts(line: String): List<String>? {
        val thread = object : Thread() {
            override fun run() {
                when (line) {
                    "Red Line" -> {
                        lineAlertList = mStationDao.allRedLineAlertIDs
                        Log.d("red line alerts: ", lineAlertList!!.toString())
                    }
                    "Blue Line" -> lineAlertList = mStationDao.allBlueLineAlertIDs
                    "Brown Line" -> lineAlertList = mStationDao.allBrownLineAlertIDs
                    "Green Line" -> lineAlertList = mStationDao.allGreenLineAlertIDs
                    "Orange Line" -> lineAlertList = mStationDao.allOrangeLineAlertIDs
                    "Pink Line" -> lineAlertList = mStationDao.allPinkLineAlertIDs
                    "Purple Line" -> lineAlertList = mStationDao.allPurpleLineAlertIDs
                    "Yellow Line" -> lineAlertList = mStationDao.allYellowLineAlertIDs
                    else -> {
                    }
                }
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return lineAlertList
    }

    companion object {
        @Volatile
        var INSTANCE: StationRepository? = null

        fun getInstance(application: Application): StationRepository? {
            if (INSTANCE == null) {
                synchronized(StationRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StationRepository(application)
                    }
                }
            }
            return INSTANCE
        }
    }
}