package com.example.sportikitochka.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sportikitochka.data.db.dto.SportActivityDto

@Database(
    entities = [
        SportActivityDto::class
    ],
    version = 1
)
abstract class SportActivitiesDatabase : RoomDatabase() {

    abstract val dao: SportActivitiesDao

    companion object {

        private var INSTANCE: SportActivitiesDatabase? = null

        fun getDataBase(context: Context): SportActivitiesDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    SportActivitiesDatabase::class.java,
                    "sport_activity_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}