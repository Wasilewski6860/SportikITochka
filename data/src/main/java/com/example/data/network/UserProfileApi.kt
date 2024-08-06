package com.example.data.network

import com.example.data.models.response.profile.AdminProfileResponse
import com.example.data.models.response.profile.UserProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UserProfileApi {

    @GET(EndPoints.USER_PROFILE)
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Query("period") period: String
    ): Response<UserProfileResponse>

    @GET(EndPoints.ADMIN_PROFILE)
    suspend fun getAdminProfile(
        @Header("Authorization") token: String
    ): Response<AdminProfileResponse>
}