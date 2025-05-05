package com.example.rescueme

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.rescueme.utils.Contact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RescueMeApp : Application() {
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "RescueMePrefs"
        private const val KEY_CONTACTS = "contacts"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_USER_ID = "user_id"
        
        private var instance: RescueMeApp? = null

        fun getInstance(): RescueMeApp {
            return instance ?: throw IllegalStateException("Application not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        // Initialize with default emergency services if contacts list is empty
        if (getContacts().isEmpty()) {
            saveContacts(Contact.DEFAULT_EMERGENCY_SERVICES.toMutableList())
        }
    }

    // Contact List Management
    fun getContacts(): MutableList<Contact> {
        val json = sharedPreferences.getString(KEY_CONTACTS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Contact>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun saveContacts(contacts: MutableList<Contact>) {
        val json = gson.toJson(contacts)
        sharedPreferences.edit().putString(KEY_CONTACTS, json).apply()
    }

    fun addContact(contact: Contact) {
        val contacts = getContacts()
        contacts.add(contact)
        saveContacts(contacts)
    }

    fun removeContact(contactId: String) {
        val contacts = getContacts()
        contacts.removeIf { it.id == contactId }
        saveContacts(contacts)
    }

    fun updateContact(updatedContact: Contact) {
        val contacts = getContacts()
        val index = contacts.indexOfFirst { it.id == updatedContact.id }
        if (index != -1) {
            contacts[index] = updatedContact
            saveContacts(contacts)
        }
    }

    // User Data Management
    fun saveUserData(name: String, email: String, phone: String, userId: String = "") {
        sharedPreferences.edit().apply {
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_PHONE, phone)
            if (userId.isNotEmpty()) {
                putString(KEY_USER_ID, userId)
            }
            apply()
        }
    }

    fun getUserName(): String = sharedPreferences.getString(KEY_USER_NAME, "") ?: ""
    fun getUserEmail(): String = sharedPreferences.getString(KEY_USER_EMAIL, "") ?: ""
    fun getUserPhone(): String = sharedPreferences.getString(KEY_USER_PHONE, "") ?: ""
    fun getUserId(): String = sharedPreferences.getString(KEY_USER_ID, "") ?: ""
} 