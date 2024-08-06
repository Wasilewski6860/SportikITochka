package com.example.data.repositories

import com.example.data.network.UserApi
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectInputException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NoTokenException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UnknownException
import com.example.domain.coroutines.Response
import com.example.domain.models.Achievement
import com.example.domain.models.CreditCard
import com.example.domain.models.User
import com.example.domain.models.UserType.Normal.toUserType
import com.example.domain.repositories.SessionRepository
import com.example.domain.repositories.UsersRepository

class UsersRepositoryImpl(private val api: UserApi, private val sessionRepository: SessionRepository):
    UsersRepository {
    override suspend fun getAllUsesRemote(): Response<List<User>> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = api.getUsers("Bearer "+token)
                if(response.isSuccessful){
                    val data = response.body()
                    if (data != null) {
                        Response.Success(data.user.map { response -> User(
                            id = response.id,
                            name = response.name,
                            role = response.role.toUserType(),
                            image = response.image,
                            place = response.rating,
                            totalCalories = response.totalCalories,
                            totalCount = response.totalCount,
                            totalTime = response.totalTime,
                            totalDistanse = response.totalDistanse,
                            averageTime = response.averageTime,
                            averageDistanse = response.averageDistanse,
                            averageCalories = response.averageCalories,
                            isBlocked = response.isBlocked,
                            achievements = response.achievements.map { achievementResponse ->
                                Achievement(
                                    achievementName = achievementResponse.achievementName,
                                    achievementImage = achievementResponse.achievementImage,
                                    achievementId = achievementResponse.achievementId,
                                    achievementDistance = achievementResponse.achievementDistance
                                )
                            }
                        ) })
                    }
                    else Response.Failure(UnknownException())
                }
                else when(response.code()) {
                    401 -> Response.Failure(IncorrectTokenException())
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