package com.example.rescueme

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import androidx.cardview.widget.CardView

class LandingActivity : AppCompatActivity() {

    private var isLongPress = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val SMS_PERMISSION_REQUEST_CODE = 1002
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_landingpage)

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        auth = FirebaseAuth.getInstance()

        // Panic Button
        val panicButton = findViewById<Button>(R.id.panicButton)
        
        panicButton.setOnClickListener {
            if (checkLocationPermission()) {
                if (checkSmsPermission()) {
                    showConfirmationDialog()
                } else {
                    requestSmsPermission()
                }
            } else {
                requestLocationPermission()
            }
        }

        // Long press detection
        panicButton.setOnLongClickListener {
            if (checkLocationPermission()) {
                if (checkSmsPermission()) {
                    sendEmergencyAlert()
                } else {
                    requestSmsPermission()
                }
            } else {
                requestLocationPermission()
            }
            true
        }

        //Navigation bar
        findViewById<RelativeLayout>(R.id.homeButton).setOnClickListener {
            Log.e("This is CSIT284", "Home button is clicked!")
            Toast.makeText(this, "The home button is clicked!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LandingActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.contactButton).setOnClickListener {
            Log.e("This is CSIT284", "Contact button is clicked!")
            Toast.makeText(this, "The Contact button is clicked!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, ContactsActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.profileButton).setOnClickListener {
            Log.e("This is CSIT284", "Profile button is clicked!")
            Toast.makeText(this, "The Profile button is clicked!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, ProfilePageActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.notificationsButton).setOnClickListener {
            Log.e("This is CSIT284", "Notifications button is clicked!")
            Toast.makeText(this, "The Notifications button is clicked!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, NotificationsActivity::class.java))
        }
        findViewById<RelativeLayout>(R.id.emergencyButton).setOnClickListener {
            Log.e("This is CSIT284", "Emergency button is clicked!")
            Toast.makeText(this, "The Emergency button is clicked!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, EmergencyActivity::class.java))
        }

        // Add click listener for the injury card
        findViewById<CardView>(R.id.cardViewInjury)?.setOnClickListener {
            startActivity(Intent(this, InjuryChecklistActivity::class.java))
        }

        // Add click listener for the accident card
        findViewById<CardView>(R.id.cardAccident).setOnClickListener {
            startActivity(Intent(this, AccidentChecklistActivity::class.java))
        }

        // Add click listener for the bleeding card
        findViewById<CardView>(R.id.bleedingCard).setOnClickListener {
            startActivity(Intent(this, BleedingChecklistActivity::class.java))
        }

        // Add click listener for the cant breathe card
        findViewById<CardView>(R.id.cardViewCantBreathe).setOnClickListener {
            startActivity(Intent(this, CantBreatheChecklistActivity::class.java))
        }

        // Add click listener for the chest pain card
        findViewById<CardView>(R.id.chestPainCard).setOnClickListener {
            val intent = Intent(this, ChestPainChecklistActivity::class.java)
            startActivity(intent)
        }

        // Add click listener for the unconscious card
        findViewById<CardView>(R.id.cardViewUnconscious).setOnClickListener {
            val intent = Intent(this, UnconsciousChecklistActivity::class.java)
            startActivity(intent)
        }

        //Add click listener for ambulance card
        findViewById<CardView>(R.id.cardViewAmbulance).setOnClickListener{
            try {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:911")
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to make emergency call: ${e.message}", Toast.LENGTH_LONG).show()
                // Fallback to dialer if direct call fails
                try {
                    val dialIntent = Intent(Intent.ACTION_DIAL)
                    dialIntent.data = Uri.parse("tel:911")
                    startActivity(dialIntent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to open dialer: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun checkSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS
            ),
            SMS_PERMISSION_REQUEST_CODE
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
                    Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Location permission is required for emergency alerts", Toast.LENGTH_LONG).show()
                }
            }
            SMS_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show()
                    showConfirmationDialog()
                } else {
                    Toast.makeText(this, "SMS permission is required to send emergency alerts", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showConfirmationDialog() {
        try {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Emergency Alert")
            builder.setMessage("Are you sure you want to send an emergency alert?")
            builder.setPositiveButton("Yes") { _, _ ->
                sendEmergencyAlert()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        } catch (e: Exception) {
            Log.e("PanicButton", "Error showing confirmation dialog: ${e.message}")
            sendEmergencyAlert()
        }
    }

    private fun sendEmergencyAlert() {
        val userId = auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance()
        
        // Get user's current location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    // Get emergency contact details from contacts collection
                    database.getReference("users/$userId/contacts")
                        .orderByChild("relation")
                        .equalTo("Emergency Contact")
                        .limitToFirst(1)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                val contact = snapshot.children.first()
                                val contactName = contact.child("name").getValue(String::class.java)
                                val contactPhone = contact.child("phoneNumber").getValue(String::class.java)
                                
                                if (contactName != null && contactPhone != null) {
                                    // Create emergency message
                                    val message = """
                                        EMERGENCY ALERT!

                                        ${auth.currentUser?.displayName ?: "User"} requires urgent help.

                                        Location: [Link to Maps/Directions] (${location.latitude}, ${location.longitude})
                                        Time of Alert: ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())} (${SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())})

                                        Panic Button Pressed
                                    """.trimIndent()
                                    
                                    // Send SMS to emergency contact
                                    try {
                                        val smsManager = SmsManager.getDefault()
                                        smsManager.sendTextMessage(
                                            contactPhone,
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                        
                                        // Log the SMS sending attempt
                                        Log.d("EmergencyAlert", "Attempting to send SMS to: $contactPhone")
                                        Log.d("EmergencyAlert", "Message content: $message")
                                        
                                        Toast.makeText(this, "Emergency alert sent successfully!", Toast.LENGTH_LONG).show()
                                    } catch (e: Exception) {
                                        Log.e("EmergencyAlert", "Failed to send SMS: ${e.message}", e)
                                        Toast.makeText(this, "Failed to send emergency alert: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Toast.makeText(this, "Emergency contact details are incomplete!", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Toast.makeText(this, "No emergency contact found! Please add an emergency contact in the Contacts section.", Toast.LENGTH_LONG).show()
                            }
                        }
                        .addOnFailureListener {
                            Log.e("EmergencyAlert", "Failed to get emergency contact details", it)
                            Toast.makeText(this, "Failed to get emergency contact details", Toast.LENGTH_LONG).show()
                        }
                } ?: run {
                    Toast.makeText(this, "Unable to get current location", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val SMS_PERMISSION_REQUEST_CODE = 1002
    }
}