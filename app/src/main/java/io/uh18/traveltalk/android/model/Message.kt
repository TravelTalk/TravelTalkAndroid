package io.uh18.traveltalk.android.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.LocalDateTime

/**
 * Created by samuel.hoelzl on 04.05.18.
 */
@Entity(tableName = "messages")
data class Message(@PrimaryKey(autoGenerate = true) var id: Long?,
                   @ColumnInfo(name = "message") var message: String,
                   @ColumnInfo(name = "userId") var userId: String,
                   @ColumnInfo(name = "timeStamp") var timeStamp: LocalDateTime)
