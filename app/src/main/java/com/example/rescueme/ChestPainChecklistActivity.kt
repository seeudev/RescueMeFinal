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

class ChestPainChecklistActivity : AppCompatActivity() {
    private lateinit var stepsContainer: LinearLayout
    private val CALL_PERMISSION_REQUEST_CODE = 1004

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_chest_pain_checklist)

        stepsContainer = findViewById(R.id.chestPainStepsContainer)

        // Back Button
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        showChestPainSteps()
    }

    private fun showChestPainSteps() {
        stepsContainer.removeAllViews()
        
        val steps = listOf(
            Step("Call Emergency Medical Help Immediately", 
                "This is the most important step. Do not delay in calling emergency services.", true),
            Step("Do Not Delay Seeking Help", 
                "Chest pain can be a sign of a serious medical condition. Every minute counts."),
            Step("Describe Your Symptoms Clearly", 
                "When you call for help, clearly and calmly describe your symptoms to the dispatcher, including the location, intensity, and type of pain (e.g., sharp, dull, pressure)."),
            Step("Follow Dispatcher's Instructions", 
                "Carefully follow any instructions provided by the emergency dispatcher."),
            Step("Try to Rest and Stay Still", 
                "While waiting for help to arrive, try to rest in a comfortable position. Avoid unnecessary movement or exertion."),
            Step("If You Have Prescribed Medication", 
                "If you have been prescribed medication for chest pain (like nitroglycerin), take it as directed by your doctor while waiting for help."),
            Step("Inform Others Around You", 
                "If there are other people nearby, inform them about your chest pain and that you have called for help."),
            Step("Provide Medical History to Responders", 
                "When emergency medical responders arrive, provide them with your medical history, including any known heart conditions, medications you are taking, and any allergies.")
        )

        addStepsToContainer(steps)
    }

    private fun addStepsToContainer(steps: List<Step>) {
        steps.forEach { step ->
            val cardView = LayoutInflater.from(this)
                .inflate(R.layout.item_chest_pain_step, stepsContainer, false) as CardView

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