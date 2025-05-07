package com.example.rescueme

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class CprGuideActivity : AppCompatActivity() {

    private lateinit var videoThumbnail: ImageView
    private lateinit var cprInstructionsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_cpr_guide)

        // Initialize views
        videoThumbnail = findViewById(R.id.videoThumbnail)
        cprInstructionsContainer = findViewById(R.id.cprInstructionsContainer)

        // Set up video thumbnail click listener
        videoThumbnail.setOnClickListener {
            val videoUrl = "android.resource://${packageName}/${R.raw.cpr_adult}"
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra("videoUrl", videoUrl)
            }
            startActivity(intent)
        }

        // Add instruction cards
        addInstructionCards()
    }

    private fun addInstructionCards() {
        val instructions = listOf(
            "Tap or shake the person and shout, 'Are you okay?'",
            "If the person doesn't respond, immediately call emergency services (your local emergency number). If someone else is nearby, tell them to call.",
            "Lay the person on their back on a firm, flat surface.",
            "Use the head-tilt chin-lift maneuver: place one hand on the forehead and gently tilt the head back, then use your other hand to lift the chin.",
            "Look, listen, and feel for normal breathing for no more than 10 seconds. Look for chest rise and fall, listen for breath sounds, and feel for air on their cheek.",
            "If the person isn't breathing or is only gasping, begin chest compressions.",
            "Place the heel of one hand in the center of the person's chest, on the lower half of the breastbone.",
            "Place your other hand directly on top of the first hand and interlock your fingers.",
            "Position your body directly over the person's chest and push straight down at a rate of 100 to 120 compressions per minute and a depth of about 2 inches (5 cm).",
            "Allow the chest to recoil completely after each compression.",
            "After 30 chest compressions, give two rescue breaths. Open the airway again using the head-tilt chin-lift.",
            "Pinch the person's nose shut and make a complete seal over their mouth with yours. Give one breath lasting about one second and watch for the chest to rise.",
            "Give a second breath.",
            "Continue cycles of 30 chest compressions and 2 rescue breaths until the person shows signs of life, emergency help arrives and takes over, or you become too exhausted to continue.",
            "If you are not trained in rescue breaths, continue chest compressions without stopping until emergency help arrives."
        )

        instructions.forEachIndexed { index, instruction ->
            val cardView = createInstructionCard(index + 1, instruction)
            cprInstructionsContainer.addView(cardView)
        }
    }

    private fun createInstructionCard(stepNumber: Int, instruction: String): CardView {
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

            val stepNumberView = TextView(context).apply {
                text = "$stepNumber."
                textSize = 18f
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setPadding(0, 0, 16, 0)
            }

            val instructionView = TextView(context).apply {
                text = instruction
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, R.color.black))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            contentLayout.addView(stepNumberView)
            contentLayout.addView(instructionView)
            addView(contentLayout)
        }
    }
} 