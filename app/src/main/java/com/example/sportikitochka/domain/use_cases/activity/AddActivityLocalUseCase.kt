package com.example.sportikitochka.domain.use_cases.activity

import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.repositories.ActivityRepository

class AddActivityLocalUseCase(private val activityRepository: ActivityRepository) {

    suspend fun execute(activity: SportActivity) = activityRepository.addActivity(activity)
}