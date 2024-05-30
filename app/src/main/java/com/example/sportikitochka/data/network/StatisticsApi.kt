package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.payment.AddCardRequest
import com.example.sportikitochka.data.models.request.statistic.StatisticsRequest
import com.example.sportikitochka.data.models.response.payment.CardOperationResponse
import com.example.sportikitochka.data.models.response.statistic.AdminStatisticsResponse
import com.example.sportikitochka.data.models.response.statistic.PremiumStatisticsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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