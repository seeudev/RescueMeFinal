package com.example.rescueme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rescueme.utils.EmergencyAdapter

class EmergencyActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EmergencyAdapter
    private lateinit var app: RescueMeApp
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_emergency)

        app = RescueMeApp.getInstance()

        // Initialize RecyclerView for emergency contacts
        recyclerView = findViewById(R.id.emergencyContactsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get first 3 contacts
        val emergencyContacts = app.getContacts().take(3)
        adapter = EmergencyAdapter(this, emergencyContacts) { contact ->
            // Handle contact click if needed
        }
        recyclerView.adapter = adapter

        // Initialize video view
        videoView = findViewById(R.id.emergencyVideoView)

        // Set up emergency guide cards
        setupEmergencyGuideCards()

        // Set up navigation
        setupNavigationBar()
    }

    private fun setupEmergencyGuideCards() {
        // Injury card
        findViewById<CardView>(R.id.injuryCard).setOnClickListener {
            playEmergencyVideo(R.raw.injury_treatment)
        }

        // Bleeding card
        findViewById<CardView>(R.id.bleedingCard).setOnClickListener {
            playEmergencyVideo(R.raw.bleeding_treatment)
        }

        // Breathing card
        findViewById<CardView>(R.id.breathingCard).setOnClickListener {
            playEmergencyVideo(R.raw.breathing_treatment)
        }
    }

    private fun playEmergencyVideo(videoResourceId: Int) {
        videoView.setVideoPath("android.resource://" + packageName + "/" + videoResourceId)
        videoView.start()
    }

    private fun setupNavigationBar() {
        findViewById<View>(R.id.homeButton).setOnClickListener {
            startActivity(Intent(this, LandingActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.profileButton).setOnClickListener {
            startActivity(Intent(this, ProfilePageActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.contactButton).setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.notificationsButton).setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
            finish()
        }
    }
} 