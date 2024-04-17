package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.payment.AddCardRequest
import com.example.sportikitochka.data.models.request.statistic.StatisticsRequest
import com.example.sportikitochka.data.models.response.payment.CardOperationResponse
import com.example.sportikitochka.data.models.response.statistic.AdminStatisticsResponse
import com.example.sportikitochka.data.models.response.statistic.PremiumStatisticsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface StatisticsApi {

    @POST(EndPoints.PREMIUM_STATISTIC)
    suspend fun getPremiumStatistic(
        @Header("аuthorization") token: String,
        @Body statisticsRequest: StatisticsRequest,
    ): Response<PremiumStatisticsResponse>

    @POST(EndPoints.ADMIN_STATISTIC)
    suspend fun getAdminStatistic(
        @Header("аuthorization") token: String,
        @Body statisticsRequest: StatisticsRequest,
    ): Response<AdminStatisticsResponse>
}