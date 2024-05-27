package com.example.sportikitochka.domain.use_cases.profile

import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.domain.repositories.ProfileRepository

class GetAdminProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute() = profileRepository.getAdminProfileUseCase()
}