package com.example.rescueme.utils
data class User(val username: String, val password: String, val email: String)

// Simulate a simple in-memory user storage
object UserDatabase {
    val users = mutableListOf<User>()
}
