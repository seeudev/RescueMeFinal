package com.example.rescueme

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PersonalDataActivity : AppCompatActivity() {
    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var updateButton: Button
    private lateinit var cancelButton: Button
    private lateinit var backButton: ImageView

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_personaldata)

        // Initialize views
        initializeViews()
        
        // Load current user data
        loadUserData()
        
        // Set up click listeners
        setupClickListeners()
    }

    private fun initializeViews() {
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        phoneInput = findViewById(R.id.phoneInput)
        passwordInput = findViewById(R.id.passwordInput)
        updateButton = findViewById(R.id.updateButton)
        cancelButton = findViewById(R.id.cancelButton)
        backButton = findViewById(R.id.backButton)
    }

    private fun loadUserData() {
        val app = RescueMeApp.getInstance()
        nameInput.setText(app.getUserName())
        emailInput.setText(app.getUserEmail())
        phoneInput.setText(app.getUserPhone())
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        updateButton.setOnClickListener {
            updateUserData()
        }
    }

    private fun updateUserData() {
        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading state
        updateButton.isEnabled = false
        updateButton.text = "Updating..."

        val user = auth.currentUser
        if (user != null) {
            // Update email if changed
            if (email != user.email) {
                user.updateEmail(email)
                    .addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            updateUserDataInFirestore(name, email, phone, password)
                        } else {
                            handleUpdateError("Failed to update email: ${emailTask.exception?.message}")
                        }
                    }
            } else {
                updateUserDataInFirestore(name, email, phone, password)
            }
        } else {
            handleUpdateError("User not logged in")
        }
    }

    private fun updateUserDataInFirestore(name: String, email: String, phone: String, password: String) {
        val user = auth.currentUser
        if (user != null) {
            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "phone" to phone
            )

            // Update password if provided
            if (password.isNotEmpty()) {
                user.updatePassword(password)
                    .addOnCompleteListener { passwordTask ->
                        if (!passwordTask.isSuccessful) {
                            handleUpdateError("Failed to update password: ${passwordTask.exception?.message}")
                            return@addOnCompleteListener
                        }
                    }
            }

            // Update Firestore
            db.collection("users").document(user.uid)
                .update(userData as Map<String, Any>)
                .addOnSuccessListener {
                    // Update local app data
                    val app = RescueMeApp.getInstance()
                    app.saveUserData(name, email, phone)
                    
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    handleUpdateError("Failed to update profile: ${e.message}")
                }
        }
    }

    private fun handleUpdateError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        updateButton.isEnabled = true
        updateButton.text = "Update"
    }
} 