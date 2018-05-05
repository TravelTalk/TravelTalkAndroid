package io.uh18.traveltalk.android.model

import org.threeten.bp.LocalDateTime

/**
 * Created by samuel.hoelzl on 04.05.18.
 */

data class Message (var message: String, var userId: String, var timeStamp: LocalDateTime)