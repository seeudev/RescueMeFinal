package com.example.rescueme

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class FireEmergencyGuideActivity : AppCompatActivity() {

    private lateinit var fireTipsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_fire_emergency_guide)

        // Initialize views
        fireTipsContainer = findViewById(R.id.fireTipsContainer)

        // Add tip cards
        addTipCards()
    }

    private fun addTipCards() {
        val tips = listOf(
            "Stay calm and act quickly.",
            "If there is a small fire and you know how to extinguish it safely, do so.",
            "Alert others in the building by shouting 'Fire!' and activating fire alarms if available.",
            "If the fire is large or spreading rapidly, evacuate immediately.",
            "Feel doors with the back of your hand before opening them. If hot, do not open.",
            "If there is smoke, stay low to the ground and crawl.",
            "Use stairs if possible; do not use elevators during a fire.",
            "If you cannot escape, close all doors and seal any cracks with towels or clothing.",
            "Go to a window and signal for help.",
            "Once you are out, stay out. Do not go back inside for anything.",
            "Call emergency services (your local emergency number) from a safe location.",
            "Provide the dispatcher with clear information about the fire's location and any trapped individuals."
        )

        tips.forEach { tip ->
            val cardView = createTipCard(tip)
            fireTipsContainer.addView(cardView)
        }
    }

    private fun createTipCard(tip: String): CardView {
        return CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            radius = 8f
            cardElevation = 4f
            setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))

            val contentLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(16, 16, 16, 16)
            }

            val tipView = TextView(context).apply {
                text = tip
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, R.color.black))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            contentLayout.addView(tipView)
            addView(contentLayout)
        }
    }
} 