package com.example.domain.use_cases.activity

import com.example.domain.models.SportActivity
import com.example.domain.repositories.ActivityRepository

class AddActivityLocalUseCase(private val activityRepository: ActivityRepository) {

    suspend fun execute(activity: SportActivity) = activityRepository.addActivity(activity)
}