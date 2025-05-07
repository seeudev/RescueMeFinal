package com.example.rescueme

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class EarthquakeEmergencyGuideActivity : AppCompatActivity() {

    private lateinit var earthquakeTipsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_earthquake_emergency_guide)

        // Initialize views
        earthquakeTipsContainer = findViewById(R.id.earthquakeTipsContainer)

        // Add tip cards
        addTipCards()
    }

    private fun addTipCards() {
        val tips = listOf(
            "DROP to your hands and knees.",
            "COVER your head and neck with one arm and hand.",
            "HOLD ON to any sturdy furniture until the shaking stops. If there isn't a sturdy desk or table nearby, move against an interior wall and protect your head and neck.",
            "Stay away from windows, glass, and unsecured objects that could fall.",
            "If you are outdoors, find a clear spot away from buildings, trees, streetlights, and power lines. Drop to the ground and stay there until the shaking stops.",
            "If you are in a vehicle, pull over to a clear location and stop. Stay inside the vehicle until the shaking stops.",
            "Do not run outside or try to move around during strong shaking.",
            "After the shaking stops, check yourself and others for injuries.",
            "If you are in a damaged building, evacuate carefully once it is safe to do so. Be aware of aftershocks.",
            "Avoid downed power lines and other hazards.",
            "If you are trapped, send a text or call for help, bang on a pipe or wall so rescuers can locate you, and cover your mouth with a cloth to avoid inhaling dust."
        )

        tips.forEach { tip ->
            val cardView = createTipCard(tip)
            earthquakeTipsContainer.addView(cardView)
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