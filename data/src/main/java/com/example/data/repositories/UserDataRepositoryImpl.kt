package com.example.data.repositories

import com.example.data.network.UserDataApi
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectInputException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NoTokenException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UnknownException
import com.example.domain.coroutines.Response
import com.example.domain.models.AdminData
import com.example.domain.models.AdminStatistic
import com.example.domain.models.GraphData
import com.example.domain.models.UserData
import com.example.domain.repositories.SessionRepository
import com.example.domain.repositories.UserDataRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserDataRepositoryImpl(val userDataApi: UserDataApi, val sessionRepository: SessionRepository):
    UserDataRepository {
    override suspend fun getAdminData(): Response<AdminData> {
       val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = userDataApi.getAdminData("Bearer "+token)
                if(response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Response.Success(
                            AdminData(
                                name = data.name,
                                image = data.image,
                                phone = data.phone,
                                birthday = data.birthday
                            )
                        )
                    }
                    else Response.Failure(UnknownException())
                }
                else when(response.code()) {
                    401 -> Response.Failure(IncorrectTokenException())
                    403 -> Response.Failure(ForbiddenException())
                    404 -> Response.Failure(NotFoundException())
                    else -> Response.Failure(UnknownException())
                }
            } catch (ex: Exception) {
                Response.Failure(ex)
            }
        }
        else return Response.Failure(NoTokenException())
    }

    override suspend fun getUserData(): Response<UserData> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = userDataApi.getUserData("Bearer "+token)
                if(response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Response.Success(
                            UserData(
                                id = data.id,
                                name = data.name,
                                image = data.image,
                                phone = data.phone,
                                birthday = data.birthday,
                                weight = data.weight
                            )
                        )
                    }
                    else Response.Failure(UnknownException())
                }
                else when(response.code()) {
                    401 -> Response.Failure(IncorrectTokenException())
                    403 -> Response.Failure(ForbiddenException())
                    404 -> Response.Failure(NotFoundException())
                    else -> Response.Failure(UnknownException())
                }
            } catch (ex: Exception) {
                Response.Failure(ex)
            }
        }
        else return Response.Failure(NoTokenException())
    }

    override suspend fun changeUserData(
        name: String,
        image: File,
        weight: Int,
        phone: String,
        birthday: String
    ): Response<Unit> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("weight", weight.toString())
                    .addFormDataPart("phone", phone)
                    .addFormDataPart("birthday", birthday)
                    .addFormDataPart(
                        "image",
                        image.name,
                        image.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                    .build()
                val response = userDataApi.changeUserData("Bearer "+token, requestBody)
                if(response.isSuccessful) {
                    Response.Success(Unit)
                }
                else when(response.code()) {
                    400 -> Response.Failure(IncorrectInputException())
                    401 -> Response.Failure(IncorrectTokenException())
                    403 -> Response.Failure(ForbiddenException())
                    404 -> Response.Failure(NotFoundException())
                    else -> Response.Failure(UnknownException())
                }
            } catch (ex: Exception) {
                Response.Failure(ex)
            }
        }
        else return Response.Failure(NoTokenException())
    }

    override suspend fun changeAdminData(
        name: String,
        image: File,
        phone: String,
        birthday: String
    ): Response<Unit> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("phone", phone)
                    .addFormDataPart("birthday", birthday)
                    .addFormDataPart(
                        "image",
                        image.name,
                        image.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                    .build()
                val response = userDataApi.changeAdminData("Bearer "+token, requestBody)
                if(response.isSuccessful) {
                    Response.Success(Unit)
                }
                else when(response.code()) {
                    400 -> Response.Failure(IncorrectInputException())
                    401 -> Response.Failure(IncorrectTokenException())
                    403 -> Response.Failure(ForbiddenException())
                    404 -> Response.Failure(NotFoundException())
                    else -> Response.Failure(UnknownException())
                }
            } catch (ex: Exception) {
                Response.Failure(ex)
            }
        }
        else return Response.Failure(NoTokenException())
    }
}