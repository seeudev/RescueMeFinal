package com.example.rescueme

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DevPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_developerpage)

        val button_home = findViewById<ImageButton>(R.id.button_home)
        button_home.setOnClickListener {
            Log.e("This is CSIT284","Home button is clicked!")
            Toast.makeText(this,"The home button is clicked!", Toast.LENGTH_LONG).show()
            val intent = Intent(this,LandingActivity::class.java)
            startActivity(intent)
        }

        val button_profile = findViewById<ImageButton>(R.id.button_profile)
        button_profile.setOnClickListener {
            Log.e("This is CSIT284","Settings button is clicked!")
            Toast.makeText(this,"The Setting button is clicked!", Toast.LENGTH_LONG).show()
            val intent = Intent(this,ProfilePageActivity::class.java)
            startActivity(intent)
        }
    }
}