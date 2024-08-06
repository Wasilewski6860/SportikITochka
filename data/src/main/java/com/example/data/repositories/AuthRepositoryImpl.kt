package com.example.data.repositories

import com.example.data.models.request.admin_action.AdminActionRequest
import com.example.data.models.request.auth.LoginRequest
import com.example.data.models.response.activities.mapToSportActivity
import com.example.data.network.AuthApi
import com.example.data.network.error.AlreadyUsedException
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectInputException
import com.example.data.network.error.IncorrectPasswordException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UnknownException
import com.example.data.network.error.UserBlockedException
import com.example.domain.coroutines.Response
import com.example.domain.models.UserSession
import com.example.domain.repositories.AuthRepository
import com.example.domain.repositories.SessionRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response as RetrofitResponse
import java.io.File

class AuthRepositoryImpl(private val authApi: AuthApi, private val sessionRepository: SessionRepository) :
    AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Response<UserSession> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if(response.isSuccessful){
                val data = response.body()
                if (data != null) {
                    Response.Success(UserSession(data.accessToken, data.role, data.userId))
                }
                else Response.Failure(UnknownException())
            }
            else when(response.code()) {
                400 -> Response.Failure(IncorrectInputException())
                401 -> Response.Failure(IncorrectPasswordException())
                403, 409 -> Response.Failure(UserBlockedException())
                404 -> Response.Failure(NotFoundException())
                else -> Response.Failure(UnknownException())
            }
        } catch (ex: Exception) {
            Response.Failure(ex)
        }
    }

    override suspend fun validateEmail(email: String): Response<Unit> {
        return try {
            val response = authApi.validateEmail(email)
            if(response.isSuccessful){
                Response.Success(Unit)
            }
            else when(response.code()) {
                400 -> Response.Failure(IncorrectInputException())
                401 -> Response.Failure(IncorrectPasswordException())
                403, 409 -> Response.Failure(UserBlockedException())
                404 -> Response.Failure(NotFoundException())
                else -> Response.Failure(UnknownException())
            }
        } catch (ex: Exception) {
            Response.Failure(ex)
        }
    }
    override suspend fun register(
        email: String,
        name: String,
        weight: Int,
        image: File,
        phone: String,
        birthday: String,
        password: String
    ): Response<Unit> {
        return try {
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("weight", weight.toString())
                .addFormDataPart("phone", phone)
                .addFormDataPart("birthday", birthday)
                .addFormDataPart("password_hash", password)
                .addFormDataPart(
                    "avatar",
                    image.name,
                    image.asRequestBody("image/*".toMediaTypeOrNull())
                )
                .build()
            val response = authApi.register(email, requestBody)
            if(response.isSuccessful){
                Response.Success(Unit)
            }
            else when(response.code()) {
                400 -> Response.Failure(IncorrectInputException())
                409 -> Response.Failure(AlreadyUsedException())
                else -> Response.Failure(UnknownException())
            }
        } catch (ex: Exception) {
            Response.Failure(ex)
        }
    }

    override suspend fun register(
        email: String,
        name: String,
        image: File,
        phone: String,
        birthday: String,
        password: String
    ): Response<Unit> {
        return try {
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("phone", phone)
                .addFormDataPart("birthday", birthday)
                .addFormDataPart("password_hash", password)
                .addFormDataPart(
                    "avatar",
                    image.name,
                    image.asRequestBody("image/*".toMediaTypeOrNull())
                )
                .build()

            val response = authApi.register(email, requestBody)
            if(response.isSuccessful){
                Response.Success(Unit)
            }
            else when(response.code()) {
                400 -> Response.Failure(IncorrectInputException())
                409 -> Response.Failure(AlreadyUsedException())
                else -> Response.Failure(UnknownException())
            }
        } catch (ex: Exception) {
            Response.Failure(ex)
        }
    }

    override fun saveSession(loginResponse: UserSession): Boolean {
        sessionRepository.saveSession(loginResponse)
        return true
    }


}
