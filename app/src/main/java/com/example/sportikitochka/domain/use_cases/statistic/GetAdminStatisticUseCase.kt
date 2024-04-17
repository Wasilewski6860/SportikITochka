package com.example.sportikitochka.domain.use_cases.statistic

import com.example.sportikitochka.data.models.request.profile.ProfilePeriod
import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.request.statistic.StatisticsRequest
import com.example.sportikitochka.domain.repositories.ProfileRepository
import com.example.sportikitochka.domain.repositories.StatisticRepository

class GetAdminStatisticUseCase(private val statisticRepository: StatisticRepository) {

    suspend fun execute(period: ProfilePeriod) = statisticRepository.getAdminStatistic(
        StatisticsRequest(period.toString())
    )
}