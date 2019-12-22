package com.github.cta_elevator_alerts.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.github.cta_elevator_alerts.R
import com.github.cta_elevator_alerts.activities.DisplayAlertActivity
import com.github.cta_elevator_alerts.activities.SpecificLineActivity

/**
 * Adapter for alerts within SpecificLineActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class SpecificLineAlertsAdapter(private val context: Context, private val alertStations: List<String>) : RecyclerView.Adapter<SpecificLineAlertsAdapter.SpecificLineAlertsViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class SpecificLineAlertsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val stationAlertTextView: TextView = itemView.findViewById(R.id.txt_specific_line_alert_station)
        internal val starIcon: ImageView = itemView.findViewById(R.id.img_star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificLineAlertsViewHolder {
        val itemView = mInflater.inflate(R.layout.specific_line_alert_station, parent, false)
        return SpecificLineAlertsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SpecificLineAlertsViewHolder, position: Int) {
        val currentStationID = alertStations[position]
        val currentName = (context as SpecificLineActivity).getStationName(currentStationID)
        val isFavorite = context.getIsFavorite(currentStationID)

        holder.stationAlertTextView.text = currentName

        (holder.stationAlertTextView.parent as View).setOnClickListener { v ->
            val intent = Intent(context, DisplayAlertActivity::class.java)
            intent.putExtra("stationID", currentStationID)
            context.startActivity(intent)
        }

        if (isFavorite) {
            holder.starIcon.visibility = View.VISIBLE
        } else {
            holder.starIcon.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return alertStations.size
    }
}
