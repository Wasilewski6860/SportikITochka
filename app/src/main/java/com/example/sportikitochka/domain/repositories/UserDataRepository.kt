package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import retrofit2.Response

interface UserDataRepository {
    suspend fun getUserData(): Response<UserDataResponse>
    suspend fun changeUserData(changeDataUserRequest: ChangeDataUserRequest): Response<ChangeDataUserResponse>
}