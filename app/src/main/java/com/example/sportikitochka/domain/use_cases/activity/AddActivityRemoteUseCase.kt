package com.example.sportikitochka.domain.use_cases.activity

import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.repositories.ActivityRepository

class AddActivityRemoteUseCase(private val activityRepository: ActivityRepository) {

    suspend fun execute(activity: SportActivity) = activityRepository.addActivityRemote(activity)
}