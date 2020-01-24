package com.github.cta_elevator_alerts_kotlin.fragments


import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.adapters.StationListAdapter
import com.github.cta_elevator_alerts_kotlin.adapters.StationListener
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentFavoriteAlertsBinding
import com.github.cta_elevator_alerts_kotlin.utils.NetworkWorker
import com.github.cta_elevator_alerts_kotlin.viewmodels.MainViewModel

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
            addOneTimeWorker()
            binding.swipeRefreshFavorites.isRefreshing = false
        }

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this

        //Create adapter to display favorites
        val favoritesAdapter = StationListAdapter(StationListener { stationID ->
            findNavController().navigate(
                    FavoriteAlertsFragmentDirections.actionFavoriteAlertsFragmentToDisplayAlert(stationID)
            )
        })
        binding.recyclerFavoriteStations.adapter = favoritesAdapter

        //Create SharedPreferences for last updated date/time
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity!!)
        val tvAlertsTime = binding.txtUpdateAlertTime
        val time = sharedPreferences.getString("LastUpdatedTime", "")
        if (time != null && time != "") tvAlertsTime.text = time

        //        addTestButtons();

        viewModel.favorites.observe(viewLifecycleOwner, Observer {
            it?.let {
                favoritesAdapter.submitList(it)

                //If no alerts
                val tv = binding.noFavoritesAdded
                if (it.isEmpty()) {
                    tv.visibility = View.VISIBLE
                } else {
                    tv.visibility = View.GONE
                }
            }
        })

        viewModel.updateAlertsTime.observe(this, Observer<String>{
            tvAlertsTime.text = it

            val editor = sharedPreferences.edit()
            editor.putString("LastUpdatedTime", it)
            editor.apply()
        })

        addOneTimeWorker()
        return binding.root
    }

    private fun addOneTimeWorker() {
        val request = OneTimeWorkRequest.Builder(NetworkWorker::class.java)
                .addTag("OneTimeWork")
                .setConstraints(Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .setRequiresStorageNotLow(true)
                        .build())
                .build()

        WorkManager.getInstance(activity!!).enqueue(request)
    }
}
