package io.uh18.traveltalk.android.db

import android.arch.persistence.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime

/**
 * Created by samuel.hoelzl on 05.05.18.
 */
class RoomConverter {


    @TypeConverter
    fun longToInstant(value: String): LocalDateTime {
        return LocalDateTime.parse(value, org.threeten.bp.format.DateTimeFormatter.ISO_DATE_TIME)
    }


    @TypeConverter
    fun dateTimeToLong(localDateTime: LocalDateTime): String {
        return org.threeten.bp.format.DateTimeFormatter.ISO_DATE_TIME.format(localDateTime)
    }

}