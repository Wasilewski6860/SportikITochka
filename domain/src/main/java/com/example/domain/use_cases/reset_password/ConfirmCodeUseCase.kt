package com.example.domain.use_cases.reset_password

import com.example.domain.repositories.ResetPasswordRepository

class ConfirmCodeUseCase(private val resetPasswordRepository: ResetPasswordRepository) {

    suspend fun execute(code: String, receivedCode: String)  = resetPasswordRepository.confirmCode(code, receivedCode)
}