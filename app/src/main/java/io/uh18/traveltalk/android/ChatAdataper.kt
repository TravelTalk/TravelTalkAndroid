package io.uh18.traveltalk.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.uh18.traveltalk.android.model.ChatItem

/**
 * Created by samuel.hoelzl on 04.05.18.
 */

class ChatAdapter(private var myUserId: String, context: Context?, resource: Int, objects: MutableList<ChatItem>?) : ArrayAdapter<ChatItem>(context, resource, objects) {

    

    override fun getCount(): Int {
        return super.getCount()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var rowItem = convertView
        var resId = 0
        val item = getItem(position)
        if(item.userId == myUserId){
            resId = R.layout.list_item_conversation_right
        } else {
            resId = R.layout.list_item_conversation_left
        }
        rowItem = LayoutInflater.from(context).inflate(resId, parent, false)
        var tvMessage = rowItem.findViewById<TextView>(R.id.message)
        tvMessage.text = item.message

        return rowItem!!
    }

}