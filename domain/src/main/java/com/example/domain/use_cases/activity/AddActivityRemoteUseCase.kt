package com.example.domain.use_cases.activity

import com.example.domain.models.SportActivity
import com.example.domain.repositories.ActivityRepository
import java.io.File

class AddActivityRemoteUseCase(private val activityRepository: ActivityRepository) {

    suspend fun execute(activity: SportActivity, image: File) = activityRepository.addActivityRemote(activity, image)
}