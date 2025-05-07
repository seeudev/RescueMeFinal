package com.example.rescueme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class ChokingGuideActivity : AppCompatActivity() {

    private lateinit var videoThumbnail: ImageView
    private lateinit var chokingInstructionsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_choking_guide)

        // Initialize views
        videoThumbnail = findViewById(R.id.videoThumbnail)
        chokingInstructionsContainer = findViewById(R.id.chokingInstructionsContainer)

        // Set up video thumbnail click listener
        videoThumbnail.setOnClickListener {
            // TODO: Replace with actual video URL
            val videoUrl = "android.resource://${packageName}/${R.raw.breathing_treatment}"
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
            "If the person can cough forcefully, encourage them to keep coughing.",
            "Tell someone to call for emergency help immediately (or call yourself if you are alone).",
            "Stand behind the person.",
            "Wrap your arms around their waist.",
            "Make a fist with one hand and place it slightly above their navel (belly button), thumb side in.",
            "Grasp your fist with your other hand.",
            "Give a quick, upward thrust into their abdomen.",
            "Perform 5 abdominal thrusts.",
            "Alternate between 5 abdominal thrusts and 5 back blows (given with the heel of your hand between the person's shoulder blades).",
            "The object is forced out and the person can breathe, cough, or speak, OR emergency help arrives.",
            "If the person becomes unconscious: Carefully lower the person to the ground. Begin CPR if you are trained. Emergency services will provide further instructions."
        )

        instructions.forEachIndexed { index, instruction ->
            val cardView = createInstructionCard(index + 1, instruction)
            chokingInstructionsContainer.addView(cardView)
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