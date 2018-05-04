package io.uh18.traveltalk.android.backend.mock

import io.reactivex.Observable
import io.uh18.traveltalk.android.backend.Location
import io.uh18.traveltalk.android.backend.LocationApi
import retrofit2.mock.BehaviorDelegate

class LocationApiMock(private val delegate: BehaviorDelegate<LocationApi>) : LocationApi {

    override fun sendLocation(userId: String, location: Location): Observable<Location> {

        return delegate.returningResponse(location).sendLocation(userId, location)

    }

}