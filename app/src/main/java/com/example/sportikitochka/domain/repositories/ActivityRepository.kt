package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.response.activities.ActivityResponse
import com.example.sportikitochka.data.models.response.activities.AddActivityResponse
import com.example.sportikitochka.domain.models.SportActivity
import retrofit2.Response

interface ActivityRepository {

    suspend fun addActivity(sportActivity: SportActivity)
    suspend fun getAllActivities(): List<SportActivity>
    suspend fun getActivity(id: Int): SportActivity
    suspend fun clearAll()

    suspend fun addActivityRemote(sportActivity: SportActivity) : Response<AddActivityResponse>
    //    suspend fun deleteActivityRemote(token: String, addActivityRequest: AddActivityRequest) : Response<TaskResponse>
    suspend fun getAllActivitiesRemote()  : Response<List<ActivityResponse>>
}