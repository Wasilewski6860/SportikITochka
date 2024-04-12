package com.example.sportikitochka.domain.use_cases.user_data

import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.domain.repositories.ProfileRepository
import com.example.sportikitochka.domain.repositories.UserDataRepository

class ChangeUserDataUseCase(private val userDataRepository: UserDataRepository) {

    suspend fun execute(changeDataUserRequest: ChangeDataUserRequest) = userDataRepository.changeUserData(changeDataUserRequest)
}