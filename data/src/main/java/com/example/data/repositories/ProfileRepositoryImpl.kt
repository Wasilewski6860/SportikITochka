package com.example.data.repositories

import com.example.data.network.UserProfileApi
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectInputException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NoTokenException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UnknownException
import com.example.domain.coroutines.Response
import com.example.domain.models.Achievement
import com.example.domain.models.AdminProfile
import com.example.domain.models.CreditCard
import com.example.domain.models.Statistics
import com.example.domain.models.UserProfile
import com.example.domain.repositories.ProfileRepository
import com.example.domain.repositories.SessionRepository
import com.example.domain.repositories.UserDataRepository
import retrofit2.Response as RetrofitResponse

class ProfileRepositoryImpl(val userProfileApi: UserProfileApi, val sessionRepository: SessionRepository):
    ProfileRepository {
    override suspend fun getUserProfileRemote(period: String): Response<UserProfile> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = userProfileApi.getUserProfile("Bearer "+token, period)
                if(response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Response.Success(
                            UserProfile(
                                name = data.name,
                                image = data.image,
                                rating = data.rating,
                                statistics = Statistics(
                                    totalDistanceInMeters = data.statisticsResponse.totalDistanceInMeters,
                                    totalTime = data.statisticsResponse.totalTime,
                                    totalCalories = data.statisticsResponse.totalCalories
                                ),
                                achievements = data.achievements.map {
                                    achievementResponse -> Achievement(
                                        achievementId = achievementResponse.achievementId,
                                        achievementName = achievementResponse.achievementName,
                                        achievementImage = achievementResponse.achievementImage,
                                        achievementDistance = achievementResponse.achievementDistance
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

    override suspend fun getAdminProfileUseCase(): Response<AdminProfile> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = userProfileApi.getAdminProfile("Bearer "+token)
                if(response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Response.Success(
                            AdminProfile(
                                name = data.name,
                                image = data.image
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