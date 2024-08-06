package com.example.domain.use_cases.activity

import com.example.domain.repositories.ActivityRepository

class GetAllActivitiesLocalUseCase(private val activityRepository: ActivityRepository) {

    suspend fun execute()  = activityRepository.getAllActivities()
}