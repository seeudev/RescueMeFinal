package com.example.rescueme

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rescueme.adapters.EmergencyAdapter

class EmergencyActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EmergencyAdapter
    private lateinit var app: RescueMeApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_emergency)

        app = RescueMeApp.getInstance()

        // Initialize RecyclerView for emergency contacts
        recyclerView = findViewById(R.id.emergencyContactsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list first
        adapter = EmergencyAdapter(this, emptyList()) { contact ->
            val intent = Intent(this, ContactDetailActivity::class.java).apply {
                putExtra("contact_id", contact.id)
                putExtra("contact_name", contact.name)
                putExtra("contact_phone", contact.phoneNumber)
                putExtra("contact_relation", contact.relation)
                putExtra("contact_image", contact.profileImageResourceId)
                putExtra("is_emergency_service", contact.isEmergencyService)
                putExtra("service_type", contact.serviceType)
            }
            startActivity(intent)
            // Handle contact click if needed
        }
        recyclerView.adapter = adapter

        // Load contacts from Firebase
        loadEmergencyContacts()

        // Set up emergency guide cards
        setupEmergencyGuideCards()

        // Set up navigation
        setupNavigationBar()


    }

    private fun loadEmergencyContacts() {
        app.getContacts { contacts ->
            // Get first 3 contacts
            val emergencyContacts = contacts.take(3)
            adapter.updateContacts(emergencyContacts.toMutableList())
        }
    }

    private fun setupEmergencyGuideCards() {
        // Add click listener for the injury card
        findViewById<CardView>(R.id.injuryCard)?.setOnClickListener {
            startActivity(Intent(this, InjuryChecklistActivity::class.java))
        }

        // Add click listener for the accident card
//        findViewById<CardView>(R.id.cardAccident).setOnClickListener {
//            startActivity(Intent(this, AccidentChecklistActivity::class.java))
//        }

        // Add click listener for the bleeding card
        findViewById<CardView>(R.id.bleedingCard).setOnClickListener {
            startActivity(Intent(this, BleedingChecklistActivity::class.java))
        }

        // Add click listener for the cant breathe card
        findViewById<CardView>(R.id.breathingCard).setOnClickListener {
            startActivity(Intent(this, CantBreatheChecklistActivity::class.java))
        }
    }

    private fun playEmergencyVideo(videoResourceId: Int) {
        val intent = Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra("video_resource_id", videoResourceId)
        }
        startActivity(intent)
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

        findViewById<View>(R.id.emergencyButton).setOnClickListener {
            startActivity(Intent(this, EmergencyActivity::class.java))
            finish()
        }
    }
} 