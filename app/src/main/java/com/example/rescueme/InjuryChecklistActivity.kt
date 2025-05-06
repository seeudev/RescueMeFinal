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

class InjuryChecklistActivity : AppCompatActivity() {
    private lateinit var stepsContainer: LinearLayout
    private val CALL_PERMISSION_REQUEST_CODE = 1003

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_injury_checklist)

        stepsContainer = findViewById(R.id.stepsContainer)

        findViewById<CardView>(R.id.cardIAmInjured).setOnClickListener {
            showSelfInjurySteps()
        }

        findViewById<CardView>(R.id.cardSomeoneIsInjured).setOnClickListener {
            showOtherInjurySteps()
        }
        //Back Button
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener() {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showSelfInjurySteps() {
        stepsContainer.removeAllViews()
        
        val steps = listOf(
            Step("Call for help", "Call Emergency Services", true),
            Step("Assess surroundings", "Try to identify any immediate dangers around you and move away if possible and safe."),
            Step("Check for bleeding", "If you are bleeding, try to apply direct pressure to the wound using a clean cloth or your hand."),
            Step("Safe position", "If possible, try to sit or lie down in a comfortable and safe position to prevent further injury."),
            Step("Stay calm", "Try to remain as calm as possible and wait for assistance to arrive. Provide clear information to the emergency responders when they arrive.")
        )

        addStepsToContainer(steps)
    }

    private fun showOtherInjurySteps() {
        stepsContainer.removeAllViews()
        
        val steps = listOf(
            Step("Your safety", "Before approaching, make sure the area is safe for you and the injured person."),
            Step("Responsiveness", "Gently tap or shake the person and ask loudly, 'Are you okay?'"),
            Step("Call for help", "Call Emergency Services", true),
            Step("Breathing", "Look, listen, and feel for breathing for about 10 seconds."),
            Step("CPR (if trained)", "If not breathing and trained, begin CPR."),
            Step("Severe bleeding", "Quickly scan for severe bleeding and apply direct pressure."),
            Step("Do not move", "Avoid moving the person unless necessary."),
            Step("Keep warm", "If possible, cover the person to prevent shock and keep them warm."),
            Step("Reassure", "Talk to the injured person calmly and reassure them."),
            Step("Inform responders", "Provide clear information to emergency services when they arrive.")
        )

        addStepsToContainer(steps)
    }

    private fun addStepsToContainer(steps: List<Step>) {
        steps.forEach { step ->
            val cardView = LayoutInflater.from(this)
                .inflate(R.layout.item_injury_step, stepsContainer, false) as CardView

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
                // Show explanation to the user
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