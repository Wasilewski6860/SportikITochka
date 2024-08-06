package com.example.domain.use_cases.reset_password

import com.example.domain.repositories.ResetPasswordRepository

class SendToEmailUseCase(private val resetPasswordRepository: ResetPasswordRepository) {

    suspend fun execute(email: String)  = resetPasswordRepository.sendToEmail(email)
}