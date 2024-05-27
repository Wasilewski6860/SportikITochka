package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.rating.RatingResponse
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import com.example.sportikitochka.data.models.response.users.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @GET(EndPoints.USERS)
    suspend fun getUsers(
        @Header("Authorization") token: String
    ): Response<RatingResponse>

}