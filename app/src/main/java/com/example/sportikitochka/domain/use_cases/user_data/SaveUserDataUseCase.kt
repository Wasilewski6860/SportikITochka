package com.example.sportikitochka.domain.use_cases.user_data

import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import com.example.sportikitochka.domain.repositories.SessionRepository

class SaveUserDataUseCase(private val sessionRepository: SessionRepository) {

    fun execute(userDataResponse: UserDataResponse?) = sessionRepository.saveUserData(userDataResponse)
}