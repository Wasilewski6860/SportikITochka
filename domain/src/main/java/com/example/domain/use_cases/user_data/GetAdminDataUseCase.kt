package com.example.domain.use_cases.user_data

import com.example.domain.repositories.UserDataRepository

class GetAdminDataUseCase(private val userDataRepository: UserDataRepository) {

    suspend fun execute() = userDataRepository.getAdminData()
}