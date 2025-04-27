package com.example.rescueme

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : Activity() {

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_signup)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val usernameEditText = findViewById<EditText>(R.id.et_username)
        val passwordEditText = findViewById<EditText>(R.id.et_password)
        val emailEditText = findViewById<EditText>(R.id.et_email)
        val confirmPasswordEditText = findViewById<EditText>(R.id.et_confirm_password)
        val buttonSignup = findViewById<Button>(R.id.button_signup)
        val textViewLogin = findViewById<TextView>(R.id.textViewLogin)

        buttonSignup.setOnClickListener {
            val enteredUsername = usernameEditText.text.toString().trim()
            val enteredPassword = passwordEditText.text.toString().trim()
            val enteredEmail = emailEditText.text.toString().trim()
            val enteredConfirmPassword = confirmPasswordEditText.text.toString().trim()

            if (enteredUsername.isEmpty() ||
                enteredPassword.isEmpty() ||
                enteredEmail.isEmpty() ||
                enteredConfirmPassword.isEmpty()
            ) {
                Toast.makeText(this, "Fill out all fields completely.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (enteredPassword != enteredConfirmPassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                // Instead of Firebase Auth, generate a random ID
                val userId = database.reference.child("users").push().key
                if (userId != null) {
                    val userReference = database.getReference("users").child(userId)
                    val userMap = mapOf(
                        "username" to enteredUsername,
                        "email" to enteredEmail,
                        "password" to enteredPassword // (Important: real app should hash this)
                    )
                    userReference.setValue(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "User Registered!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }


        textViewLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
