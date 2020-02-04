package com.github.cta_elevator_alerts_kotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentDisplayAlertBinding
import com.github.cta_elevator_alerts_kotlin.domain.Station
import com.github.cta_elevator_alerts_kotlin.viewmodel.DisplayAlertViewModel
import com.github.cta_elevator_alerts_kotlin.viewmodel.DisplayAlertViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class DisplayAlertFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        val binding: FragmentDisplayAlertBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_display_alert,
                container,
                false
        )
        val arguments = DisplayAlertFragmentArgs.fromBundle(arguments!!)
        val stationID = arguments.stationID

        //Create ViewModel and initialize line name at construction
        val application = requireNotNull(this.activity).application
        val viewModelFactory = DisplayAlertViewModelFactory(application, stationID)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(DisplayAlertViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        binding.imgStarIcon.setOnClickListener{onClick(viewModel, binding)}
//        binding.favoritedText.setOnClickListener{onClick(viewModel, binding)}

        return binding.root
    }
}

//    private fun onClick(viewModel: DisplayAlertViewModel, binding: FragmentDisplayAlertBinding){
//        when (viewModel.isFavorite){
//            true -> {
////                binding.imgStarIcon.setImageResource(R.drawable.star_icon_empty)
////                binding.favoritedText.setText(R.string.add_to_favorites)
////                viewModel.removeFavorite()
//                viewModel.updateStationInfo(viewModel.stationID)
//            }
//            false -> {
////                binding.imgStarIcon.setImageResource(R.drawable.star_icon_full)
////                binding.favoritedText.setText(R.string.added_to_favorites)
////                viewModel.addFavorite()
//                viewModel.updateStationInfo(viewModel.stationID)
//            }
//        }
//    }
//}
