package com.github.cta_elevator_alerts_kotlin.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

import com.github.cta_elevator_alerts.R
import com.github.cta_elevator_alerts_kotlin.model.StationRepository
import com.github.cta_elevator_alerts.viewmodels.DisplayAlertViewModel

/**
 * DisplayAlertActivity shows the details of a
 * specific elevator outage alert
 * (description and date/time it began).
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class DisplayAlertActivity : AppCompatActivity() {
    private lateinit var starIcon: ImageView
    private lateinit var favoriteText: TextView
    private var mRepository: StationRepository? = null
    lateinit var stationID: String
    var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_alert)
        val tvShortDesc = findViewById<TextView>(R.id.txt_alert_shortDesc)
        val toolbarText = findViewById<TextView>(R.id.txt_toolbar)
        starIcon = this.findViewById(R.id.img_star_icon)
        favoriteText = this.findViewById(R.id.favorited_text)
        mRepository = StationRepository.getInstance(this.application)

        stationID = intent.getStringExtra("stationID")

        //Get ViewModel
        val mDisplayAlertViewModel = ViewModelProviders.of(this).get(DisplayAlertViewModel::class.java)
        mDisplayAlertViewModel.setStationID(stationID)

        toolbarText.text = mDisplayAlertViewModel.stationName //Set Station Name

        isFavorite = mDisplayAlertViewModel.isFavorite
        val hasElevator = mRepository?.mGetHasElevator(stationID) ?: false

        if (isFavorite && hasElevator) {
            starIcon.setImageResource(R.drawable.star_icon_full)
            favoriteText.setText(R.string.added_to_favorites)
        } else if (hasElevator) {
            starIcon.setImageResource(R.drawable.star_icon_empty)
            favoriteText.setText(R.string.add_to_favorites)
        } else {
            starIcon.visibility = View.GONE
            favoriteText.setText(R.string.no_elevator_title)
            favoriteText.setTextColor(Color.parseColor("#757575"))
        }

        //Set alert description
        if (!mDisplayAlertViewModel.hasElevator)
            tvShortDesc.setText(R.string.no_elevator)
        else if (!mDisplayAlertViewModel.hasAlert)
            tvShortDesc.setText(R.string.present_elevator)
        else
            tvShortDesc.text = mDisplayAlertViewModel.shortDesc
    }

    fun toMainActivity(v: View) {
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        finish()
        startActivity(i)
    }

    fun onBackPressed(v: View) {
        this.onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (intent.getBooleanExtra("fromMain", false)) {
            val intent = Intent(this@DisplayAlertActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun clickStarIcon(v: View) {
        isFavorite = if (isFavorite) {
            starIcon.setImageResource(R.drawable.star_icon_empty)
            favoriteText.setText(R.string.add_to_favorites)
            mRepository?.removeFavorite(stationID)
            false
        } else {
            starIcon.setImageResource(R.drawable.star_icon_full)
            favoriteText.setText(R.string.added_to_favorites)
            mRepository?.addFavorite(stationID)
            true
        }
    }
}
