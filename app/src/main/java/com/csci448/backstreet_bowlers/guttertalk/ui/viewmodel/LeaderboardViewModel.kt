package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel

import android.Manifest
import android.R.attr.country
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import android.util.Log.e
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import com.csci448.backstreet_bowlers.guttertalk.MainActivity
import com.csci448.backstreet_bowlers.guttertalk.data.database.UserRepository
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.effect.LeaderboardEffect
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.intent.LeaderboardIntent
import com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel.state.LeaderboardState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LeaderboardViewModel(
    application: android.app.Application,
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : AndroidViewModel(application),
    IViewModelContract<LeaderboardState, LeaderboardIntent, LeaderboardEffect> {

    private val context = getApplication<android.app.Application>().applicationContext
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context)


    // Using the state structure you already defined
    private var _savedState: LeaderboardState by savedStateHandle.saved(
        key = "SAVED_GAME_STATE",
        init = { LeaderboardState() }
    )
    private val _stateFlow = MutableStateFlow(_savedState)
    override val stateFlow: StateFlow<LeaderboardState> = _stateFlow.asStateFlow()

    private val _effectFlow = MutableSharedFlow<LeaderboardEffect?>()
    override val effectFlow: SharedFlow<LeaderboardEffect?> = _effectFlow.asSharedFlow()

    // Location request configuration
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0L)
        .setMaxUpdates(1)
        .build()

    // Somehow, we need to find a way to update the users location (once), after they have enabled locations, and update the table properly
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation?: return
            val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
            handleIntent(LeaderboardIntent.updateLocation(location, user?.uid))
        }
    }

    override fun handleIntent(intent: LeaderboardIntent) {
        when (intent) {
            is LeaderboardIntent.updateLocation -> {
                updateCityState(intent.location, intent.userId)
            }
            is LeaderboardIntent.OnLeaderboardPressed -> {
                if(!_stateFlow.value.isLocationAvailable){
                    viewModelScope.launch {
                        _effectFlow.emit(LeaderboardEffect.RequestLocationPermission)
                    }
                }
            }
//            is LeaderboardIntent.CheckLocationPermission -> {
//                val isGranted = ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                _stateFlow.update {
//                    it.copy(isLocationAvailable = isGranted)
//                }
//            }
            is LeaderboardIntent.RefreshPermissionStatus -> {
                Log.d("448.LeaderboardViewModel","Refresh permission status")
                val isGranted = ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (isGranted) {
                    if(_stateFlow.value.isLocationAvailable != true){
                        _stateFlow.update { it.copy(isLocationAvailable = true) }
                        startLocationUpdates()
                        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                            viewModelScope.launch {
                                try {
                                    // Priority ensures we get an accurate enough result for city/state
                                    val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                        if (location != null && user != null) {
                                            updateCityState(location, user.uid)
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e(
                                        "448.LeaderboardViewModel",
                                        "Failed to get current location",
                                        e
                                    )
                                }
                        }
                    }
                    // Update the state so the button enables

                    // Start the FusedLocationProvider
                } else {
                    _stateFlow.update { it.copy(isLocationAvailable = false) }
                }

            }
        }
    }

    private fun updateCityState(location: Location?, userId:String?) {
        Log.d("448.LeaderboardViewModel", "Attempting to update location ${location}, for userString ${userId}")
        if (location == null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Get Address from Geocoder
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val city = address.locality ?: "Unknown"
                    val adminDistrict = address.adminArea ?: ""
                    val country = address.countryName ?: ""

                    // 2. Update UI State
                    _stateFlow.update {
                        it.copy(
                            city = city,
                            adminDistrict = adminDistrict,
                            countryCode = address.countryCode ?: "",
                            location = location
                        )
                    }
                    Log.d("448.LeaderboardViewModel", "New stateflow: ${_stateFlow.value.countryCode}")

                    // 3. Connect to Firebase via Repository
                    // Assuming you have the current userId available
                    val currentUserId = userId
                    if(currentUserId != null){
                        userRepository.updateUserLocation(
                            userId = currentUserId,
                            city = city,
                            adminDistrict = adminDistrict,
                            country = country
                        )
                        // Stop updates after success to save battery (since you only need it once)
                        fusedLocationClient.removeLocationUpdates(locationCallback)
                    }
                }
            } catch (e: Exception) {
                Log.e("GutterTalk", "Geocoding/Firebase update failed", e)
            }
        }
    }

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override fun onCleared() {
        super.onCleared()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}