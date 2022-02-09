package com.example.camping.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.camping.data.vo.Item

@Database(entities = [Item::class], version = 1)
abstract class MDataBase : RoomDatabase() {
    abstract fun favoriteDao(): MDao

    companion object {
        private var instance: MDataBase? = null

        @Synchronized
        fun getInstance(context: Context): MDataBase? {
            if (instance == null) {
                synchronized(MDataBase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MDataBase::class.java,
                        "camping-database"
                    ).build()
                }
            }

            return instance
        }
    }
}