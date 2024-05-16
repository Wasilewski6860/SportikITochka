package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.user_data.ChangeAdminDataRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.user_data.AdminDataResponse
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import com.example.sportikitochka.data.network.UserDataApi
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.domain.repositories.UserDataRepository
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
        return userDataApi.changeUserData("Bearer "+token, changeDataUserRequest)
    }

    override suspend fun changeAdminData(changeAdminDataRequest: ChangeAdminDataRequest): Response<ChangeDataUserResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return userDataApi.changeAdminData("Bearer "+token, changeAdminDataRequest)
    }
}