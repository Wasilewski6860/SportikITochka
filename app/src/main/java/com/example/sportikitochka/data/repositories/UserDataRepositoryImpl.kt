package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.user_data.ChangeAdminDataRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.user_data.AdminDataResponse
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import com.example.sportikitochka.data.network.UserDataApi
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.domain.repositories.UserDataRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response

class UserDataRepositoryImpl(val userDataApi: UserDataApi,val sessionRepository: SessionRepository): UserDataRepository {
    override suspend fun getAdminData(): Response<AdminDataResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return userDataApi.getAdminData("Bearer "+token)
    }

    override suspend fun getUserData(): Response<UserDataResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return userDataApi.getUserData("Bearer "+token)
    }

    override suspend fun changeUserData(changeDataUserRequest: ChangeDataUserRequest): Response<ChangeDataUserResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", changeDataUserRequest.name)
            .addFormDataPart("weight", changeDataUserRequest.weight.toString())
            .addFormDataPart("phone", changeDataUserRequest.phone)
            .addFormDataPart("birthday", changeDataUserRequest.birthday)
            .addFormDataPart(
                "image",
                changeDataUserRequest.image.name,
                changeDataUserRequest.image.asRequestBody("image/*".toMediaTypeOrNull())
            )
            .build()
        return userDataApi.changeUserData("Bearer "+token, requestBody)
    }

    override suspend fun changeAdminData(changeAdminDataRequest: ChangeAdminDataRequest): Response<ChangeDataUserResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", changeAdminDataRequest.name)
            .addFormDataPart("phone", changeAdminDataRequest.phone)
            .addFormDataPart("birthday", changeAdminDataRequest.birthday)
            .addFormDataPart(
                "image",
                changeAdminDataRequest.image.name,
                changeAdminDataRequest.image.asRequestBody("image/*".toMediaTypeOrNull())
            )
            .build()
        return userDataApi.changeAdminData("Bearer "+token, requestBody)
    }
}