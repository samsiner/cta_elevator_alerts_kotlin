package com.github.cta_elevator_alerts_kotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.cta_elevator_alerts_kotlin.database.getDatabase
import com.github.cta_elevator_alerts_kotlin.repository.AlertsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel between DisplayAlertActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class DisplayAlertViewModel(application: Application, val stationID: String) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val alertsRepository = AlertsRepository(database)

    val station = alertsRepository.getStationById(stationID)

    fun switchFavorite() = viewModelScope.launch(Dispatchers.IO) {
        alertsRepository.switchFavorite(stationID)
    }
}

class DisplayAlertViewModelFactory(
        private val application: Application, private val stationID: String) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisplayAlertViewModel::class.java)) {
            return DisplayAlertViewModel(application, stationID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}