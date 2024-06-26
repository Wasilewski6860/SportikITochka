package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.auth.AdminRegisterRequest
import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.data.models.response.auth.AdminRegisterResponse
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.RegisterResponse
import com.example.sportikitochka.data.models.response.auth.ValidateEmailResponse
import com.example.sportikitochka.data.network.AuthApi
import com.example.sportikitochka.domain.repositories.AuthRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class AuthRepositoryImpl(private val authApi: AuthApi, private val sessionRepository: SessionRepository) :
    AuthRepository {

    override suspend fun login(loginRequest: LoginRequest) = authApi.login(loginRequest)
    override suspend fun validateEmail(email: String): Response<ValidateEmailResponse> = authApi.validateEmail(email)

    override suspend fun register(email: String,registerRequest: RegisterRequest): Response<RegisterResponse> {

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", registerRequest.name)
            .addFormDataPart("weight", registerRequest.weight.toString())
            .addFormDataPart("phone", registerRequest.phone)
            .addFormDataPart("birthday", registerRequest.birthday)
            .addFormDataPart("password_hash", registerRequest.password)
            .addFormDataPart(
                "avatar",
                registerRequest.image.name,
                registerRequest.image.asRequestBody("image/*".toMediaTypeOrNull())
            )
            .build()
        return authApi.register(email, requestBody)
    }
    override suspend fun register(
        email: String,
        registerRequest: AdminRegisterRequest
    ): Response<AdminRegisterResponse> {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", registerRequest.name)
            .addFormDataPart("phone", registerRequest.phone)
            .addFormDataPart("birthday", registerRequest.birthday)
            .addFormDataPart("password_hash", registerRequest.password)
            .addFormDataPart(
                "avatar",
                registerRequest.image.name,
                registerRequest.image.asRequestBody("image/*".toMediaTypeOrNull())
            )
            .build()

        return authApi.adminRegister(email, requestBody)
    }

    override fun saveSession(loginResponse: LoginResponse): Boolean {
        sessionRepository.saveSession(loginResponse)
        return true
    }


}
