package com.example.sportikitochka.domain.use_cases.reset_password

import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.domain.repositories.AuthRepository
import com.example.sportikitochka.domain.repositories.ResetPasswordRepository

class ConfirmCodeUseCase(private val resetPasswordRepository: ResetPasswordRepository) {

    suspend fun execute(code: String, receivedCode: String)  = resetPasswordRepository.confirmCode(code, receivedCode)
}