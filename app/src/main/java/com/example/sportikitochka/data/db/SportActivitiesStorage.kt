package com.example.sportikitochka.data.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sportikitochka.data.db.dto.SportActivityDto

interface SportActivitiesStorage {

    suspend fun addActivity(activityDto: SportActivityDto)

    suspend fun getAllActivities(): List<SportActivityDto>

    suspend fun getActivity(id: Int): SportActivityDto

    suspend fun clearAll()

}