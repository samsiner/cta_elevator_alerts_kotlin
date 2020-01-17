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
import kotlin.collections.HashMap

/**
 * Repository to interact with Room database and
 * pull data from external API (stations and alerts)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class Repository private constructor(application: Application) {

    private val mDao: Dao
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

    private lateinit var lineAlertList: LiveData<List<Station>>

    init {
        val db = MyDatabase.getDatabase(application)
        mDao = db!!.stationDao()
        updateAlertsTimeLD = MutableLiveData()
        connectionStatusLD = MutableLiveData()
        stationCountLD = MutableLiveData()
        executor = Executors.newFixedThreadPool(4)
    }

    fun mGetAllAlertStations(): LiveData<List<Station>> {
        return mDao.allAlertStations
    }

    fun mGetAllFavorites(): LiveData<List<Station>> {
        return mDao.allFavorites
    }

    fun getAllLines(): LiveData<List<Line>> {
        return mDao.allLines
    }

    fun isFavoriteLiveData(stationID: String): LiveData<Boolean>{
        return mDao.getIsFavoriteLiveData(stationID)
    }

    fun mGetStationAlertIDs(): List<String> {
        val list2 = ArrayList<String>()

        val thread = object : Thread() {
            override fun run() {
                list2.addAll(mDao.allAlertStationIDs)
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
                b[0] = mDao.getRed(stationID)
                b[1] = mDao.getBlue(stationID)
                b[2] = mDao.getBrown(stationID)
                b[3] = mDao.getGreen(stationID)
                b[4] = mDao.getOrange(stationID)
                b[5] = mDao.getPink(stationID)
                b[6] = mDao.getPurple(stationID)
                b[7] = mDao.getYellow(stationID)
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
                s = mDao.getName(stationID)
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
                mDao.insert(station)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun insert(line: Line) {
        val thread = object : Thread() {
            override fun run() {
                mDao.insert(line)
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
                hasElevator = mDao.getHasElevator(stationID)
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
                hasElevatorAlert = mDao.getHasElevatorAlert(stationID)
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
                list2.add(0, mDao.getName(stationID))
                val shortDesc = mDao.getShortDescription(stationID)

                if (shortDesc != null)
                    list2.add(1, mDao.getShortDescription(stationID))
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
        executor.execute { mDao.addFavorite(stationID) }
    }

    fun removeFavorite(stationID: String) {
        val thread = object : Thread() {
            override fun run() {
                mDao.removeFavorite(stationID)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    //Hardcoded order of stations from CTA
    var lineStationTable: HashMap<String, List<String>> = HashMap()

    private fun setLineStationIDs() {
        lineStationTable["Red Line"] = listOf("40900", "41190", "40100", "41300", "40760", "40880", "41380", "40340", "41200", "40770", "40540", "40080", "41420", "41320", "41220", "40650", "40630", "41450", "40330", "41660", "41090", "40560", "41490", "41400", "41000", "40190", "41230", "41170", "40910", "40990", "40240", "41430", "40450")
        lineStationTable["Blue Line"] = listOf("40890", "40820", "40230", "40750", "41280", "41330", "40550", "41240", "40060", "41020", "40570", "40670", "40590", "40320", "41410", "40490", "40380", "40370", "40790", "40070", "41340", "40430", "40350", "40470", "40810", "40220", "40250", "40920", "40970", "40010", "40180", "40980", "40390")
        lineStationTable["Brown Line"] = listOf("41290", "41180", "40870", "41010", "41480", "40090", "41500", "41460", "41440", "41310", "40360", "41320", "41210", "40530", "41220", "40660", "40800", "40710", "40460", "40730", "40040", "40160", "40850", "40680", "41700", "40260", "40380")
        lineStationTable["Green Line"] = listOf("40020", "41350", "40610", "41260", "40280", "40700", "40480", "40030", "41670", "41070", "41360", "40170", "41510", "41160", "40380", "40260", "41700", "40680", "41400", "41690", "41120", "40300", "41270", "41080", "40130", "40510", "41140", "40720", "40940", "40290")
        lineStationTable["Orange Line"] = listOf("40930", "40960", "41150", "40310", "40120", "41060", "41130", "41400", "40850", "40160", "40040", "40730", "40380", "40260", "41700", "40680")
        lineStationTable["Pink Line"] = listOf("40580", "40420", "40600", "40150", "40780", "41040", "40440", "40740", "40210", "40830", "41030", "40170", "41510", "41160", "40380", "40260", "41700", "40680", "40850", "40160", "40040", "40730")
        lineStationTable["Purple Line"] = listOf("41050", "41250", "40400", "40520", "40050", "40690", "40270", "40840", "40900", "40540", "41320", "41210", "40530", "41220", "40660", "40800", "40710", "40460", "40380", "40260", "41700", "40680", "40850", "40160", "40040", "40730")
        lineStationTable["Yellow Line"] = listOf("40140", "41680", "40900")
    }

    fun getStationsByLine(lineName: String): MutableLiveData<MutableList<Station>> {
        val newList: MutableList<Station> = mutableListOf()

        for (stationID in lineStationTable[lineName].orEmpty()){
            newList.add(getStation(stationID))
        }
        val newLiveDataList: MutableLiveData<MutableList<Station>> = MutableLiveData()
        newLiveDataList.value = newList
        return newLiveDataList
    }

    var newStation: Station = Station("")

    private fun getStation(stationID: String): Station {
        val thread = object : Thread() {
            override fun run() {
                newStation = mDao.getStation(stationID)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return newStation
    }

    fun isFavorite(stationID: String): Boolean {
        val thread = object : Thread() {
            override fun run() {
                isFavorite = mDao.isFavoriteStation(stationID)
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
        if (mDao.stationCount > 0) return

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

                val station = mDao.getStation(mapID)

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
                    mDao.updateName(mapID, stationName)
                }

                //Set routes that come to this station
                if (ada) mDao.setHasElevator(mapID)
                if (red) {
                    mDao.setRedTrue(mapID)
                }
                if (blue) {
                    mDao.setBlueTrue(mapID)
                }
                if (brown) {
                    mDao.setBrownTrue(mapID)
                }
                if (green) {
                    mDao.setGreenTrue(mapID)
                }
                if (orange) {
                    mDao.setOrangeTrue(mapID)
                }
                if (pink) {
                    mDao.setPinkTrue(mapID)
                }
                if (purple) {
                    mDao.setPurpleTrue(mapID)
                }
                if (yellow) {
                    mDao.setYellowTrue(mapID)
                }
            }
            stationCountLD.postValue(mDao.stationCount)
        } catch (e: JSONException) {
            connectionStatusLD.postValue(false)
        }

    }

    fun buildLines(){
        insert(Line("Red Line"))
        insert(Line("Blue Line"))
        insert(Line("Brown Line"))
        insert(Line("Green Line"))
        insert(Line("Orange Line"))
        insert(Line("Pink Line"))
        insert(Line("Purple Line"))
        insert(Line("Yellow Line"))

        setLineStationIDs()
    }

    fun buildAlerts() {
        val jsonstring = pullJSONFromWebService("https://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON")

        //Set internet connection status
        connectionStatusLD.postValue(jsonstring != "NO INTERNET")
        if (jsonstring == "NO INTERNET") return

        val currentAlerts = ArrayList<String>() //For multiple alerts
        val beforeStationsOut = ArrayList(mDao.allAlertStationIDs)

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
                            shortDesc += mDao.getShortDescription(id)
                        } else {
                            currentAlerts.add(id)
                        }

                        mDao.setAlert(id, shortDesc)
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
            mDao.removeAlert(id)
        }

        val dateFormat = SimpleDateFormat("'Last updated:\n'MMMM' 'dd', 'yyyy' at 'h:mm a", Locale.US)
        val date = Date(System.currentTimeMillis())
        updateAlertsTimeLD.postValue(dateFormat.format(date))
    }

    fun removeAlertKing() {
        val thread = object : Thread() {
            override fun run() {
                mDao.removeAlert("41140")
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
                mDao.setAlert("40900", "Elevator is DOWN - TEST!")
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

    fun getAllLineAlerts(line: String): LiveData<List<Station>> {
        val thread = object : Thread() {
            override fun run() {
                when (line) {
                    "Red Line" -> lineAlertList = mDao.allRedLineAlertStations
                    "Blue Line" -> lineAlertList = mDao.allRedLineAlertStations
                    "Brown Line" -> lineAlertList = mDao.allRedLineAlertStations
                    "Green Line" -> lineAlertList = mDao.allRedLineAlertStations
                    "Orange Line" -> lineAlertList = mDao.allRedLineAlertStations
                    "Pink Line" -> lineAlertList = mDao.allRedLineAlertStations
                    "Purple Line" -> lineAlertList = mDao.allRedLineAlertStations
                    "Yellow Line" -> lineAlertList = mDao.allRedLineAlertStations
                    else -> {
                        lineAlertList = MutableLiveData<List<Station>>(listOf())
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
        var INSTANCE: Repository? = null

        fun getInstance(application: Application): Repository? {
            if (INSTANCE == null) {
                synchronized(Repository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Repository(application)
                    }
                }
            }
            return INSTANCE
        }
    }
}