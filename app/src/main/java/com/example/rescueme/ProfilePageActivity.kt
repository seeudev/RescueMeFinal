package com.example.rescueme

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast


class ProfilePageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_profilepage)

        val settingsArrow = findViewById<ImageView>(R.id.ic_arrow2)
        val aboutUsArrow = findViewById<ImageView>(R.id.ic_arrow4)
        val logoutArrow = findViewById<ImageView>(R.id.ic_arrow6)
        val contactButton = findViewById<RelativeLayout>(R.id.contactButton)
        val homeButton = findViewById<RelativeLayout>(R.id.homeButton)

        settingsArrow.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        aboutUsArrow.setOnClickListener {
            val intent = Intent(this, DevPageActivity::class.java)
            startActivity(intent)
        }

        logoutArrow.setOnClickListener {
            // For now, let's just show a toast message for logout
            Toast.makeText(this, "Logout functionality will be implemented here", Toast.LENGTH_SHORT).show()
            // In a real application, you would handle user logout and navigate to the LoginActivity
            // val intent = Intent(this, LoginActivity::class.java)
            // startActivity(intent)
            // finish() // Close the current activity
        }
        //Buttons
        contactButton.setOnClickListener {
            val intent = Intent(this, ContactsActivity::class.java) // Corrected class name
            startActivity(intent)
        }
        homeButton.setOnClickListener {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }

    }
}