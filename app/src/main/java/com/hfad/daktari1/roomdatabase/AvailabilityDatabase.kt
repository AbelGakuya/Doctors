package com.hfad.daktari1.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Availability::class], version = 1, exportSchema = false)
abstract class AvailabilityDatabase : RoomDatabase() {

    abstract fun availabilityDao(): AvailabilityDao

    companion object {
        @Volatile
        private var INSTANCE: AvailabilityDatabase? = null

        fun getInstance(context: Context) : AvailabilityDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AvailabilityDatabase::class.java,
                        "availability_database"
                    ).build()
                    INSTANCE = instance
                }

                return instance
            }
        }


    }
}