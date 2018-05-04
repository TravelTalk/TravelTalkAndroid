package io.uh18.traveltalk.android

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

class TravelTalkApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // init ThreeTen timezones
        AndroidThreeTen.init(this);

        // init Timber debug logging
        Timber.plant(Timber.DebugTree())
    }
}