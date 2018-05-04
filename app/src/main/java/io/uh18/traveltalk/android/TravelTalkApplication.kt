package io.uh18.traveltalk.android

import android.app.Application
import timber.log.Timber

class TravelTalkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}