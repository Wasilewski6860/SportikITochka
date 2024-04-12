package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserProfileApi {

    @POST(EndPoints.USER_PROFILE)
    suspend fun getUserProfile(
        @Header("аuthorization") token: String,
        @Body getUserProfileRequest: UserProfileRequest
    ): Response<UserProfileResponse>
}