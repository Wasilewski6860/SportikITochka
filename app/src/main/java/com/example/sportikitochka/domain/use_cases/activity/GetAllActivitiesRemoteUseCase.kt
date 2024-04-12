package com.example.sportikitochka.domain.use_cases.activity

import com.example.sportikitochka.domain.repositories.ActivityRepository

class GetAllActivitiesRemoteUseCase(private val activityRepository: ActivityRepository) {

    suspend fun execute() = activityRepository.getAllActivitiesRemote()
}