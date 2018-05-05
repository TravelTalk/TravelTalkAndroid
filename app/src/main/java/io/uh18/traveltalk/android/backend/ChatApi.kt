package io.uh18.traveltalk.android.backend

import io.reactivex.Observable
import io.uh18.traveltalk.android.model.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ChatApi {

    @POST("messages")
    fun sendMessage(@Body message: Message): Observable<Message>

    @GET("users/{userId}/messages")
    fun getMessages(@Path("userId") userId: String): Observable<List<Message>>

}