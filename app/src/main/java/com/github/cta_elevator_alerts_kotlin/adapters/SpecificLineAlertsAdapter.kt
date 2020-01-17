package com.github.cta_elevator_alerts_kotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.databinding.SpecificLineAlertStationBinding
import com.github.cta_elevator_alerts_kotlin.model.Station

/**
 * Adapter for alerts within SpecificLineActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class SpecificLineAlertsAdapter(private val specificLineAlertListener: SpecificLineAlertListener): ListAdapter<Station, SpecificLineAlertsAdapter.ViewHolder>(SpecificLineAlertStationDiffCallback()) {

    class ViewHolder private constructor(val binding: SpecificLineAlertStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Station, specificLineAlertListener: SpecificLineAlertListener){
            binding.station = item
            binding.specificLineAlertListener = specificLineAlertListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflator = LayoutInflater.from(parent.context)
                val binding = SpecificLineAlertStationBinding.inflate(layoutInflator, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), specificLineAlertListener)
    }
}

class SpecificLineAlertStationDiffCallback : DiffUtil.ItemCallback<Station>(){
    override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem.stationID == newItem.stationID
    }

    override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem == newItem
    }
}

class SpecificLineAlertListener(val clickListener: (stationID: String) -> Unit){
    fun onClick(station: Station) = clickListener(station.stationID)
}


//    override fun onBindViewHolder(holder: SpecificLineAlertsViewHolder, position: Int) {
//        val currentStationID = alertStations[position]
//        val currentName = (context as SpecificLineActivity).getStationName(currentStationID)
//        val isFavorite = context.getIsFavorite(currentStationID)
//
//        holder.stationAlertTextView.text = currentName
//
//        (holder.stationAlertTextView.parent as View).setOnClickListener { v ->
//            val intent = Intent(context, DisplayAlertActivity::class.java)
//            intent.putExtra("stationID", currentStationID)
//            context.startActivity(intent)
//        }
//
//        if (isFavorite) {
//            holder.starIcon.visibility = View.VISIBLE
//        } else {
//            holder.starIcon.visibility = View.INVISIBLE
//        }
