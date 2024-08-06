package com.example.domain.use_cases.profile

import com.example.domain.repositories.ProfileRepository
import com.example.domain.repositories.SessionRepository

class GetProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute(period: String) = profileRepository.getUserProfileRemote(period = period)
}