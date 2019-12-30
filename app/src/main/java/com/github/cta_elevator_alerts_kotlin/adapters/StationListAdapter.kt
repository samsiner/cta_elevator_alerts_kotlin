package com.github.cta_elevator_alerts_kotlin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.github.cta_elevator_alerts.R
import com.github.cta_elevator_alerts_kotlin.activities.DisplayAlertActivity
import com.github.cta_elevator_alerts_kotlin.activities.MainActivity
import com.github.cta_elevator_alerts_kotlin.model.Station
import com.github.cta_elevator_alerts.viewmodels.MainViewModel

import java.util.ArrayList

/**
 * Adapter for all station lists (Favorites & Alerts) within MainActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class StationListAdapter(private val context: Context) : RecyclerView.Adapter<StationListAdapter.StationListViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val lineColors: IntArray = context.resources.getIntArray(R.array.lineColors)
    private val vm: MainViewModel? = (context as MainActivity).stationAlertsViewModel
    private var stations: List<Station> = ArrayList()

    inner class StationListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rl: RelativeLayout = itemView.findViewById(R.id.rl_individual_station)
        val iv: ImageView = itemView.findViewById(R.id.img_individual_station)
        val tv: TextView = itemView.findViewById(R.id.txt_individual_station)
        val starIcon: ImageView = itemView.findViewById(R.id.img_star_icon)
        val lineViews: Array<View> = arrayOf(itemView.findViewById<View>(R.id.line_0),
            itemView.findViewById<View>(R.id.line_1),
            itemView.findViewById<View>(R.id.line_2),
            itemView.findViewById<View>(R.id.line_3),
            itemView.findViewById<View>(R.id.line_4),
            itemView.findViewById<View>(R.id.line_5)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationListViewHolder {
        val itemView = mInflater.inflate(R.layout.individual_station, parent, false)
        itemView.setBackgroundColor(0x00000000)
        return StationListViewHolder(itemView)
    }

    fun updateStationList(stations: List<Station>) {
        this.stations = stations
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: StationListViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val current = stations[position]
        val currentRoutes = vm!!.getAllRoutes(current.stationID)
        holder.tv.text = current.name

        if (vm.getHasElevatorAlert(current.stationID)) {
            holder.iv.setImageResource(R.drawable.status_red)
        } else {
            holder.iv.visibility = View.INVISIBLE
        }

        if (current.isFavorite) {
            holder.starIcon.visibility = View.VISIBLE
        }

        //populate line bars to show colors of each route under station name
        var viewPosition = 0
        for (i in currentRoutes.indices) {
            if (currentRoutes[i]) {
                holder.lineViews[viewPosition].setBackgroundColor(lineColors[i])
                viewPosition++
            }
        }

        //remove bottom border styling from last element
        if (position == stations.size - 1) {
            holder.rl.setBackgroundResource(0)
        } else {
            holder.rl.setBackgroundResource(R.drawable.border_bottom)
        }

        //Make station clickable to see details
        (holder.tv.parent as View).setOnClickListener {
            val intent = Intent(context, DisplayAlertActivity::class.java)
            intent.putExtra("stationID", current.stationID)
            intent.putExtra("fromMain", true)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}