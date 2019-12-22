package com.github.cta_elevator_alerts.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import com.github.cta_elevator_alerts.R

/**
 * AboutActivity shows information about the app.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val t2 = findViewById<TextView>(R.id.txt_privacy)
        t2.movementMethod = LinkMovementMethod.getInstance()

        val contactEmail = findViewById<TextView>(R.id.txt_contact_email)
        contactEmail.movementMethod = LinkMovementMethod.getInstance()

        val about = findViewById<ImageView>(R.id.img_home_icon)
        about.visibility = View.INVISIBLE
    }

    fun toMainActivity(v: View) {
        val intent = Intent(this@AboutActivity, MainActivity::class.java)
        startActivity(intent)
    }

    fun onBackPressed(v: View) {
        this.onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@AboutActivity, MainActivity::class.java)
        startActivity(intent)
    }
}
