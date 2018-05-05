package io.uh18.traveltalk.android

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.location.*
import io.reactivex.Observable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.uh18.traveltalk.android.model.Location
import io.uh18.traveltalk.android.backend.mock.TravelTalkClientMock
import io.uh18.traveltalk.android.model.Message
import io.uh18.traveltalk.android.db.TravelDataBase
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*
import org.threeten.bp.LocalDateTime
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private var chat :ArrayList<Message> = ArrayList()
    private val myUserID = "0"

    private var travelDb: TravelDataBase? = null
    private val disposable = CompositeDisposable()



    // provider for locations
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // callback receiving the location updates
    private lateinit var locationCallback: LocationCallback

    // request for location updates
    private val locationRequest: LocationRequest = LocationRequest()
            .setPriority(LOCATION_UPDATE_PRIORITY)
            .setInterval(LOCATION_UPDATE)
            .setFastestInterval(LOCATION_UPDATE_FAST)


    private val locationService by lazy {
        TravelTalkClientMock().createLocationService()
    }

    private val chatService by lazy {
        TravelTalkClientMock().createChatService()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        travelDb = TravelDataBase.getInstance(this)

        val adapter = MessageAdapter(myUserID, this, 0, chat)

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

        Observable.interval(10, TimeUnit.SECONDS, Schedulers.io())
                .map { tick ->
                    Timber.d("Tick %s", tick)
                    chatService.getMessages(myUserID)
                }
                .doOnError { e ->
                    Timber.e(e, "Error getting messages in doOnError")
                }
                .retry()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            result.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ it.forEach(this::addMessage) })

                        },
                        { error -> Timber.e(error, "Error getting messages in subscribe") })

        ensurePermissions()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    Timber.d("New location: %s", location)
                    val loc = Location(location.longitude, location.latitude)

                    locationService.sendLocation(myUserID, loc)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { result -> Timber.d("Success: %s", result) },
                                    { error -> Timber.e(error) }
                            )

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
                        val loc = Location(location.longitude, location.latitude)
                        locationService.sendLocation(myUserID, loc)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        { result -> Timber.d("Success: %s", result) },
                                        { error -> Timber.e(error) }
                                )
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

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
        loadMessages()
    }

    private fun loadMessages() {

        travelDb?.messageDao()?.getAll()!!.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    result ->
                    Timber.d("messages loaded")
                    chat.clear()
                    chat.addAll(result)
                    (lvMessages.adapter as MessageAdapter).notifyDataSetInvalidated()
                })
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
        disposable.dispose()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    private fun send(message: String) {
        if (message.isBlank()) {
            return
        }

        val msg = Message(null,message, myUserID, LocalDateTime.now())
        Timber.d("Send message %s", message)
        addMessage(msg)
        et_message.text.clear()
        chatService.sendMessage(msg)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Timber.d("Success: %s", result)

                        },
                        { error ->
                            Timber.e(error, "Can't send message")
                            removeMessage(msg)
                            Snackbar.make(root, "Can't send message", Snackbar.LENGTH_SHORT)
                        }

                )
    }


    fun insertMessage(message: Message): Completable {
        return Completable.fromAction {
            travelDb?.messageDao()?.insert(message)
        }
    }

    private fun addMessage(message: Message) {
        disposable.add(insertMessage(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("message inserted")
                    chat.add(message)
                    (lvMessages.adapter as MessageAdapter).notifyDataSetInvalidated()
                },
                        { error -> Timber.e(error, "Unable to insert message") }))


        et_message.text.clear()    }


    private fun removeMessage(msg: Message){
        chat.remove(msg)
        (lvMessages.adapter as MessageAdapter).notifyDataSetChanged()
    }
}
