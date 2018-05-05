package io.uh18.traveltalk.android

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.util.*

/**
 * Created by samuel.hoelzl on 05.05.18.
 */
class AppUtility {

    companion object {
        fun getUserId(context: Context): String {

            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            var uuid = preferences.getString(PREFERENCES_USER_ID, null )
            if (uuid == null) {
                uuid = UUID.randomUUID().toString()
                val editor = preferences.edit()
                editor.putString(PREFERENCES_USER_ID, uuid)
                editor.apply()
            }
            return uuid
        }
    }
}