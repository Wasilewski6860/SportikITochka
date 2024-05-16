package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.activities.AddActivityRequest
import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.data.models.response.activities.ActivitiesResponse
import com.example.sportikitochka.data.models.response.activities.ActivityResponse
import com.example.sportikitochka.data.models.response.activities.AddActivityResponse
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.RegisterResponse
import com.example.sportikitochka.data.models.response.auth.ValidateEmailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ActivitiesApi {

    @GET(EndPoints.ACTIVITIES)
    suspend fun getAllActivities(
        @Header("Authorization") token: String
    ): Response<ActivitiesResponse>

    @POST(EndPoints.ADD_ACTIVITY)
    suspend fun addActivity(
        @Header("Authorization") token: String,
        @Body addActivityRequest: AddActivityRequest
    ): Response<AddActivityResponse>
}