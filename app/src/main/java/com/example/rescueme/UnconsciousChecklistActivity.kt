package com.example.rescueme

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class UnconsciousChecklistActivity : AppCompatActivity() {
    private lateinit var stepsContainer: LinearLayout
    private val CALL_PERMISSION_REQUEST_CODE = 1004

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_unconscious_checklist)

        stepsContainer = findViewById(R.id.unconsciousStepsContainer)
        setupSteps()
        
        // Back Button
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupSteps() {
        val steps = listOf(
            Step("Call Emergency Services", "Immediately call emergency services for professional medical assistance.", true),
            Step("Check for Responsiveness", "Gently tap or shake the person and shout loudly, 'Are you okay?'"),
            Step("Open the Airway", "If the person is unresponsive, carefully open their airway using the head-tilt chin-lift maneuver: place one hand on their forehead and gently tilt their head back, then use your other hand to lift their chin."),
            Step("Check for Breathing", "Once the airway is open, look, listen, and feel for breathing for about 10 seconds. Look for chest rise and fall, listen for breath sounds, and feel for air on your cheek."),
            Step("If Breathing Normally", "If the person is breathing normally, and there are no obvious injuries, carefully place them in the recovery position (on their side) to help keep their airway open."),
            Step("If Not Breathing or Only Gasping", "If the person is not breathing or only gasping, and you are trained in CPR, prepare to start chest compressions and rescue breaths immediately. Follow your CPR training."),
            Step("Do Not Give Food or Drink", "Do not give an unconscious person anything to eat or drink."),
            Step("Stay with the Person", "Stay with the person and continue to monitor their breathing and responsiveness until emergency help arrives."),
            Step("Provide Information to Responders", "When emergency medical responders arrive, provide them with a clear and concise account of what you observed, the person's condition, and any actions you took.")
        )

        addStepsToContainer(steps)
    }

    private fun addStepsToContainer(steps: List<Step>) {
        steps.forEach { step ->
            val cardView = LayoutInflater.from(this)
                .inflate(R.layout.item_unconscious_step, stepsContainer, false) as CardView

            val titleTextView = cardView.findViewById<TextView>(R.id.stepTitle)
            val descriptionTextView = cardView.findViewById<TextView>(R.id.stepDescription)
            val callButton = cardView.findViewById<Button>(R.id.callButton)

            titleTextView.text = step.title
            descriptionTextView.text = step.description

            if (step.hasCallButton) {
                callButton.visibility = Button.VISIBLE
                callButton.setOnClickListener {
                    handleEmergencyCall()
                }
            } else {
                callButton.visibility = Button.GONE
            }

            stepsContainer.addView(cardView)
        }
    }

    private fun handleEmergencyCall() {
        when {
            checkCallPermission() -> {
                makeEmergencyCall()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {
                Toast.makeText(this, "Call permission is required to make emergency calls", Toast.LENGTH_LONG).show()
                requestCallPermission()
            }
            else -> {
                requestCallPermission()
            }
        }
    }

    private fun checkCallPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCallPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CALL_PHONE),
            CALL_PERMISSION_REQUEST_CODE
        )
    }

    private fun makeEmergencyCall() {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALL_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeEmergencyCall()
                } else {
                    Toast.makeText(this, "Call permission is required to make emergency calls", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    data class Step(
        val title: String,
        val description: String,
        val hasCallButton: Boolean = false
    )
} 