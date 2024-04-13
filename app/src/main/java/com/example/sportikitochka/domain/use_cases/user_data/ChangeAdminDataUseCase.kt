package com.example.sportikitochka.domain.use_cases.user_data

import com.example.sportikitochka.data.models.request.user_data.ChangeAdminDataRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.domain.repositories.UserDataRepository

class ChangeAdminDataUseCase(private val userDataRepository: UserDataRepository) {

    suspend fun execute(changeAdminDataRequest: ChangeAdminDataRequest) = userDataRepository.changeAdminData(changeAdminDataRequest)
}