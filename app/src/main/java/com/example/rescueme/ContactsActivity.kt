package com.example.rescueme

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rescueme.utils.Contact
import com.example.rescueme.utils.ContactAdapter

class ContactsActivity : Activity() {

    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private val contactList = mutableListOf<Contact>()
    private lateinit var addContactTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_contact)

        recyclerViewContacts = findViewById(R.id.recyclerViewContacts)
        recyclerViewContacts.layoutManager = LinearLayoutManager(this)

        addContactTextView = findViewById(R.id.textViewAdd)
        addContactTextView.setOnClickListener {
            showAddContactDialog()
        }

        // Initial Sample data
        contactList.add(Contact("April John Sultan", "Brother", R.drawable.profile_image_1))
        contactAdapter = ContactAdapter(this, contactList)
        recyclerViewContacts.adapter = contactAdapter
    }

    private fun showAddContactDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add New Contact")

        // Inflate a custom layout for the dialog
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null)
        builder.setView(view)

        val nameEditText = view.findViewById<EditText>(R.id.editTextName)
        val relationEditText = view.findViewById<EditText>(R.id.editTextRelation)

        // Set the positive button action
        builder.setPositiveButton("Save") { dialog, _ ->
            val name = nameEditText.text.toString().trim()
            val relation = relationEditText.text.toString().trim()

            if (name.isNotEmpty()) {
                val newContact = Contact(name, relation, R.drawable.profile_image_1) // You might want to add a way to select a different image
                contactList.add(newContact)
                contactAdapter.notifyItemInserted(contactList.size - 1)
                recyclerViewContacts.scrollToPosition(contactList.size - 1)
                Toast.makeText(this, "Contact saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        // Set the negative button action
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}