package com.demirli.a42gamesuggestionapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demirli.a42gamesuggestionapp.model.Poll

@Database(entities = [Poll::class], version = 1)
abstract class PollDatabase: RoomDatabase() {

    abstract fun pollDao(): PollDao

    companion object{
        @Volatile
        var INSTANCE: PollDatabase? = null

        @Synchronized
        fun getInstance(context: Context): PollDatabase {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    PollDatabase::class.java,
                    "polls.db")
                    .build()
            }
            return INSTANCE as PollDatabase
        }



    }

}