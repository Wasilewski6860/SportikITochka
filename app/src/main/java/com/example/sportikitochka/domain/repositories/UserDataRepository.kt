package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.request.user_data.ChangeAdminDataRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.user_data.AdminDataResponse
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import retrofit2.Response

interface UserDataRepository {
    suspend fun getAdminData(): Response<AdminDataResponse>
    suspend fun getUserData(): Response<UserDataResponse>
    suspend fun changeUserData(changeDataUserRequest: ChangeDataUserRequest): Response<ChangeDataUserResponse>
    suspend fun changeAdminData(changeAdminDataRequest: ChangeAdminDataRequest): Response<ChangeDataUserResponse>
}