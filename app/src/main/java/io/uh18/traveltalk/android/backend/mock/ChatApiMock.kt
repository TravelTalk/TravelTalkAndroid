package io.uh18.traveltalk.android.backend.mock

import io.reactivex.Observable
import io.uh18.traveltalk.android.backend.ChatApi
import io.uh18.traveltalk.android.model.Location
import io.uh18.traveltalk.android.backend.LocationApi
import io.uh18.traveltalk.android.model.Message
import retrofit2.mock.BehaviorDelegate

class ChatApiMock(private val delegate: BehaviorDelegate<ChatApi>) : ChatApi {

    override fun sendMessage(message: Message): Observable<Message> {

        return delegate.returningResponse(message).sendMessage(message)

    }

}