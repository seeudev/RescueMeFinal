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

class CantBreatheChecklistActivity : AppCompatActivity() {
    private lateinit var stepsContainer: LinearLayout
    private val CALL_PERMISSION_REQUEST_CODE = 1004

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_cant_breathe_checklist)

        stepsContainer = findViewById(R.id.cantBreatheStepsContainer)
        
        // Back Button
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }

        showBreathingEmergencySteps()
    }

    private fun showBreathingEmergencySteps() {
        stepsContainer.removeAllViews()
        
        val steps = listOf(
            Step("Call for help", "Call Emergency Services immediately", true),
            Step("Assess responsiveness", "Check if the person is responsive. Gently tap or shake them and ask loudly, 'Are you okay?'"),
            Step("Check for obstructions", "Quickly look in the person's mouth for any obvious obstructions like food, a foreign object, or the tongue blocking the airway. If you see something, try to remove it carefully (only if easily accessible)."),
            Step("Position the person", "If the person is unresponsive and not breathing normally, lay them flat on their back on a firm surface. If they are responsive but struggling to breathe, help them sit up in a comfortable position that eases their breathing."),
            Step("Prepare for CPR if needed", "If the person is unresponsive and not breathing or only gasping, and you are trained in CPR, prepare to start chest compressions and rescue breaths. Follow your CPR training."),
            Step("Inform responders", "When emergency services arrive, provide them with clear and concise information about what you observed and any actions you took."),
            Step("Stay with the person", "Stay with the person until emergency help arrives and takes over.")
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