package com.github.cta_elevator_alerts.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import com.github.cta_elevator_alerts.model.StationRepository

/**
 * ViewModel between AllLinesActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class AllLinesViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: StationRepository? = StationRepository.getInstance(application)

    fun getAllLineAlerts(line: String): List<String>? {
        return mRepository!!.getAllLineAlerts(line)
    }
}
