package com.github.cta_elevator_alerts_kotlin.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.generated.callback.OnClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * MainActivity displays favorite stations and their
 * elevator status, all current alerts,
 * the most updated date/time, and the privacy policy.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class MainActivity : AppCompatActivity() {

//    lateinit var stationAlertsViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up bottom navigation
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController = findNavController(R.id.my_nav_host_fragment)
        bottomNav.setupWithNavController(navController)
    }

    fun toAboutFragment(v: View){
        Navigation.findNavController(this, R.id.my_nav_host_fragment).navigate(R.id.aboutFragment)
    }


    //    private fun addConnectionStatusObserver() {
//        stationAlertsViewModel.connectionStatus.observe(this, Observer<Boolean>{
//            if ((!it)) {
//                val toast = Toast.makeText(this, "Not connected - please refresh!", Toast.LENGTH_SHORT)
//                toast.show()
//            }
//            stationAlertsViewModel.setConnectionStatus(true)
//        })
//    }
}
