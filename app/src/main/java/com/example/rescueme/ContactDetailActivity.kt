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
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var contactName: TextView
    private lateinit var contactPhone: TextView
    private lateinit var contactRelation: TextView
    private lateinit var contactImage: ImageView
    private lateinit var serviceType: TextView
    private lateinit var callButton: Button
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var app: RescueMeApp

    private var contactId: String = ""
    private var isEmergencyService: Boolean = false
    private var currentImageResourceId: Int = 0

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val IMAGE_PICKER_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        app = RescueMeApp.getInstance()

        // Initialize views
        contactName = findViewById(R.id.contactName)
        contactPhone = findViewById(R.id.contactPhone)
        contactRelation = findViewById(R.id.contactRelation)
        contactImage = findViewById(R.id.contactImage)
        serviceType = findViewById(R.id.serviceType)
        callButton = findViewById(R.id.callButton)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Set up back button
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        // Get contact details from intent
        intent.extras?.let { bundle ->
            contactId = bundle.getString("contact_id", "")
            contactName.text = bundle.getString("contact_name", "")
            contactPhone.text = bundle.getString("contact_phone", "")
            contactRelation.text = bundle.getString("contact_relation", "")
            currentImageResourceId = bundle.getInt("contact_image", R.drawable.circleavatar)
            contactImage.setImageResource(currentImageResourceId)
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
        contactImage.setOnClickListener {
            if (!isEmergencyService) {
                checkPermissionAndOpenImagePicker()
            } else {
                Toast.makeText(this, "Emergency services cannot be edited", Toast.LENGTH_SHORT).show()
            }
        }

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

    private fun checkPermissionAndOpenImagePicker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        } else {
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                Toast.makeText(this, "Permission denied to access images", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                // For now, we'll just use a default image resource since we can't store actual images
                // In a real app, you would save the image to internal storage and store its path
                val newImageResourceId = R.drawable.circleavatar
                contactImage.setImageResource(newImageResourceId)
                currentImageResourceId = newImageResourceId
                
                // Update the contact in the app's data
                val contacts = app.getContacts()
                val contactIndex = contacts.indexOfFirst { it.id == contactId }
                if (contactIndex != -1) {
                    val updatedContact = contacts[contactIndex].copy(
                        profileImageResourceId = newImageResourceId
                    )
                    app.updateContact(updatedContact)
                    Toast.makeText(this, "Contact image updated!", Toast.LENGTH_SHORT).show()
                }
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
                // Update the contact in the app's data
                val contacts = app.getContacts()
                val contactIndex = contacts.indexOfFirst { it.id == contactId }
                if (contactIndex != -1) {
                    val updatedContact = contacts[contactIndex].copy(
                        name = newName,
                        phoneNumber = newPhone,
                        relation = newRelation,
                        profileImageResourceId = currentImageResourceId
                    )
                    app.updateContact(updatedContact)
                    
                    // Update UI
                    contactName.text = newName
                    contactPhone.text = newPhone
                    contactRelation.text = newRelation
                    Toast.makeText(this, "Contact updated!", Toast.LENGTH_SHORT).show()
                }
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