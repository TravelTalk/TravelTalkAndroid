package io.uh18.traveltalk.android.backend

interface TravelTalkClient {

    fun createLocationService(): LocationApi
}