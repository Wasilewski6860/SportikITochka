package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import retrofit2.Response

interface ProfileRepository {

    suspend fun getUserProfileRemote(userProfileRequest: UserProfileRequest): Response<UserProfileResponse>
}