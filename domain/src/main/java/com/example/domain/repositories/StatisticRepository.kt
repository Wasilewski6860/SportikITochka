package com.example.domain.repositories

import com.example.domain.coroutines.Response
import com.example.domain.models.AdminStatistic
import com.example.domain.models.PremiumStatistic


interface StatisticRepository {
    suspend fun getPremiumStatistic(period: String): Response<PremiumStatistic>
    suspend fun getAdminStatistic(period: String): Response<AdminStatistic>
}