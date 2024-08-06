package com.example.domain.use_cases.auth

import com.example.domain.repositories.AuthRepository
import java.io.File

class RegisterUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(
        email: String,
        name: String,
        weight: Int,
        image: File,
        phone: String,
        birthday: String,
        password: String,
    )  = authRepository.register(
        email = email,
        name = name,
        weight = weight,
        image = image,
        phone = phone,
        birthday = birthday,
        password = password
    )
}