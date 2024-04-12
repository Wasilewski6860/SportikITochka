package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.db.SportActivitiesStorage
import com.example.sportikitochka.data.db.dto.SportActivityDto
import com.example.sportikitochka.data.models.request.activities.AddActivityRequest
import com.example.sportikitochka.data.models.response.activities.ActivityResponse
import com.example.sportikitochka.data.models.response.activities.AddActivityResponse
import com.example.sportikitochka.data.network.ActivitiesApi
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.repositories.ActivityRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.other.mapToActivityType
import retrofit2.Response

class ActivitiesRepositoryImpl(
    private val sportActivitiesStorage: SportActivitiesStorage,
    private val api: ActivitiesApi,
    private val sessionRepository: SessionRepository
    ): ActivityRepository {

    override suspend fun addActivity(sportActivity: SportActivity) = sportActivitiesStorage.addActivity(sportActivity.mapToData())

    override suspend fun getAllActivities(): List<SportActivity> = sportActivitiesStorage.getAllActivities().map { sportActivityDto -> sportActivityDto.mapToDomain() }

    override suspend fun getActivity(id: Int): SportActivity = sportActivitiesStorage.getActivity(id).mapToDomain()

    override suspend fun clearAll() = sportActivitiesStorage.clearAll()


    override suspend fun addActivityRemote(
        sportActivity: SportActivity
    ): Response<AddActivityResponse> {
        return api.addActivity(token, sportActivity.mapToRequest())
    }


    override suspend fun getAllActivitiesRemote(): Response<List<ActivityResponse>> {
        return api.getAllActivities(token)
    }

    val userId: Int
        get() = sessionRepository.getSession()!!.userId

    val token: String
        get() = sessionRepository.getSession()!!.accessToken

    fun SportActivityDto.mapToDomain(): SportActivity = SportActivity(
        id = id,
        activityType = activityType.mapToActivityType(),
        img, timestamp, avgSpeedInKMH, distanceInMeters, timeInMillis, caloriesBurned
    )
    fun SportActivity.mapToData(): SportActivityDto = SportActivityDto(id, activityType.activityName, img, timestamp, avgSpeed, distanceInMeters, timeInMillis, caloriesBurned)
    fun SportActivity.mapToRequest(): AddActivityRequest = AddActivityRequest(activityType.toString(), img, timestamp, avgSpeed, distanceInMeters, timeInMillis, caloriesBurned)
}