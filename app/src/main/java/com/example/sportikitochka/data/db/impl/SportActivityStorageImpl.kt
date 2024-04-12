package com.example.sportikitochka.data.db.impl

import com.example.sportikitochka.data.db.SportActivitiesDatabase
import com.example.sportikitochka.data.db.SportActivitiesStorage
import com.example.sportikitochka.data.db.dto.SportActivityDto

class SportActivityStorageImpl(sportActivitiesDatabase: SportActivitiesDatabase): SportActivitiesStorage {

    private val sportActivitiesDao = sportActivitiesDatabase.dao

    override suspend fun addActivity(activityDto: SportActivityDto) = sportActivitiesDao.addActivity(activityDto)

    override suspend fun getAllActivities(): List<SportActivityDto> = sportActivitiesDao.getAllActivities()

    override suspend fun getActivity(id: Int): SportActivityDto = sportActivitiesDao.getActivity(id)

    override suspend fun clearAll() = sportActivitiesDao.clearAll()
}