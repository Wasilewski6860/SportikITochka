package com.example.data.network

import com.example.data.models.response.activities.ActivityResponse
import com.example.data.models.response.activities.AddActivityResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ActivitiesApi {

    @GET(EndPoints.ACTIVITIES)
    suspend fun getAllActivities(
        @Header("Authorization") token: String
    ): Response<List<ActivityResponse>>

    @POST(EndPoints.ADD_ACTIVITY)
    suspend fun addActivity(
        @Header("Authorization") token: String,
        @Body data: RequestBody
    ): Response<AddActivityResponse>
}