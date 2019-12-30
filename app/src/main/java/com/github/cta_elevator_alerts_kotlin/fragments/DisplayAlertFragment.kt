package com.github.cta_elevator_alerts_kotlin.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.cta_elevator_alerts_kotlin.R

/**
 * A simple [Fragment] subclass.
 */
class DisplayAlertFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_alert, container, false)
    }


}
