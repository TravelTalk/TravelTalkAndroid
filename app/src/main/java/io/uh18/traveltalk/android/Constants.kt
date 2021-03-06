package io.uh18.traveltalk.android

import android.Manifest
import com.google.android.gms.location.LocationRequest
import org.threeten.bp.Duration

const val PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
const val PERMISSIONS_REQUEST_LOCATION: Int = 1000
const val JOB_MESSAGE_POLLING: Long = 3000

const val LOCATION_UPDATE_PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
val LOCATION_UPDATE: Long = Duration.ofSeconds(30).toMillis()
val LOCATION_UPDATE_FAST: Long = Duration.ofSeconds(10).toMillis()

var PREFERENCES_USER_ID = "PREFERENCES_USER_ID"


const val SERVICE_URL = "http://10.11.167.248:5000/"