package com.example.data.network

import com.example.data.models.response.statistic.AdminStatisticsResponse
import com.example.data.models.response.statistic.PremiumStatisticsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StatisticsApi {

    @GET(EndPoints.PREMIUM_STATISTIC)
    suspend fun getPremiumStatistic(
        @Header("Authorization") token: String,
        @Query("period") statisticsRequest: String,
    ): Response<PremiumStatisticsResponse>

    @GET(EndPoints.ADMIN_STATISTIC)
    suspend fun getAdminStatistic(
        @Header("Authorization") token: String,
        @Query("period") statisticsRequest: String,
    ): Response<AdminStatisticsResponse>
}