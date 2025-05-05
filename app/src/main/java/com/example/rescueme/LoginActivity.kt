package com.example.rescueme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)

        try {
            val etEmail = findViewById<EditText>(R.id.loginEmail)
            val etPassword = findViewById<EditText>(R.id.loginPassword)
            val buttonLogin = findViewById<Button>(R.id.button_login)
            val textSignUp = findViewById<TextView>(R.id.textViewSignup)

            // Initialize Firebase Database reference
            databaseReference = FirebaseDatabase.getInstance().getReference("users")

            intent?.let {
                it.getStringExtra("email")?.let { email ->
                    etEmail.setText(email)
                }
                it.getStringExtra("password")?.let { password ->
                    etPassword.setText(password)
                }
            }

            textSignUp.setOnClickListener {
                Log.e("This is CSIT284", "Sign up TextView is clicked!")
                Toast.makeText(this, "The sign up text is clicked!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            buttonLogin.setOnClickListener {
                val enteredEmail = etEmail.text.toString().trim()
                val enteredPassword = etPassword.text.toString().trim()

                if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(this, "Email and password cannot be empty.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                // Check users in Firebase Realtime Database by email
                databaseReference.orderByChild("email").equalTo(enteredEmail)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                var foundUser = false
                                var username = ""
                                for (userSnapshot in snapshot.children) {
                                    val userPassword = userSnapshot.child("password").getValue(String::class.java)
                                    if (userPassword == enteredPassword) {
                                        foundUser = true
                                        username = userSnapshot.child("username").getValue(String::class.java) ?: ""
                                        break
                                    }
                                }
                                if (foundUser) {
                                    // Save user data to RescueMeApp
                                    val app = RescueMeApp.getInstance()
                                    app.saveUserData(username, enteredEmail, "")
                                    
                                    // Login success
                                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@LoginActivity, LandingActivity::class.java).apply {
                                        putExtra("email", enteredEmail)
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // Wrong password
                                    Toast.makeText(this@LoginActivity, "Invalid password.", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                // Email not found
                                Toast.makeText(this@LoginActivity, "Email not found.", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@LoginActivity, "Database error: ${error.message}", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error in onCreate: ${e.message}")
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_LONG).show()
        }
    }
}
