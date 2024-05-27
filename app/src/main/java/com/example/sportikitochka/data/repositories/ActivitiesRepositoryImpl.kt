package com.example.sportikitochka.data.repositories

import android.util.Base64
import com.example.sportikitochka.data.db.SportActivitiesStorage
import com.example.sportikitochka.data.db.dto.SportActivityDto
import com.example.sportikitochka.data.models.request.activities.AddActivityRequest
import com.example.sportikitochka.data.models.response.activities.ActivitiesResponse
import com.example.sportikitochka.data.models.response.activities.ActivityResponse
import com.example.sportikitochka.data.models.response.activities.AddActivityResponse
import com.example.sportikitochka.data.network.ActivitiesApi
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.repositories.ActivityRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.other.mapToActivityType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
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
    ): Response<AddActivityResponse> {
//        val decodedImageData = Base64.decode(sportActivity.img, Base64.DEFAULT)
//        val file = File(context.filesDir, "image.png")

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

        return api.addActivity("Bearer "+token, requestBody)
    }


    override suspend fun getAllActivitiesRemote(): Response<List<ActivityResponse>> {
        return api.getAllActivities("Bearer "+token)
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