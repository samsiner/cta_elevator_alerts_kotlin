package com.github.cta_elevator_alerts_kotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentSpecificLineBinding
import com.github.cta_elevator_alerts_kotlin.databinding.SpecificLineAlertStationBinding
import com.github.cta_elevator_alerts_kotlin.databinding.SpecificLineStationBinding
import com.github.cta_elevator_alerts_kotlin.domain.Station
import com.github.cta_elevator_alerts_kotlin.viewmodel.SpecificLineViewModel
import com.github.cta_elevator_alerts_kotlin.viewmodel.SpecificLineViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class SpecificLineFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate view and obtain an instance of the binding class
        val binding: FragmentSpecificLineBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_specific_line,
                container,
                false
        )
        val arguments = SpecificLineFragmentArgs.fromBundle(arguments!!)
        val lineName = arguments.lineName

        //Create ViewModel and initialize line name at construction
        val application = requireNotNull(this.activity).application
        val viewModelFactory = SpecificLineViewModelFactory(application, lineName)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SpecificLineViewModel::class.java)

        binding.lifecycleOwner = this

        //Create adapter to display all alerts
        val specificLineAlertsAdapter = SpecificLineAlertsAdapter(SpecificLineAlertListener { stationID ->
            findNavController().navigate(
                    SpecificLineFragmentDirections.actionSpecificLineFragmentToDisplayAlertFragment(stationID)
            )
        })
        binding.recyclerSpecificLineAlertStations.adapter = specificLineAlertsAdapter

        //Create adapter to display all line stations
        val specificLineAdapter = SpecificLineAdapter(viewModel.lineName, SpecificLineListener { stationID ->
            findNavController().navigate(
                    SpecificLineFragmentDirections.actionSpecificLineFragmentToDisplayAlertFragment(stationID)
            )
        })
        binding.recyclerSpecificLine.adapter = specificLineAdapter

        viewModel.stationsByLineWithAlerts.observe(viewLifecycleOwner, Observer {
            it?.let {
                specificLineAlertsAdapter.submitList(it)

                //If no alerts
                val heading = binding.tvElevatorAlerts
                val alertsRV = binding.recyclerSpecificLineAlertStations

                if (it.isEmpty()) {
                    heading.visibility = View.GONE
                    alertsRV.visibility = View.GONE
                } else {
                    heading.visibility = View.VISIBLE
                    alertsRV.visibility = View.VISIBLE
                }
            }
        })

        viewModel.stationsByLine.observe(viewLifecycleOwner, Observer {
            it?.let {
                specificLineAdapter.submitList(it)
            }
        })

//        linesAdapter.setToolbarTextView()

        return binding.root
    }
}

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
