package io.uh18.traveltalk.android.backend

import io.reactivex.Observable
import io.uh18.traveltalk.android.model.Message
import retrofit2.http.Body
import retrofit2.http.POST


interface ChatApi {

    @POST("messages")
    fun sendMessage(@Body message: Message): Observable<Message>

}