package com.example.rescueme


import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import androidx.appcompat.app.AlertDialog;

class LandingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_landingpage)

//        val textview_welcome_message = findViewById<TextView>(R.id.tv_welcome)
//        intent?.let {
//            it.getStringExtra("username")?.let { username ->
//                textview_welcome_message.text = "Hello $username!"
//            }
//        }

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
            // You might want to start a NotificationsActivity here instead
            // startActivity(Intent(this, NotificationsActivity::class.java))
        }

        // It looks like there's no explicit "Settings" button in your XML.
        // If you intend for one of the existing buttons to act as settings,
        // you'll need to update its ID in the XML and then set the OnClickListener here.
        // For now, I'll comment out the original settings button logic.
        /*
        findViewById<ImageButton>(R.id.button_settings).setOnClickListener {
            Log.e("This is CSIT284", "Settings button is clicked!")
            Toast.makeText(this, "The Setting button is clicked!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        */

        // Similarly, there's no "Logout" button in the bottom navigation.
        // If you have a logout functionality elsewhere, you'll need to connect it.
        // I'll comment out the original logout button logic.
        /*
        findViewById<ImageButton>(R.id.button_logout).setOnClickListener {
            Log.e("This is CSIT284", "Logout button is clicked!")
            Toast.makeText(this, "The logout button is clicked!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        */
        findViewById<RelativeLayout>(R.id.emergencyButton).setOnClickListener {
            Log.e("This is CSIT284", "Emergency button is clicked!")
            Toast.makeText(this, "The Emergency button is clicked!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, ContactsActivity::class.java))
        }

        findViewById<RelativeLayout>(R.id.profileButton).setOnClickListener {
            Log.e("This is CSIT284", "Profile button is clicked!")
            Toast.makeText(this, "The Profile button is clicked!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, ProfilePageActivity::class.java))
        }
    }

    // The clickNewCalendar function is not being used in your current XML layout.
    // If you want the "Emergency" button to navigate to ContactsActivity,
    // you can update the OnClickListener for the emergencyButton.
}