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

class AccidentChecklistActivity : AppCompatActivity() {
    private lateinit var stepsContainer: LinearLayout
    private val CALL_PERMISSION_REQUEST_CODE = 1004

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_accident_checklist)

        stepsContainer = findViewById(R.id.accidentStepsContainer)
        showAccidentSteps()

        //BackButton
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener() {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showAccidentSteps() {
        stepsContainer.removeAllViews()
        
        val steps = listOf(
            Step(
                "Ensure Safety",
                "Ensure your safety and the safety of others involved. If possible, move vehicles to the side of the road and turn on hazard lights."
            ),
            Step(
                "Assess the Situation",
                "Check for injuries. Call emergency services immediately if anyone is injured or if you are unsure of the extent of the damage.",
                true
            ),
            Step(
                "Do Not Move Injured Persons",
                "Do not move injured persons unless they are in immediate danger (e.g., from fire or traffic)."
            ),
            Step(
                "Administer First Aid",
                "If you are trained in first aid, provide assistance to injured persons until emergency services arrive. Prioritize controlling bleeding and maintaining airway."
            ),
            Step(
                "Gather Information",
                "Exchange information with other drivers involved, including names, contact information, insurance details, and license plate numbers. If possible, take photos of the damage and the accident scene."
            ),
            Step(
                "Contact Insurance",
                "Contact your insurance company as soon as possible to report the accident and begin the claims process."
            ),
            Step(
                "Cooperate with Authorities",
                "Cooperate fully with law enforcement officers when they arrive. Provide them with accurate information about the accident."
            ),
            Step(
                "Document the Scene",
                "If possible, document the scene with photos or videos. This documentation can be helpful for insurance claims and legal purposes."
            )
        )

        addStepsToContainer(steps)
    }

    private fun addStepsToContainer(steps: List<Step>) {
        steps.forEach { step ->
            val cardView = LayoutInflater.from(this)
                .inflate(R.layout.item_accident_step, stepsContainer, false) as CardView

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