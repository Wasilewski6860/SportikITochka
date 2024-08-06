package com.example.domain.use_cases.user_data

import com.example.domain.models.UserData
import com.example.domain.repositories.SessionRepository

class SaveUserDataUseCase(private val sessionRepository: SessionRepository) {

    fun execute(userDataResponse: UserData?) = sessionRepository.saveUserData(
        userDataResponse
    )
}