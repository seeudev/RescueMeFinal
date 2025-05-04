package com.example.rescueme.utils

import com.example.rescueme.R

data class Contact(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val relation: String = "",
    val profileImageResourceId: Int = R.drawable.profile_image_1,
    val isEmergencyService: Boolean = false,
    val serviceType: String = ""
) {
    companion object {
        // Default emergency services for Cebu City
        val DEFAULT_EMERGENCY_SERVICES = listOf(
            Contact(
                id = "1",
                name = "Cebu City Police",
                phoneNumber = "911",
                relation = "Police",
                profileImageResourceId = R.drawable.ic_police,
                isEmergencyService = true,
                serviceType = "Police"
            ),
            Contact(
                id = "2",
                name = "Cebu City Fire Department",
                phoneNumber = "911",
                relation = "Fire Department",
                profileImageResourceId = R.drawable.ic_fire,
                isEmergencyService = true,
                serviceType = "Fire"
            ),
            Contact(
                id = "3",
                name = "Cebu City Emergency Medical Services",
                phoneNumber = "911",
                relation = "Ambulance",
                profileImageResourceId = R.drawable.ic_ambulance,
                isEmergencyService = true,
                serviceType = "Ambulance"
            ),
            Contact(
                id = "4",
                name = "Cebu City Disaster Risk Reduction and Management Office",
                phoneNumber = "911",
                relation = "Disaster Management",
                profileImageResourceId = R.drawable.ic_disaster,
                isEmergencyService = true,
                serviceType = "Disaster"
            )
        )
    }
} 