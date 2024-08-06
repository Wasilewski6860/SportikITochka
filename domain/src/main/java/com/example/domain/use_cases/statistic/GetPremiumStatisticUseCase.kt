package com.example.domain.use_cases.statistic

import com.example.domain.repositories.StatisticRepository

class GetPremiumStatisticUseCase(private val statisticRepository: StatisticRepository) {

    suspend fun execute(period: String) = statisticRepository.getPremiumStatistic(
        period
    )
}