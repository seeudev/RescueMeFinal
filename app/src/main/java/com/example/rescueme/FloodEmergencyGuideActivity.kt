package com.example.rescueme

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class FloodEmergencyGuideActivity : AppCompatActivity() {

    private lateinit var floodTipsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_flood_emergency_guide)

        // Initialize views
        floodTipsContainer = findViewById(R.id.floodTipsContainer)

        // Add tip cards
        addTipCards()
    }

    private fun addTipCards() {
        val tips = listOf(
            "Stay informed about flood warnings and evacuation orders through official channels.",
            "If advised to evacuate, do so immediately to higher ground.",
            "Never walk, swim, or drive through floodwaters. Even shallow water can be dangerous and fast-moving.",
            "If you are in a building, move to the highest level if possible.",
            "Turn off electricity at the main breaker if it is safe to do so to prevent electrical hazards.",
            "Do not touch electrical equipment if you are wet or standing in water.",
            "If you are trapped in a vehicle in rising water, abandon it and move to higher ground if you can do so safely.",
            "Be aware of potential hazards such as debris, fallen power lines, and contaminated water.",
            "Do not drink floodwater.",
            "After floodwaters recede, be cautious of structural damage and potential hazards.",
            "Follow guidance from emergency responders and local authorities."
        )

        tips.forEach { tip ->
            val cardView = createTipCard(tip)
            floodTipsContainer.addView(cardView)
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