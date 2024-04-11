package com.example.sportikitochka.di

import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.response.ErrorResponse
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.network.AuthApi
import com.example.sportikitochka.data.repositories.AuthRepositoryImpl
import com.example.sportikitochka.data.repositories.OnboardingRepositoryImpl
import com.example.sportikitochka.data.repositories.PreferencesRepositoryImpl
import com.example.sportikitochka.data.repositories.SessionRepositoryImpl
import com.example.sportikitochka.domain.repositories.AuthRepository
import com.example.sportikitochka.domain.repositories.OnboardingRepository
import com.example.sportikitochka.domain.repositories.PreferencesRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.koin.dsl.module
import retrofit2.Response

val dataModule = module {

    val email: String = "trollivanovich6860@gmail.com"
    val password: String = "2143658709"
    val accessToken: String = "token"
    val uid: Int = 12345
    var role: UserType = UserType. Normal
    var isBlocked: Boolean = false

    single<Moshi> {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single<AuthApi> {
        object : AuthApi {
            override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {

                if (email == loginRequest.email && password == loginRequest.password && !isBlocked) {
                    return Response.success(
                        LoginResponse (
                            accessToken = accessToken,
                            role = role.toString(),
                            userId = uid,
                            success = true
                        )
                    )
                } else if (email == loginRequest.email && password == loginRequest.password && isBlocked) {
                    // Создаем объект ResponseBody для передачи ошибки
                    val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "USER_BLOCKED")

                    // Создаем неуспешный Response с кодом ошибки и объектом ResponseBody
                    val response = Response.error<LoginResponse>(400, errorResponseBody)

                    return response
                }
                else if (email == loginRequest.email && password != loginRequest.password) {
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
        }
    }
    single<PreferencesRepository> { PreferencesRepositoryImpl(context = get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(preferencesRepository = get()) }

    single<SessionRepository> { SessionRepositoryImpl(preferencesRepository = get(), moshi = get()) }
    single<AuthRepository> { AuthRepositoryImpl(authApi = get(), sessionRepository = get()) }

}