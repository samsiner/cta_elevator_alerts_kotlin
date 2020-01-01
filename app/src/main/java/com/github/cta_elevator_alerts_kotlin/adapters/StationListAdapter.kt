package com.github.cta_elevator_alerts_kotlin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.cta_elevator_alerts_kotlin.R

import com.github.cta_elevator_alerts_kotlin.databinding.IndividualStationBinding
import com.github.cta_elevator_alerts_kotlin.model.Station

/**
 * Adapter for all station lists (Favorites & Alerts) within MainActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class StationListAdapter: ListAdapter<Station, StationListAdapter.ViewHolder>(StationDiffCallback()) {

//    private val lineColors: IntArray = context.resources.getIntArray(R.array.lineColors)

    class ViewHolder private constructor(val binding: IndividualStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Station){
            binding.station = item
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
        val item = getItem(position)
        holder.bind(item)
//
//        if (current.isFavorite) {
//            holder.starIcon.visibility = View.VISIBLE
//        }
//
//        //populate line bars to show colors of each route under station name
//        var viewPosition = 0
//        for (i in currentRoutes.indices) {
//            if (currentRoutes[i]) {
//                holder.lineViews[viewPosition].setBackgroundColor(lineColors[i])
//                viewPosition++
//            }
//        }
//
//        //remove bottom border styling from last element
//        if (position == stations.size - 1) {
//            holder.rl.setBackgroundResource(0)
//        } else {
//            holder.rl.setBackgroundResource(R.drawable.border_bottom)
//        }
//
//        //Make station clickable to see details
//        (holder.tv.parent as View).setOnClickListener {
//            val intent = Intent(context, DisplayAlertActivity::class.java)
//            intent.putExtra("stationID", current.stationID)
//            intent.putExtra("fromMain", true)
//            context.startActivity(intent)
//        }
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
