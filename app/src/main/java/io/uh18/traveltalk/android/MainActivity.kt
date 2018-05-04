package io.uh18.traveltalk.android

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.uh18.traveltalk.android.model.ChatItem
import com.google.android.gms.location.*
import io.uh18.traveltalk.android.backend.Location
import io.uh18.traveltalk.android.backend.createLocationService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*
import android.graphics.Bitmap
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime


class MainActivity : AppCompatActivity() {

    private val chat = LinkedList<ChatItem>()
    private val myUserID = "0"
    private val messagePollingJob = io.uh18.traveltalk.android.jobs.MessagePollingJob()



    // provider for locations
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // callback receiving the location updates
    private lateinit var locationCallback: LocationCallback

    // request for location updates
    private val locationRequest: LocationRequest = LocationRequest()
            .setPriority(LOCATION_UPDATE_PRIORITY)
            .setInterval(LOCATION_UPDATE)
            .setFastestInterval(LOCATION_UPDATE_FAST)

    private val locationClient = createLocationService()

    private val sendLocationCallback: Callback<Location> = object : Callback<Location> {
        override fun onResponse(call: Call<Location>, response: Response<Location>) {
            val loc = response.body()
        }

        override fun onFailure(call: Call<Location>, throwable: Throwable) {
            Timber.e(throwable)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = ChatAdapter(myUserID, this, 0, chat)

        lvMessages.adapter = adapter

        btn_send.setOnClickListener { v: View? ->
            val message = et_message.text.toString()
            if (message.isBlank()) {
                Timber.d("No message entered")
                return@setOnClickListener
            }

            send(message)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        ensurePermissions()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    Timber.d("New location: %s", location)
                    val sendLocationCallback: Callback<Location> = sendLocationCallback
                    locationClient.sendLocation("abc", Location(location.longitude, location.latitude)).enqueue(sendLocationCallback)
                }
            }
        }
    }

    private fun ensurePermissions() {
        if (ContextCompat.checkSelfPermission(this, PERMISSION_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            PERMISSION_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(PERMISSION_LOCATION),
                        PERMISSIONS_REQUEST_LOCATION)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        // Got last known location. In some rare situations this can be null.
                        Timber.d("Last location: %s", location)
                        locationClient.sendLocation("abc",
                                Location(location.longitude, location.latitude))
                                .enqueue(sendLocationCallback)
                    }.addOnFailureListener {
                        Timber.e(it, "Error getting last location")
                    }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Timber.d("Permission granted")
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Timber.w("Permission denied")

                    val dialog = AlertDialog.Builder(this)
                    dialog.setIcon(android.R.drawable.ic_dialog_alert)
                    dialog.setTitle("Permission required")
                    dialog.setMessage("%s needs location permissions to work correctly.".format(getString(R
                            .string.app_name)))
                    dialog.setPositiveButton(android.R.string.ok, { _, _ -> finish() })
                    dialog.show()

                }
                return
            }

        }
    }

    override fun onStop() {
        super.onStop()
        messagePollingJob.stopTest()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
        messagePollingJob.startTest()
    }


    private fun startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, PERMISSION_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Timber.w("Location permission not granted. Can't request location updates.")
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */)
    }


    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    private fun send(message: String) {
        if (message.isBlank()){
            return
        }

        // TODO: benedikt.stricker 04.05.18 - send to server
        Timber.d("Send message %s", message)
        chat.add(ChatItem(message, myUserID, LocalDateTime.now()))
        (lvMessages.adapter as ChatAdapter).notifyDataSetChanged()
        et_message.text.clear()

    }
}
