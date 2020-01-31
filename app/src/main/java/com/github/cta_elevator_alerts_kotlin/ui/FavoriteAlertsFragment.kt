package com.github.cta_elevator_alerts_kotlin.ui


import android.os.Bundle
import android.preference.PreferenceManager
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
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentFavoriteAlertsBinding
import com.github.cta_elevator_alerts_kotlin.databinding.IndividualStationBinding
import com.github.cta_elevator_alerts_kotlin.domain.Station
import com.github.cta_elevator_alerts_kotlin.work.NetworkWorker
import com.github.cta_elevator_alerts_kotlin.viewmodel.AllAlertsViewModel
import com.github.cta_elevator_alerts_kotlin.viewmodel.FavoriteAlertsViewModel
import com.github.cta_elevator_alerts_kotlin.work.RefreshAlertsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavoriteAlertsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate view and obtain an instance of the binding class
        val binding: FragmentFavoriteAlertsBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_favorite_alerts,
                container,
                false
        )

        binding.swipeRefreshFavorites.setOnRefreshListener {
//            addOneTimeWorker()
            //TODO: one time worker for refresh

//            val applicationScope = CoroutineScope(Dispatchers.Default)
//
//            applicationScope.launch {
//                val oneTimeAlertRequest = OneTimeWorkRequest.Builder(RefreshAlertsWorker::class.java)
//                        .build()
//
//                WorkManager.getInstance(context).enqueue(oneTimeAlertRequest)
//            }
            binding.swipeRefreshFavorites.isRefreshing = false
        }

        val viewModel = ViewModelProvider(this).get(FavoriteAlertsViewModel::class.java)

        binding.lifecycleOwner = this

        //Create adapter to display favorites
        val favoritesAdapter = StationListAdapter(StationListener { stationID ->
            findNavController().navigate(
                    FavoriteAlertsFragmentDirections.actionFavoriteAlertsFragmentToDisplayAlert(stationID)
            )
        })
        binding.recyclerFavoriteStations.adapter = favoritesAdapter

//        //Create SharedPreferences for last updated date/time
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity!!)
//        val tvAlertsTime = binding.txtUpdateAlertTime
//        val time = sharedPreferences.getString("LastUpdatedTime", "")
//        if (time != null && time != "") tvAlertsTime.text = time

        //        addTestButtons();

        viewModel.favoriteStations.observe(viewLifecycleOwner, Observer {
            it?.let {
                favoritesAdapter.submitList(it)

                //If no alerts
                if (it.isEmpty()) {
                    binding.noFavoritesAdded.visibility = View.VISIBLE
                } else {
                    binding.noFavoritesAdded.visibility = View.GONE
                }
            }
        })

        viewModel.lastUpdatedTime.observe(viewLifecycleOwner, Observer<String>{
            binding.txtUpdateAlertTime.text = it
        })

//        addOneTimeWorker()
        return binding.root
    }
//
//    private fun addOneTimeWorker() {
//        val request = OneTimeWorkRequest.Builder(NetworkWorker::class.java)
//                .addTag("OneTimeWork")
//                .setConstraints(Constraints.Builder()
//                        .setRequiresBatteryNotLow(true)
//                        .setRequiresStorageNotLow(true)
//                        .build())
//                .build()
//
//        WorkManager.getInstance(activity!!).enqueue(request)
//    }
}
