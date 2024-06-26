package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.activities.AddActivityRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeAdminDataRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.activities.ActivityResponse
import com.example.sportikitochka.data.models.response.activities.AddActivityResponse
import com.example.sportikitochka.data.models.response.user_data.AdminDataResponse
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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