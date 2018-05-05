package io.uh18.traveltalk.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.uh18.traveltalk.android.model.Message

/**
 * Created by samuel.hoelzl on 04.05.18.
 */

class MessageAdapter(private var myUserId: String, context: Context?, resource: Int, objects: MutableList<Message>?) : ArrayAdapter<Message>(context, resource, objects) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var rowItem = convertView
        var resId = 0
        val item = getItem(position)
        if (item.userId == myUserId) {
            resId = R.layout.list_item_conversation_right
        } else {
            resId = R.layout.list_item_conversation_left
        }
        rowItem = LayoutInflater.from(context).inflate(resId, parent, false)
        var tvMessage = rowItem.findViewById<TextView>(R.id.message)
        tvMessage.text = item.message
        var tvTimeStamp = rowItem.findViewById<TextView>(R.id.timeStamp)
        tvTimeStamp.text = org.threeten.bp.format.DateTimeFormatter.ofPattern("HH:mm").format(item.timeStamp)

        return rowItem!!
    }

}