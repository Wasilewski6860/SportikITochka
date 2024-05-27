package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.statistic.StatisticsRequest
import com.example.sportikitochka.data.models.response.statistic.AdminStatisticsResponse
import com.example.sportikitochka.data.models.response.statistic.PremiumStatisticsResponse
import com.example.sportikitochka.data.network.StatisticsApi
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.domain.repositories.StatisticRepository
import retrofit2.Response

class StatisticRepositoryImpl(private val sessionRepository: SessionRepository, private val statisticsApi: StatisticsApi): StatisticRepository {
    override suspend fun getPremiumStatistic(statisticsRequest: StatisticsRequest): Response<PremiumStatisticsResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return statisticsApi.getPremiumStatistic("Bearer "+token, statisticsRequest.period)
    }

    override suspend fun getAdminStatistic(statisticsRequest: StatisticsRequest): Response<AdminStatisticsResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return statisticsApi.getAdminStatistic("Bearer "+token, statisticsRequest.period)
    }
}