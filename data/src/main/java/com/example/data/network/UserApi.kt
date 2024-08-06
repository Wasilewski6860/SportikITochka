package com.example.data.network

import com.example.data.models.response.rating.RatingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {

    @GET(EndPoints.USERS)
    suspend fun getUsers(
        @Header("Authorization") token: String
    ): Response<RatingResponse>

}