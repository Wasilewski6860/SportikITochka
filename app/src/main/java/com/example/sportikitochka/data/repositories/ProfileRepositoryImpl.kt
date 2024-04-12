package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import com.example.sportikitochka.data.network.UserDataApi
import com.example.sportikitochka.data.network.UserProfileApi
import com.example.sportikitochka.domain.repositories.ProfileRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.domain.repositories.UserDataRepository
import retrofit2.Response

class ProfileRepositoryImpl(val userProfileApi: UserProfileApi, val sessionRepository: SessionRepository):
    ProfileRepository {
    override suspend fun getUserProfileRemote(userProfileRequest: UserProfileRequest): Response<UserProfileResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return userProfileApi.getUserProfile(token, userProfileRequest)
    }
}