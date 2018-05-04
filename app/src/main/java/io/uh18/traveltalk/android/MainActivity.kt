package io.uh18.traveltalk.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.uh18.traveltalk.android.model.ChatItem
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    val chat = LinkedList<ChatItem>()
    val myUserID = "0"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = ChatAdapter(myUserID, this, 0, chat)

        lvMessages.adapter = adapter

        btn_send.setOnClickListener { v: View? ->
            val message = et_message.text.toString()
            if (message.isBlank()) {
                Timber.d("No message entered")
                return@setOnClickListener
            }

            send(message)
        }


    }

    private fun send(message: String) {
        if (message.isBlank()){
            return
        }

        // TODO: benedikt.stricker 04.05.18 - send to server
        Timber.d("Send message %s", message)
        chat.add(ChatItem(message, myUserID))
        (lvMessages.adapter as ChatAdapter).notifyDataSetChanged()
        et_message.text.clear()

    }
}
