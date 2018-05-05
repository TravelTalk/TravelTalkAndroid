package io.uh18.traveltalk.android

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.uh18.traveltalk.android.model.Message
import kotlin.math.absoluteValue

/**
 * Created by samuel.hoelzl on 04.05.18.
 */

class MessageAdapter(private var myUserId: String, context: Context?, resource: Int, objects: MutableList<Message>?) : ArrayAdapter<Message>(context, resource, objects) {

    val colors = listOf(
            Color.rgb(240, 255, 255),
            Color.rgb(240, 240, 255),
            Color.rgb(255, 240, 255),
            Color.rgb(255, 240, 240),
            Color.rgb(255, 255, 240),
            Color.rgb(240, 255, 240)
    )

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

        val rectangle = rowItem.findViewById<ConstraintLayout>(R.id.rectangle)
        (rectangle.background as GradientDrawable).setColor(getColor(item))

        return rowItem!!
    }

    private fun getColor(item: Message) =
            colors[item.userId.hashCode().absoluteValue % colors.size]

}