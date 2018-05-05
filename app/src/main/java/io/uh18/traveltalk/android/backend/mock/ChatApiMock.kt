package io.uh18.traveltalk.android.backend.mock

import io.reactivex.Observable
import io.uh18.traveltalk.android.backend.ChatApi
import io.uh18.traveltalk.android.model.Message
import retrofit2.mock.BehaviorDelegate
import timber.log.Timber
import java.util.*

class ChatApiMock(private val delegate: BehaviorDelegate<ChatApi>) : ChatApi {

    val random = Random()

    val words = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.".split(' ')

    override fun getMessages(userId: String): Observable<List<Message>> {


        val msgs = listOf(generateMessage())

        return delegate.returningResponse(msgs).getMessages(userId)
    }

    private fun generateMessage(): Message {
        val n = random.nextInt(9) + 1
        val start = random.nextInt(words.size - n)

        return Message(words.slice(start..start + n).joinToString(" "),
                UUID.randomUUID().toString())
    }

    override fun sendMessage(message: Message): Observable<Message> {

        Timber.d("send message")

        return delegate.returningResponse(message).sendMessage(message)

    }

}