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

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_settingpage)

        val button_back = findViewById<ImageButton>(R.id.back_button)
        button_back.setOnClickListener {
            Log.e("This is CSIT284","Back button is clicked!")
            Toast.makeText(this,"The Back button is clicked!", Toast.LENGTH_LONG).show()
            val intent = Intent(this,LandingActivity::class.java)
            startActivity(intent)
        }
        val button_developers = findViewById<ImageButton>(R.id.developers_button)
        button_developers.setOnClickListener {
            Log.e("This is CSIT284","Dev button is clicked!")
            Toast.makeText(this,"The Dev button is clicked!", Toast.LENGTH_LONG).show()
            val intent = Intent(this,DevPageActivity::class.java)
            startActivity(intent)
        }
    }
}