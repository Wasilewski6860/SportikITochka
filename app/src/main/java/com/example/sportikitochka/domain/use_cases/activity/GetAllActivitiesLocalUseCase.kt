package com.example.sportikitochka.domain.use_cases.activity

import com.example.sportikitochka.domain.repositories.ActivityRepository

class GetAllActivitiesLocalUseCase(private val activityRepository: ActivityRepository) {

    suspend fun execute()  = activityRepository.getAllActivities()
}