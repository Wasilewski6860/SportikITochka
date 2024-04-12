package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.activities.AddActivityRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.activities.ActivityResponse
import com.example.sportikitochka.data.models.response.activities.AddActivityResponse
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserDataApi {

    @POST(EndPoints.USER_DATA)
    suspend fun getUserData(
        @Header("аuthorization") token: String
    ): Response<UserDataResponse>

    @POST(EndPoints.CHANGE_USER_DATA)
    suspend fun changeUserData(
        @Header("аuthorization") token: String,
        @Body changeDataUserRequest: ChangeDataUserRequest
    ): Response<ChangeDataUserResponse>
}