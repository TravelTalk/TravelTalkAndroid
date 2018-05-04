package io.uh18.traveltalk.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }
}
