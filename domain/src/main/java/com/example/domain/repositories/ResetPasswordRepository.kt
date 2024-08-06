package com.example.domain.repositories

import com.example.domain.coroutines.Response
import com.example.domain.models.ConfirmationCode


interface ResetPasswordRepository {

    suspend fun sendToEmail(email: String) : Response<ConfirmationCode>
    suspend fun confirmCode(newCode: String, receivedCode: String) : Response<Unit>
    suspend fun changePassword(email: String,
                               oldPassword: String,
                               confirmPassword: String) : Response<Unit>

}