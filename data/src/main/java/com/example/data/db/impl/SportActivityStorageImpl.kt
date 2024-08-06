package com.example.data.db.impl

import com.example.data.db.SportActivitiesDatabase
import com.example.data.db.SportActivitiesStorage
import com.example.data.db.dto.SportActivityDto

class SportActivityStorageImpl(sportActivitiesDatabase: com.example.data.db.SportActivitiesDatabase):
    com.example.data.db.SportActivitiesStorage {

    private val sportActivitiesDao = sportActivitiesDatabase.dao

    override suspend fun addActivity(activityDto: com.example.data.db.dto.SportActivityDto) = sportActivitiesDao.addActivity(activityDto)

    override suspend fun getAllActivities(): List<com.example.data.db.dto.SportActivityDto> = sportActivitiesDao.getAllActivities()

    override suspend fun getActivity(id: Int): com.example.data.db.dto.SportActivityDto = sportActivitiesDao.getActivity(id)

    override suspend fun clearAll() = sportActivitiesDao.clearAll()
}