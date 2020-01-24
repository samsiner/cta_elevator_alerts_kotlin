package com.github.cta_elevator_alerts_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.cta_elevator_alerts_kotlin.databinding.SpecificLineStationBinding
import com.github.cta_elevator_alerts_kotlin.model.Station

/**
 * Adapter for all stations with SpecificLineActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class SpecificLineAdapter(private val lineName: String, private val specificLineListener: SpecificLineListener): ListAdapter<Station, SpecificLineAdapter.ViewHolder>(SpecificLineStationDiffCallback()) {

    class ViewHolder private constructor(val binding: SpecificLineStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station, count: Int, lineName: String, specificLineListener: SpecificLineListener){
            binding.station = station
            binding.lineName = lineName
            binding.position = adapterPosition
            binding.totalPositions = count
            binding.specificLineListener = specificLineListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflator = LayoutInflater.from(parent.context)
                val binding = SpecificLineStationBinding.inflate(layoutInflator, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), itemCount, lineName, specificLineListener)
    }
}

class SpecificLineStationDiffCallback : DiffUtil.ItemCallback<Station>(){
    override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem.stationID == newItem.stationID
    }

    override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem == newItem
    }
}

class SpecificLineListener(val clickListener: (stationID: String) -> Unit) {
    fun onClick(station: Station) = clickListener(station.stationID)
}


//    fun setToolbar(lineName: String) {
//        toolbarTextView.text = lineName
//        when (lineName) {
//            "Red Line" -> toolbar.setBackgroundResource(R.color.colorRedLine)
//            "Blue Line" -> toolbar.setBackgroundResource(R.color.colorBlueLine)
//            "Brown Line" -> toolbar.setBackgroundResource(R.color.colorBrownLine)
//            "Green Line" -> toolbar.setBackgroundResource(R.color.colorGreenLine)
//            "Orange Line" -> toolbar.setBackgroundResource(R.color.colorOrangeLine)
//            "Pink Line" -> toolbar.setBackgroundResource(R.color.colorPinkLine)
//            "Purple Line" -> toolbar.setBackgroundResource(R.color.colorPurpleLine)
//            "Yellow Line" -> {
//                val colorID = ContextCompat.getColor(context, R.color.colorPrimaryDark)
//                toolbar.setBackgroundResource(R.color.colorYellowLine)
//                toolbarTextView.setTextColor(colorID)
//                backArrow.setColorFilter(colorID)
//                homeIcon.setColorFilter(colorID)
//            }
//        }
//    }
//}
