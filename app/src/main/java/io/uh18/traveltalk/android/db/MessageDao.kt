package io.uh18.traveltalk.android.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Maybe
import io.uh18.traveltalk.android.model.Message

/**
 * Created by samuel.hoelzl on 05.05.18.
 */
@Dao
interface MessageDao {

    @Query("SELECT * from messages")
    fun getAll(): Maybe<List<Message>>

    @Insert(onConflict = REPLACE)
    fun insert(message: Message)

    @Query("DELETE from messages")
    fun deleteAll()
}