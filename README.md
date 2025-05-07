# RescueMe - Emergency Response Mobile Application

## Overview
RescueMe is a comprehensive emergency response mobile application designed to provide quick access to emergency services, medical guidance, and emergency contacts. The app is built using Kotlin and follows modern Android development practices.

## Key Features

### 1. Authentication System
- **LoginActivity**: Handles user authentication using Firebase Authentication
  - Email and password-based authentication
  - Secure session management
  - Automatic redirection to initial setup for new users
  - Error handling and user feedback

### 2. Emergency Response Features
- **Panic Button**
  - Quick access emergency alert system
  - 2-second hold confirmation to prevent accidental triggers
  - Location sharing with emergency contacts
  - Confirmation dialog for alert verification

- **SOS Alerts**
  - Real-time emergency message broadcasting
  - Location tracking and sharing
  - Customizable emergency details
  - Contact selection for alert distribution

### 3. Medical Guidance
- **CPR Guide (CprGuideActivity)**
  - Step-by-step CPR instructions
  - Video demonstrations
  - Interactive checklists
  - Emergency response protocols

- **Emergency Guides**
  - Fire Emergency Guide (FireEmergencyGuideActivity)
  - Earthquake Guide (EarthquakeEmergencyGuideActivity)
  - Flood Guide (FloodEmergencyGuideActivity)
  - Choking Guide (ChokingGuideActivity)
  - Wound Care Guide (WoundCareGuideActivity)

### 4. Emergency Checklists
- **Interactive Checklists**
  - Unconscious Patient Checklist
  - Chest Pain Checklist
  - Breathing Difficulty Checklist
  - Injury Assessment Checklist
  - Accident Response Checklist
  - Bleeding Control Checklist

### 5. Contact Management
- **ContactsActivity**
  - Emergency contact management
  - Contact categorization
  - Quick dial functionality
  - Contact details editing
  - Emergency service integration

### 6. Location Services
- **Maps Integration**
  - Real-time location tracking
  - Emergency service location finder
  - Navigation to emergency services
  - Location permission handling

### 7. Notification System
- **NotificationsActivity**
  - Real-time emergency alerts
  - Local disaster notifications
  - Emergency service updates
  - Custom notification preferences

### 8. User Profile Management
- **ProfilePageActivity**
  - Personal information management
  - Emergency preferences
  - Medical information storage
  - Privacy settings

## Technical Implementation

### Key Components
1. **RecyclerViews**
   - Emergency contacts display
   - Checklist items
   - Notification lists
   - Emergency guides

2. **Location Services**
   - FusedLocationProviderClient integration
   - Permission handling
   - Real-time location updates
   - Maps integration

3. **Firebase Integration**
   - Authentication
   - Real-time database
   - User data management
   - Contact synchronization

4. **Video Player**
   - Instructional video playback
   - Emergency response demonstrations
   - Medical procedure guides

### Important Functions

#### Authentication
```kotlin
// LoginActivity.kt
//fun authenticateUser(email: String, password: String)
```
- Handles user authentication
- Manages session creation
- Redirects to appropriate activity based on user status

#### Emergency Response
```kotlin
// EmergencyActivity.kt
//fun sendEmergencyAlert()
//fun getLastLocation()
//fun openGoogleMaps(latitude: Double, longitude: Double)
```
- Manages emergency alerts
- Handles location services
- Integrates with mapping services

#### Contact Management
```kotlin
// ContactsActivity.kt
//fun loadEmergencyContacts()
//fun updateContactDetails()
//fun syncContactsWithFirebase()
```
- Manages emergency contacts
- Handles contact synchronization
- Updates contact information

## Usage Guidelines

### Emergency Response
1. Access the emergency screen
2. Use the panic button for immediate assistance
3. Follow the emergency guides for specific situations
4. Contact emergency services through the app

### Medical Assistance
1. Select the appropriate medical guide
2. Follow the step-by-step instructions
3. Use the video guides for visual assistance
4. Complete the relevant checklist

### Contact Management
1. Add emergency contacts
2. Categorize contacts by relationship
3. Set up emergency service contacts
4. Manage contact preferences

## Security Features
- Secure authentication
- Encrypted data transmission
- Privacy-focused design
- Permission-based access control

## Dependencies
- Firebase Authentication
- Firebase Realtime Database
- Google Maps API
- Android Location Services
- AndroidX Libraries

## Support
For technical support or emergency assistance, please contact the app's support team through the in-app support feature. 