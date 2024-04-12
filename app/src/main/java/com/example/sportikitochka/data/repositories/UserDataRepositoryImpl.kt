package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import com.example.sportikitochka.data.network.UserDataApi
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.domain.repositories.UserDataRepository
import retrofit2.Response

class UserDataRepositoryImpl(val userDataApi: UserDataApi,val sessionRepository: SessionRepository): UserDataRepository {
    override suspend fun getUserData(): Response<UserDataResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return userDataApi.getUserData(token)
    }

    override suspend fun changeUserData(changeDataUserRequest: ChangeDataUserRequest): Response<ChangeDataUserResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return userDataApi.changeUserData(token, changeDataUserRequest)
    }
}