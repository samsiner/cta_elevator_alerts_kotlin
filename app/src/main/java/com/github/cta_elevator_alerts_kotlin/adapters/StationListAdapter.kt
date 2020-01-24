package com.github.cta_elevator_alerts_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.github.cta_elevator_alerts_kotlin.databinding.IndividualStationBinding
import com.github.cta_elevator_alerts_kotlin.model.Station

/**
 * Adapter for all station lists (Favorites & Alerts) within MainActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class StationListAdapter(private val stationListener: StationListener): ListAdapter<Station, StationListAdapter.ViewHolder>(StationDiffCallback()) {

    class ViewHolder private constructor(val binding: IndividualStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Station, stationListener: StationListener){
            binding.station = item
            binding.stationListener = stationListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflator = LayoutInflater.from(parent.context)
                val binding = IndividualStationBinding.inflate(layoutInflator, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), stationListener)
    }
}

class StationDiffCallback : DiffUtil.ItemCallback<Station>(){
    override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem.stationID == newItem.stationID
    }

    override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem == newItem
    }
}

class StationListener(val clickListener: (stationID: String) -> Unit){
    fun onClick(station: Station) = clickListener(station.stationID)
}
