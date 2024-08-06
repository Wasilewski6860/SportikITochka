package com.example.data.repositories

import com.example.data.network.StatisticsApi
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NoTokenException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UnknownException
import com.example.domain.coroutines.Response
import com.example.domain.models.Achievement
import com.example.domain.models.AdminStatistic
import com.example.domain.models.GraphData
import com.example.domain.models.PremiumStatistic
import com.example.domain.models.SportActivityStatistic
import com.example.domain.models.Statistics
import com.example.domain.models.UserProfile
import com.example.domain.repositories.SessionRepository
import com.example.domain.repositories.StatisticRepository

class StatisticRepositoryImpl(private val sessionRepository: SessionRepository, private val statisticsApi: StatisticsApi):
    StatisticRepository {
    override suspend fun getPremiumStatistic(period: String): Response<PremiumStatistic> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = statisticsApi.getPremiumStatistic("Bearer "+token, period)
                if(response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Response.Success(
                            PremiumStatistic(
                                totalDistanceInMeters = data.totalDistanceInMeters,
                                totalTime = data.totalTime,
                                totalCalories = data.totalCalories,
                                avgSpeed = data.avgSpeed,
                                activities = data.activities.map { sportActivityStatistic ->
                                    SportActivityStatistic(
                                        id = sportActivityStatistic.id,
                                        activityType = sportActivityStatistic.activityType,
                                        date = sportActivityStatistic.date,
                                        avgSpeed = sportActivityStatistic.avgSpeed,
                                        timestamp = sportActivityStatistic.timestamp,
                                        distanceInMeters = sportActivityStatistic.distanceInMeters,
                                        timeInMillis = sportActivityStatistic.timeInMillis,
                                        caloriesBurned = sportActivityStatistic.caloriesBurned
                                    )
                                }
                            )
                        )
                    }
                    else Response.Failure(UnknownException())
                }
                else when(response.code()) {
                    401 -> Response.Failure(IncorrectTokenException())
                    403 -> Response.Failure(ForbiddenException())
                    404 -> Response.Failure(NotFoundException())
                    else -> Response.Failure(UnknownException())
                }
            } catch (ex: Exception) {
                Response.Failure(ex)
            }
        }
        else return Response.Failure(NoTokenException())
    }

    override suspend fun getAdminStatistic(period: String): Response<AdminStatistic> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = statisticsApi.getAdminStatistic("Bearer "+token, period)
                if(response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Response.Success(
                            AdminStatistic(
                                totalUsers = data.totalUsers,
                                premiumUsers = data.premiumUsers,
                                graphData = data.graphData.map { graphData ->
                                    GraphData(
                                        timestamp = graphData.timestamp,
                                        usersWithPremium = graphData.usersWithPremium,
                                        usersWithoutPremium = graphData.usersWithoutPremium
                                    )
                                }
                            )
                        )
                    }
                    else Response.Failure(UnknownException())
                }
                else when(response.code()) {
                    401 -> Response.Failure(IncorrectTokenException())
                    403 -> Response.Failure(ForbiddenException())
                    404 -> Response.Failure(NotFoundException())
                    else -> Response.Failure(UnknownException())
                }
            } catch (ex: Exception) {
                Response.Failure(ex)
            }
        }
        else return Response.Failure(NoTokenException())
    }
}