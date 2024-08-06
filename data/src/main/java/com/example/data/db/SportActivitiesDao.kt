package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sportikitochka.data.db.dto.SportActivityDto

@Dao
interface SportActivitiesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addActivity(activityDto: SportActivityDto)
    @Query("SELECT * FROM activity_entity")
    suspend fun getAllActivities(): List<SportActivityDto>
    @Query("SELECT * FROM activity_entity WHERE id =:id")
    suspend fun getActivity(id: Int): SportActivityDto
    @Query("DELETE FROM activity_entity")
    suspend fun clearAll()
}