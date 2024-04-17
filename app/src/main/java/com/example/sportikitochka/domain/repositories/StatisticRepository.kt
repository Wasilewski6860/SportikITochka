package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.request.statistic.StatisticsRequest
import com.example.sportikitochka.data.models.response.statistic.AdminStatisticsResponse
import com.example.sportikitochka.data.models.response.statistic.PremiumStatisticsResponse
import retrofit2.Response

interface StatisticRepository {
    suspend fun getPremiumStatistic(statisticsRequest: StatisticsRequest): Response<PremiumStatisticsResponse>
    suspend fun getAdminStatistic(statisticsRequest: StatisticsRequest): Response<AdminStatisticsResponse>
}