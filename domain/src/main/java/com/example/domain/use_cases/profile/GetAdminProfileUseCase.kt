package com.example.domain.use_cases.profile

import com.example.domain.repositories.ProfileRepository

class GetAdminProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute() = profileRepository.getAdminProfileUseCase()
}