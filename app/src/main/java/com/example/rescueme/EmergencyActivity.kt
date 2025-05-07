package com.example.rescueme

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rescueme.adapters.EmergencyAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class EmergencyActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EmergencyAdapter
    private lateinit var app: RescueMeApp
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_emergency)

        app = RescueMeApp.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
        }
        recyclerView.adapter = adapter

        // Load contacts from Firebase
        loadEmergencyContacts()

        // Set up emergency guide cards
        setupEmergencyGuideCards()

        // Set up navigation
        setupNavigationBar()

        // Set up maps card click listener
        findViewById<CardView>(R.id.mapsCard)?.setOnClickListener {
            checkLocationPermissionAndOpenMaps()
        }
    }

    private fun checkLocationPermissionAndOpenMaps() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted, get location
                getLastLocation()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // Show explanation to the user
                Toast.makeText(
                    this,
                    "Location permission is required to show your current location",
                    Toast.LENGTH_LONG
                ).show()
                requestLocationPermission()
            }
            else -> {
                // Request the permission
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, get location
                    getLastLocation()
                } else {
                    // Permission denied
                    Toast.makeText(
                        this,
                        "Location permission is required to show your current location",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        openGoogleMaps(location.latitude, location.longitude)
                    } else {
                        Toast.makeText(
                            this,
                            "Unable to get your location. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error getting location: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun openGoogleMaps(latitude: Double, longitude: Double) {
        // First try to open Google Maps specifically
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(My Current Location)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            // If Google Maps is not installed, try to open any map application
            val genericMapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            if (genericMapIntent.resolveActivity(packageManager) != null) {
                startActivity(genericMapIntent)
            } else {
                // If no map application is available, show a dialog with options
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("No Map Application Found")
                    .setMessage("Would you like to install Google Maps?")
                    .setPositiveButton("Install") { _, _ ->
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps")))
                        } catch (e: android.content.ActivityNotFoundException) {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")))
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
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