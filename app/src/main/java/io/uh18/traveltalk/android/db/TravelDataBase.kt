package io.uh18.traveltalk.android.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import io.uh18.traveltalk.android.model.Message

/**
 * Created by samuel.hoelzl on 05.05.18.
 */
@Database(entities = arrayOf(Message::class), version = 1)

@TypeConverters(RoomConverter::class)
abstract class TravelDataBase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {
        private var INSTANCE: TravelDataBase? = null

        fun getInstance(context: Context): TravelDataBase? {
            if (INSTANCE == null) {
                synchronized(TravelDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TravelDataBase::class.java, "travel.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}