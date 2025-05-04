package com.example.rescueme

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rescueme.utils.Contact

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var contactName: TextView
    private lateinit var contactPhone: TextView
    private lateinit var contactRelation: TextView
    private lateinit var contactImage: ImageView
    private lateinit var serviceType: TextView
    private lateinit var callButton: Button
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button

    private var contactId: String = ""
    private var isEmergencyService: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        // Initialize views
        contactName = findViewById(R.id.contactName)
        contactPhone = findViewById(R.id.contactPhone)
        contactRelation = findViewById(R.id.contactRelation)
        contactImage = findViewById(R.id.contactImage)
        serviceType = findViewById(R.id.serviceType)
        callButton = findViewById(R.id.callButton)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Get contact details from intent
        intent.extras?.let { bundle ->
            contactId = bundle.getString("contact_id", "")
            contactName.text = bundle.getString("contact_name", "")
            contactPhone.text = bundle.getString("contact_phone", "")
            contactRelation.text = bundle.getString("contact_relation", "")
            contactImage.setImageResource(bundle.getInt("contact_image", R.drawable.profile_image_1))
            isEmergencyService = bundle.getBoolean("is_emergency_service", false)
            
            if (isEmergencyService) {
                serviceType.visibility = TextView.VISIBLE
                serviceType.text = bundle.getString("service_type", "")
                deleteButton.visibility = Button.GONE
            } else {
                serviceType.visibility = TextView.GONE
            }
        }

        // Set up click listeners
        callButton.setOnClickListener {
            val phoneNumber = contactPhone.text.toString()
            if (phoneNumber.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                startActivity(intent)
            }
        }

        editButton.setOnClickListener {
            if (!isEmergencyService) {
                showEditDialog()
            } else {
                Toast.makeText(this, "Emergency services cannot be edited", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            if (!isEmergencyService) {
                showDeleteConfirmationDialog()
            } else {
                Toast.makeText(this, "Emergency services cannot be deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Contact")

        val view = layoutInflater.inflate(R.layout.dialog_add_contact, null)
        builder.setView(view)

        val nameEditText = view.findViewById<TextView>(R.id.editTextName)
        val phoneEditText = view.findViewById<TextView>(R.id.editTextPhone)
        val relationEditText = view.findViewById<TextView>(R.id.editTextRelation)

        // Pre-fill the fields
        nameEditText.text = contactName.text
        phoneEditText.text = contactPhone.text
        relationEditText.text = contactRelation.text

        builder.setPositiveButton("Save") { dialog, _ ->
            val newName = nameEditText.text.toString().trim()
            val newPhone = phoneEditText.text.toString().trim()
            val newRelation = relationEditText.text.toString().trim()

            if (newName.isNotEmpty() && newPhone.isNotEmpty()) {
                contactName.text = newName
                contactPhone.text = newPhone
                contactRelation.text = newRelation
                Toast.makeText(this, "Contact updated!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Name and phone number cannot be empty", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Contact")
            .setMessage("Are you sure you want to delete this contact?")
            .setPositiveButton("Delete") { dialog, _ ->
                // Return to contacts list with delete result
                setResult(RESULT_OK, Intent().apply {
                    putExtra("contact_id", contactId)
                    putExtra("action", "delete")
                })
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
} 