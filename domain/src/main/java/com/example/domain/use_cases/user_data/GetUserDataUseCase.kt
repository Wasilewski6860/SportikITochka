package com.example.domain.use_cases.user_data

import com.example.domain.repositories.UserDataRepository

class GetUserDataUseCase(private val userDataRepository: UserDataRepository) {

    suspend fun execute() = userDataRepository.getUserData()
}