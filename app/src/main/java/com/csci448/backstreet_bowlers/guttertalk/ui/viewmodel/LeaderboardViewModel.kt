package com.csci448.backstreet_bowlers.guttertalk.ui.viewmodel

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import com.csci448.backstreet_bowlers.guttertalk.MainActivity
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

class LeaderboardViewModel(
    application: android.app.Application,
    private val savedStateHandle: SavedStateHandle
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

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            if (location != null) {
                handleIntent(LeaderboardIntent.updateLocation(location))
            }
        }
    }

    override fun handleIntent(intent: LeaderboardIntent) {
        when (intent) {
            is LeaderboardIntent.updateLocation -> {
                updateCityState(intent.location)
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
                val isGranted = ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (isGranted) {
                    // Update the state so the button enables
                    _stateFlow.update { it.copy(isLocationAvailable = true) }
                    // Start the FusedLocationProvider
                    startLocationUpdates()
                } else {
                    _stateFlow.update { it.copy(isLocationAvailable = false) }
                }
            }
        }
    }

    private fun updateCityState(location: Location?) {
        if (location == null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    _stateFlow.update {
                        it.copy(
                            city = address.locality ?: "",
                            adminDistrict = address.adminArea ?: "",
                            countryCode = address.countryCode ?: "",
                            location = location
                        )
                    }
                    // Sync this location back to your UserInformation in Firestore here
                }
            } catch (e: Exception) {
                Log.e("GutterTalk", "Geocoding failed", e)
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