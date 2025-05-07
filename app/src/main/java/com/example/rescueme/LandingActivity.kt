package com.example.rescueme

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Geocoder
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
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        // Add click listener for the choking card
        findViewById<CardView>(R.id.chokingCard)?.setOnClickListener {
            startActivity(Intent(this, ChokingGuideActivity::class.java))
        }

        // Add click listener for the CPR card
        findViewById<CardView>(R.id.cprCard)?.setOnClickListener {
            startActivity(Intent(this, CprGuideActivity::class.java))
        }

        // Add click listener for the wound care card
        findViewById<CardView>(R.id.woundCareCard)?.setOnClickListener {
            startActivity(Intent(this, WoundCareGuideActivity::class.java))
        }

        // Add click listener for ambulance card
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

    private fun getLocationName(latitude: Double, longitude: Double, callback: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val geocoder = Geocoder(this@LandingActivity, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                
                withContext(Dispatchers.Main) {
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val locationName = buildString {
                            // Add street address if available
                            address.thoroughfare?.let { append(it) }
                            
                            // Add sublocality (neighborhood) if available
                            address.subLocality?.let {
                                if (isNotEmpty()) append(", ")
                                append(it)
                            }
                            
                            // Add locality (city) if available
                            address.locality?.let {
                                if (isNotEmpty()) append(", ")
                                append(it)
                            }
                            
                            // Add admin area (state/province) if available
                            address.adminArea?.let {
                                if (isNotEmpty()) append(", ")
                                append(it)
                            }
                            
                            // Add country if available
                            address.countryName?.let {
                                if (isNotEmpty()) append(", ")
                                append(it)
                            }
                        }
                        callback(locationName)
                    } else {
                        callback("Unknown Location")
                    }
                }
            } catch (e: IOException) {
                Log.e("Geocoder", "Error getting location name: ${e.message}")
                withContext(Dispatchers.Main) {
                    callback("Unknown Location")
                }
            }
        }
    }

    private fun sendEmergencyAlert() {
        val userId = auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance()

        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    database.getReference("users/$userId/contacts")
                        .orderByChild("relation")
                        .equalTo("Emergency Contact")
                        .limitToFirst(1)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                val contact = snapshot.children.first()
                                val contactPhone = contact.child("phoneNumber").getValue(String::class.java)

                                if (contactPhone != null) {
                                    try {
                                        // Validate phone number format
                                        val phoneNumber = contactPhone.trim()
                                        if (!phoneNumber.matches(Regex("^\\+?[0-9]{10,15}$"))) {
                                            Log.e("EmergencyAlert", "Invalid phone number format: $phoneNumber")
                                            Toast.makeText(this, "Invalid emergency contact phone number format", Toast.LENGTH_LONG).show()
                                            return@addOnSuccessListener
                                        }

                                        // Get location name using reverse geocoding
                                        getLocationName(location.latitude, location.longitude) { locationName ->
                                            val message = """
                                            EMERGENCY ALERT!

                                            ${auth.currentUser?.displayName ?: "sender"} requires urgent help.

                                            Location: $locationName (${location.latitude}, ${location.longitude})
                                            Time of Alert: ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())} (${SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())})

                                            Panic Button Pressed
                                        """.trimIndent()

                                            val smsManager = SmsManager.getDefault()
                                            val sentIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE)
                                            val deliveredIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_DELIVERED"), PendingIntent.FLAG_IMMUTABLE)

                                            // Register for SMS sent and delivered notifications
                                            registerReceiver(object : BroadcastReceiver() {
                                                override fun onReceive(context: Context, intent: Intent) {
                                                    when (resultCode) {
                                                        Activity.RESULT_OK -> {
                                                            Toast.makeText(context, "Emergency alert sent successfully", Toast.LENGTH_SHORT).show()
                                                            Log.d("EmergencyAlert", "SMS sent successfully to $phoneNumber")
                                                        }
                                                        SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                                                            Toast.makeText(context, "Failed to send emergency alert", Toast.LENGTH_SHORT).show()
                                                            Log.e("EmergencyAlert", "Generic failure in sending SMS to $phoneNumber")
                                                        }
                                                        SmsManager.RESULT_ERROR_NO_SERVICE -> {
                                                            Toast.makeText(context, "No service available", Toast.LENGTH_SHORT).show()
                                                            Log.e("EmergencyAlert", "No service available for sending SMS to $phoneNumber")
                                                        }
                                                        SmsManager.RESULT_ERROR_NULL_PDU -> {
                                                            Toast.makeText(context, "PDU error in sending alert", Toast.LENGTH_SHORT).show()
                                                            Log.e("EmergencyAlert", "PDU error when sending SMS to $phoneNumber")
                                                        }
                                                        SmsManager.RESULT_ERROR_RADIO_OFF -> {
                                                            Toast.makeText(context, "Radio is off", Toast.LENGTH_SHORT).show()
                                                            Log.e("EmergencyAlert", "Radio is off when sending SMS to $phoneNumber")
                                                        }
                                                    }
                                                }
                                            }, IntentFilter("SMS_SENT"))

                                            registerReceiver(object : BroadcastReceiver() {
                                                override fun onReceive(context: Context, intent: Intent) {
                                                    when (resultCode) {
                                                        Activity.RESULT_OK -> {
                                                            Log.d("EmergencyAlert", "SMS delivered successfully to $phoneNumber")
                                                        }
                                                        Activity.RESULT_CANCELED -> {
                                                            Log.e("EmergencyAlert", "SMS not delivered to $phoneNumber")
                                                        }
                                                    }
                                                }
                                            }, IntentFilter("SMS_DELIVERED"))

                                            try {
                                                // Split message if it's too long
                                                val messageParts = smsManager.divideMessage(message)
                                                smsManager.sendMultipartTextMessage(
                                                    phoneNumber,
                                                    null,
                                                    messageParts,
                                                    ArrayList<PendingIntent>().apply { add(sentIntent) },
                                                    ArrayList<PendingIntent>().apply { add(deliveredIntent) }
                                                )
                                                Log.d("EmergencyAlert", "SMS sending initiated to $phoneNumber")
                                            } catch (e: Exception) {
                                                Log.e("EmergencyAlert", "Exception while sending SMS to $phoneNumber: ${e.message}")
                                                Toast.makeText(this, "Failed to send emergency alert: ${e.message}", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.e("EmergencyAlert", "Exception in sendEmergencyAlert: ${e.message}")
                                        Toast.makeText(this, "Failed to send emergency alert: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Log.e("EmergencyAlert", "Emergency contact phone number is null")
                                    Toast.makeText(this, "Emergency contact phone number not found", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Log.e("EmergencyAlert", "No emergency contact found in database")
                                Toast.makeText(this, "No emergency contact found. Please add an emergency contact in settings.", Toast.LENGTH_LONG).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("EmergencyAlert", "Failed to fetch emergency contact: ${e.message}")
                            Toast.makeText(this, "Failed to fetch emergency contact: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                } ?: run {
                    Log.e("EmergencyAlert", "Location is null")
                    Toast.makeText(this, "Unable to get current location", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("EmergencyAlert", "Failed to get location: ${e.message}")
                Toast.makeText(this, "Failed to get location: ${e.message}", Toast.LENGTH_LONG).show()
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