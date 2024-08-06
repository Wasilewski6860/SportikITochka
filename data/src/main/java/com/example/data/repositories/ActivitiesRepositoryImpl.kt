package com.example.data.repositories

import com.example.data.db.SportActivitiesStorage
import com.example.data.db.dto.SportActivityDto
import com.example.data.models.request.activities.AddActivityRequest
import com.example.data.models.response.activities.ActivityResponse
import com.example.data.models.response.activities.mapToSportActivity
import com.example.data.network.ActivitiesApi
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectInputException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UnknownException
import com.example.domain.coroutines.Response
import com.example.domain.models.SportActivity
import com.example.domain.repositories.ActivityRepository
import com.example.domain.repositories.SessionRepository
import com.example.domain.models.mapToActivityType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response as RetrofitResponse
import java.io.File

class ActivitiesRepositoryImpl(
    private val sportActivitiesStorage: SportActivitiesStorage,
    private val api: ActivitiesApi,
    private val sessionRepository: SessionRepository
    ): ActivityRepository {

    override suspend fun addActivity(sportActivity: SportActivity) {
        sportActivitiesStorage.addActivity(sportActivity.mapToData())
    }

    override suspend fun getAllActivities(): List<SportActivity> = sportActivitiesStorage.getAllActivities().map { sportActivityDto -> sportActivityDto.mapToDomain() }

    override suspend fun getActivity(id: Int): SportActivity = sportActivitiesStorage.getActivity(id).mapToDomain()

    override suspend fun clearAll() = sportActivitiesStorage.clearAll()


    override suspend fun addActivityRemote(
        sportActivity: SportActivity,
        image: File
    ): Response<Unit> {
        val request: AddActivityRequest = sportActivity.mapToRequest()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("activity_type", request.activityType)
            .addFormDataPart("date", request.timestamp)
            .addFormDataPart("avg_speed", request.avgSpeed.toString())
            .addFormDataPart("distance_in_meters", request.distanceInMeters.toString())
            .addFormDataPart("duration", request.timeInMillis.toString())
            .addFormDataPart("calories_burned", request.caloriesBurned.toString())
            .addFormDataPart(
                "image",
                image.name,
                image.asRequestBody("image/*".toMediaTypeOrNull())
            )
            .build()


        return try {
            val response = api.addActivity("Bearer "+token, requestBody)
            if(response.isSuccessful) Response.Success(Unit)
            else when(response.code()) {
                400 -> Response.Failure(IncorrectInputException())
                401 -> Response.Failure(IncorrectTokenException())
                403 -> Response.Failure(NotFoundException())
                else -> Response.Failure(UnknownException())
            }
        } catch (ex: Exception) {
            Response.Failure(ex)
        }
    }


    override suspend fun getAllActivitiesRemote(): Response<List<SportActivity>> {
        return try {
            val response = api.getAllActivities("Bearer "+token)
            if(response.isSuccessful){
                val data = response.body()
                if (data != null) {
                    Response.Success(data.map { response -> response.mapToSportActivity() })
                }
                else Response.Failure(UnknownException())
            }
            else Response.Failure(UnknownException())
        } catch (ex: Exception) {
            Response.Failure(ex)
        }
    }

    val userId: Int
        get() = sessionRepository.getSession()!!.userId

    val token: String
        get() = sessionRepository.getSession()!!.accessToken

    fun SportActivityDto.mapToDomain(): com.example.domain.models.SportActivity =
        com.example.domain.models.SportActivity(
            id = id,
            activityType = activityType.mapToActivityType(),
            img, timestamp, avgSpeedInKMH, distanceInMeters, timeInMillis, caloriesBurned
        )
    fun com.example.domain.models.SportActivity.mapToData(): SportActivityDto = SportActivityDto(id, activityType.activityName, img, timestamp, avgSpeed, distanceInMeters, timeInMillis, caloriesBurned)
    fun com.example.domain.models.SportActivity.mapToRequest(): AddActivityRequest = AddActivityRequest(activityType.toString(), img, timestamp, avgSpeed, distanceInMeters, timeInMillis, caloriesBurned)
}