package com.example.data.network

import com.example.data.models.response.user_data.AdminDataResponse
import com.example.data.models.response.user_data.ChangeDataUserResponse
import com.example.data.models.response.user_data.UserDataResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface UserDataApi {

    @GET(EndPoints.USER_DATA)
    suspend fun getUserData(
        @Header("Authorization") token: String
    ): Response<UserDataResponse>

    @GET(EndPoints.USER_DATA)
    suspend fun getAdminData(
        @Header("Authorization") token: String
    ): Response<AdminDataResponse>

    @PUT(EndPoints.USER_DATA)
    suspend fun changeUserData(
        @Header("Authorization") token: String,
        @Body userData: RequestBody
    ): Response<ChangeDataUserResponse>

    @PUT(EndPoints.USER_DATA)
    suspend fun changeAdminData(
        @Header("Authorization") token: String,
        @Body userData: RequestBody
    ): Response<ChangeDataUserResponse>

}