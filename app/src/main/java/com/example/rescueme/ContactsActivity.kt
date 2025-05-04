package com.example.rescueme

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rescueme.utils.Contact
import com.example.rescueme.utils.ContactAdapter

class ContactsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactAdapter
    private lateinit var app: RescueMeApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_contact)

        app = RescueMeApp.getInstance()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.contactsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with click listener
        adapter = ContactAdapter(this, app.getContacts()) { contact ->
            val intent = Intent(this, ContactDetailActivity::class.java).apply {
                putExtra("contact_id", contact.id)
                putExtra("contact_name", contact.name)
                putExtra("contact_phone", contact.phoneNumber)
                putExtra("contact_relation", contact.relation)
                putExtra("contact_image", contact.profileImageResourceId)
                putExtra("is_emergency_service", contact.isEmergencyService)
                putExtra("service_type", contact.serviceType)
            }
            startActivityForResult(intent, CONTACT_DETAIL_REQUEST)
        }
        recyclerView.adapter = adapter

        // Set up add contact button
        findViewById<TextView>(R.id.textViewAdd).setOnClickListener {
            showAddContactDialog()
        }

        // Set up navigation bar
        setupNavigationBar()
    }

    private fun showAddContactDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add New Contact")

        val view = layoutInflater.inflate(R.layout.dialog_add_contact, null)
        builder.setView(view)

        val nameEditText = view.findViewById<EditText>(R.id.editTextName)
        val phoneEditText = view.findViewById<EditText>(R.id.editTextPhone)
        val relationEditText = view.findViewById<EditText>(R.id.editTextRelation)

        builder.setPositiveButton("Add") { dialog, _ ->
            val name = nameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val relation = relationEditText.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                val newContact = Contact(
                    id = System.currentTimeMillis().toString(),
                    name = name,
                    phoneNumber = phone,
                    relation = relation,
                    profileImageResourceId = R.drawable.profile_image_1,
                    isEmergencyService = false,
                    serviceType = ""
                )
                app.addContact(newContact)
                adapter.updateContacts(app.getContacts())
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun setupNavigationBar() {
        findViewById<View>(R.id.homeButton).setOnClickListener {
            startActivity(Intent(this, LandingActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.profileButton).setOnClickListener {
            startActivity(Intent(this, ProfilePageActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.notificationsButton).setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.emergencyButton).setOnClickListener {
            startActivity(Intent(this, EmergencyActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACT_DETAIL_REQUEST && resultCode == RESULT_OK) {
            data?.let { intent ->
                val contactId = intent.getStringExtra("contact_id") ?: return
                val action = intent.getStringExtra("action") ?: return

                if (action == "delete") {
                    app.removeContact(contactId)
                    adapter.updateContacts(app.getContacts())
                }
            }
        }
    }

    companion object {
        private const val CONTACT_DETAIL_REQUEST = 1
    }
}