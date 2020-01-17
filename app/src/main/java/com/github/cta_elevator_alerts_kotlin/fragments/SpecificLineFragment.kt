package com.github.cta_elevator_alerts_kotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.adapters.SpecificLineAdapter
import com.github.cta_elevator_alerts_kotlin.adapters.SpecificLineAlertListener
import com.github.cta_elevator_alerts_kotlin.adapters.SpecificLineAlertsAdapter
import com.github.cta_elevator_alerts_kotlin.adapters.SpecificLineListener
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentSpecificLineBinding
import com.github.cta_elevator_alerts_kotlin.model.Station
import com.github.cta_elevator_alerts_kotlin.viewmodels.MainViewModel
import com.github.cta_elevator_alerts_kotlin.viewmodels.SpecificLineViewModel

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

        val viewModel = ViewModelProviders.of(this).get(SpecificLineViewModel::class.java)
        viewModel.line = lineName
        binding.lifecycleOwner = this

        //Create adapter to display all alerts
        val specificLineAlertsAdapter = SpecificLineAlertsAdapter(SpecificLineAlertListener { stationID ->
            findNavController().navigate(
                    SpecificLineFragmentDirections.actionSpecificLineFragmentToDisplayAlert(stationID)
            )
        })
        binding.recyclerSpecificLineAlertStations.adapter = specificLineAlertsAdapter

        //Create adapter to display all alerts
        val specificLineAdapter = SpecificLineAdapter(SpecificLineListener { stationID ->
            findNavController().navigate(
                    SpecificLineFragmentDirections.actionSpecificLineFragmentToDisplayAlert(stationID)
            )
        })
        binding.recyclerSpecificLine.adapter = specificLineAdapter

        viewModel.allLineAlerts.observe(viewLifecycleOwner, Observer {
            it?.let {
                specificLineAdapter.submitList(it)

                //If no alerts
                val heading = binding.tvElevatorAlerts
                val alertsRV = binding.recyclerSpecificLineAlertStations

                if (it.isEmpty()) {
                    heading.visibility = View.VISIBLE
                    alertsRV.visibility = View.VISIBLE
                } else {
                    heading.visibility = View.GONE
                    alertsRV.visibility = View.GONE
                }
            }
        })

        viewModel.lineStations.observe(viewLifecycleOwner, Observer {
            it?.let {
                specificLineAdapter.submitList(it)
            }
        })

//        linesAdapter.setToolbarTextView()

        return binding.root
    }

//        viewModel.favorites.observe(this, Observer<List<Station>>{
//            lineAlertsAdapter.notifyDataSetChanged()
//            specificLineAdapter.notifyDataSetChanged()
//        })

}
