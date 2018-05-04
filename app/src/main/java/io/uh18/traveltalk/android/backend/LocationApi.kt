package io.uh18.traveltalk.android.backend

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


interface LocationApi {

    @POST("users/{userId}/locations")
    fun sendLocation(@Path("userId") userId: String, @Body location: Location): Observable<Location>

}