package com.github.cta_elevator_alerts.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.github.cta_elevator_alerts.R
import com.github.cta_elevator_alerts.adapters.SpecificLineAdapter
import com.github.cta_elevator_alerts.adapters.SpecificLineAlertsAdapter
import com.github.cta_elevator_alerts.model.Station
import com.github.cta_elevator_alerts.viewmodels.SpecificLineViewModel

/**
 * SpecificLineActivity displays all current elevator
 * outages at the top, then all stations in the line,
 * in order, with their accessibility status
 * and any elevator outages.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class SpecificLineActivity : AppCompatActivity() {
    private var vm: SpecificLineViewModel? = null
    private var lineAlertsAdapter: SpecificLineAlertsAdapter? = null
    private var specificLineAdapter: SpecificLineAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_line)
        vm = ViewModelProviders.of(this).get(SpecificLineViewModel::class.java)
        val line = intent.getStringExtra("line")
        vm!!.line = line

        addFavoritesObserver()

        val lineAlertIDs = vm!!.allLineAlerts
        lineAlertsAdapter = SpecificLineAlertsAdapter(this, lineAlertIDs!!)
        specificLineAdapter = SpecificLineAdapter(this, vm!!.getLine())

        if (lineAlertIDs.isNotEmpty()) {
            val lineAlertsRecyclerView = findViewById<RecyclerView>(R.id.recycler_specific_line_alert_stations)
            lineAlertsRecyclerView.adapter = lineAlertsAdapter
            lineAlertsRecyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            val alerts = findViewById<View>(R.id.tv_elevator_alerts).parent as ViewGroup
            alerts.removeView(findViewById(R.id.tv_all_stations))
            alerts.removeView(findViewById(R.id.recycler_specific_line_alert_stations))
            alerts.removeView(findViewById(R.id.tv_elevator_alerts))
        }

        val specificLineRecyclerView = findViewById<RecyclerView>(R.id.recycler_specific_line)
        specificLineAdapter!!.setToolbar(intent.getStringExtra("line"))
        specificLineRecyclerView.adapter = specificLineAdapter
        specificLineRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun addFavoritesObserver() {
        vm!!.favorites.observe(this, Observer<List<Station>>{
            lineAlertsAdapter!!.notifyDataSetChanged()
            specificLineAdapter!!.notifyDataSetChanged()
        })
    }

    fun toMainActivity(v: View) {
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        finish()
        startActivity(i)
    }

    fun getStationName(stationID: String): String? {
        return vm!!.getStationName(stationID)
    }

    fun getHasElevator(stationID: String): Boolean {
        return vm!!.getHasElevator(stationID)
    }

    fun getIsFavorite(stationID: String): Boolean {
        return vm!!.getIsFavorite(stationID)
    }

    fun getHasElevatorAlert(stationID: String): Boolean {
        return vm!!.getHasElevatorAlert(stationID)
    }

    fun onBackPressed(v: View) {
        finish()
    }
}