package com.example.domain.use_cases.user_data

import com.example.domain.repositories.UserDataRepository
import java.awt.Image
import java.io.File

class ChangeAdminDataUseCase(private val userDataRepository: UserDataRepository) {

    suspend fun execute(
        name: String,
        image: File,
        phone: String,
        birthday: String
    ) = userDataRepository.changeAdminData(
        name = name,
        image = image,
        phone = phone,
        birthday = birthday
    )
}