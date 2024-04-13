package com.example.sportikitochka.domain.use_cases.user_data

import com.example.sportikitochka.domain.repositories.UserDataRepository

class GetAdminDataUseCase(private val userDataRepository: UserDataRepository) {

    suspend fun execute() = userDataRepository.getAdminData()
}