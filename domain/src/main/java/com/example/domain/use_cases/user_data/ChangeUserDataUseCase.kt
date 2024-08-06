package com.example.domain.use_cases.user_data

import com.example.domain.repositories.UserDataRepository
import java.io.File

class ChangeUserDataUseCase(private val userDataRepository: UserDataRepository) {

    suspend fun execute(
        name: String,
        weight: Int,
        image: File,
        phone: String,
        birthday: String
    ) = userDataRepository.changeUserData(
        name = name,
        weight = weight,
        image = image,
        phone = phone,
        birthday = birthday
    )
}