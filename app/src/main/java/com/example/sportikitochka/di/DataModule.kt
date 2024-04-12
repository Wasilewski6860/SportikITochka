package com.example.sportikitochka.di

import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.data.models.response.ErrorResponse
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.RegisterResponse
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.auth.ValidateEmailResponse
import com.example.sportikitochka.data.network.AuthApi
import com.example.sportikitochka.data.repositories.AuthRepositoryImpl
import com.example.sportikitochka.data.repositories.OnboardingRepositoryImpl
import com.example.sportikitochka.data.repositories.PreferencesRepositoryImpl
import com.example.sportikitochka.data.repositories.SessionRepositoryImpl
import com.example.sportikitochka.domain.repositories.AuthRepository
import com.example.sportikitochka.domain.repositories.OnboardingRepository
import com.example.sportikitochka.domain.repositories.PreferencesRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.other.Validator
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.koin.dsl.module
import retrofit2.Response

val dataModule = module {

    var userEmail: String = "trollivanovich6860@gmail.com"
    var userPassword: String = "2143658709"
    var userAccessToken: String = "token"
    var uid: Int = 12345
    var userRole: UserType = UserType. Normal
    var isBlocked: Boolean = false

    single<Moshi> {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single<AuthApi> {
        object : AuthApi {
            override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {

                if (userEmail == loginRequest.email && userPassword == loginRequest.password && !isBlocked) {
                    return Response.success(
                        LoginResponse (
                            accessToken = userAccessToken,
                            role = userRole.toString(),
                            userId = uid,
                            success = true
                        )
                    )
                } else if (userEmail == loginRequest.email && userPassword == loginRequest.password && isBlocked) {
                    // Создаем объект ResponseBody для передачи ошибки
                    val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "USER_BLOCKED")

                    // Создаем неуспешный Response с кодом ошибки и объектом ResponseBody
                    val response = Response.error<LoginResponse>(400, errorResponseBody)

                    return response
                }
                else if (userEmail == loginRequest.email && userPassword != loginRequest.password) {
                    // Создаем объект ResponseBody для передачи ошибки
                    val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "INCORRECT_PASSWORD")

                    // Создаем неуспешный Response с кодом ошибки и объектом ResponseBody
                    val response = Response.error<LoginResponse>(400, errorResponseBody)

                    return response
                }

                // Создаем объект ResponseBody для передачи ошибки
                val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "Error message")

                // Создаем неуспешный Response с кодом ошибки и объектом ResponseBody
                val response = Response.error<LoginResponse>(400, errorResponseBody)

                return response
            }

            override suspend fun validateEmail(email: String): Response<ValidateEmailResponse> {
                return Response.success(
                    ValidateEmailResponse (
                        free = Validator.validateEmail(email) && email != userEmail
                    )
                )
            }

            override suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> {
                registerRequest.apply {
                    userEmail = email
                    userPassword = password
                    isBlocked = false
                }
                return Response.success(
                    RegisterResponse (
                        success = true,
                        accessToken = userAccessToken,
                        userId = uid
                    )
                )
            }
        }
    }
    single<PreferencesRepository> { PreferencesRepositoryImpl(context = get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(preferencesRepository = get()) }

    single<SessionRepository> { SessionRepositoryImpl(preferencesRepository = get(), moshi = get()) }
    single<AuthRepository> { AuthRepositoryImpl(authApi = get(), sessionRepository = get()) }

}