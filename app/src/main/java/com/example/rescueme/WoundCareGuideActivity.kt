package com.example.rescueme

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class WoundCareGuideActivity : AppCompatActivity() {

    private lateinit var videoThumbnail: ImageView
    private lateinit var woundCareInstructionsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_wound_care_guide)

        // Initialize views
        videoThumbnail = findViewById(R.id.videoThumbnail)
        woundCareInstructionsContainer = findViewById(R.id.woundCareInstructionsContainer)

        // Set up video thumbnail click listener
        videoThumbnail.setOnClickListener {
            val videoUrl = "android.resource://${packageName}/${R.raw.wound_care}"
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
            "Wash your hands thoroughly with soap and water, if possible, or use hand sanitizer.",
            "If available, wear disposable gloves to prevent infection.",
            "Apply direct pressure to the wound using a clean cloth or bandage. Use the palm of your hand for large areas or your fingers for smaller wounds.",
            "Maintain firm, continuous pressure for several minutes (5-10 minutes) without lifting to allow blood clots to form.",
            "If blood soaks through the first dressing, apply another one on top. Do not remove the first dressing.",
            "Elevate the injured limb above the level of the heart, if possible, to help reduce bleeding.",
            "For severe bleeding that doesn't stop, call for emergency medical help immediately.",
            "Once bleeding is controlled, gently clean the wound with mild soap and cool, running water for several minutes to remove dirt and debris.",
            "Avoid using harsh soaps, antiseptics (like iodine or hydrogen peroxide) directly in the wound, as they can damage tissue. They can be used on the surrounding skin if needed.",
            "If there is debris that won't wash away, seek medical attention.",
            "If recommended by a healthcare professional or if the wound is minor, apply a thin layer of antibiotic ointment to help prevent infection.",
            "Cover the wound with a clean bandage or dressing to protect it from dirt and germs. Choose a bandage size appropriate for the wound.",
            "Change the dressing at least once a day, or more often if it becomes wet or dirty.",
            "Monitor the wound for signs of infection, such as increased pain, redness, swelling, warmth, pus, or fever. Seek medical attention if any of these signs develop."
        )

        instructions.forEachIndexed { index, instruction ->
            val cardView = createInstructionCard(index + 1, instruction)
            woundCareInstructionsContainer.addView(cardView)
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