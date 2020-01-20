package com.github.cta_elevator_alerts_kotlin.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.cta_elevator_alerts_kotlin.model.Line

import com.github.cta_elevator_alerts_kotlin.model.Station
import com.github.cta_elevator_alerts_kotlin.model.Repository

/**
 * ViewModel between SpecificLineActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class SpecificLineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository? = Repository.getInstance(application)

    var lineName: String = ""
        set(value) {
            field = value
            lineStations = repository?.getStationsByLine(value) ?: MutableLiveData(mutableListOf())
        }
    lateinit var lineStations: MutableLiveData<MutableList<Station>>

    val favorites: LiveData<List<Station>>
        get() = repository!!.mGetAllFavorites()

    val allLineAlerts: LiveData<List<Station>>
        get() = repository!!.getAllLineAlerts(lineName)

    fun buildLines() {
        repository?.buildLines()
    }
}