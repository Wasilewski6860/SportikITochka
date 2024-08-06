package com.example.domain.use_cases.reset_password

import com.example.domain.repositories.ResetPasswordRepository

class ResetPasswordUseCase(private val resetPasswordRepository: ResetPasswordRepository) {

    suspend fun execute(email: String,pass: String, confPass: String)  = resetPasswordRepository.changePassword(email,pass, confPass)
}