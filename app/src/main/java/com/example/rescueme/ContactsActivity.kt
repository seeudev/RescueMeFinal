package com.example.rescueme

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rescueme.utils.Contact
import com.example.rescueme.utils.ContactAdapter
import kotlin.random.Random

class ContactsActivity : Activity() {

    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private val contactList = mutableListOf<Contact>()
    private lateinit var addContactTextView: TextView
    private val random = java.util.Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_contact) // Your main layout file

        recyclerViewContacts = findViewById(R.id.recyclerViewContacts)
        recyclerViewContacts.layoutManager = LinearLayoutManager(this)

        addContactTextView = findViewById(R.id.textViewAdd)
        addContactTextView.setOnClickListener {
            addNewContact()
        }

        // Initial Sample data
        contactList.add(Contact("April John Sultan", "Brother", R.drawable.profile_image_1))
        contactAdapter = ContactAdapter(this, contactList)
        recyclerViewContacts.adapter = contactAdapter
    }

    private fun addNewContact() {
        // Create a new Contact object with some slightly randomized data
        val names = listOf("Charlie Brown", "Lucy Van Pelt", "Linus van Pelt", "Schroeder", "Peppermint Patty")
        val relations = listOf("Friend", "Sister", "Cousin", "Colleague", "Acquaintance")
        val imageResources = listOf(
            R.drawable.profile_image_1,
            //R.drawable.profile_image_2,
          //  R.drawable.profile_image_3,
           // R.drawable.profile_image_4
        )

        val randomName = names[random.nextInt(names.size)]
        val randomRelation = relations[random.nextInt(relations.size)]
        val randomImage = imageResources[random.nextInt(imageResources.size)]

        val newContact = Contact(randomName, randomRelation, randomImage)
        contactList.add(newContact)

        // Notify the adapter that an item has been inserted at the end of the list
        contactAdapter.notifyItemInserted(contactList.size - 1)

        // Optionally, scroll to the newly added item
        recyclerViewContacts.scrollToPosition(contactList.size - 1)
    }
}