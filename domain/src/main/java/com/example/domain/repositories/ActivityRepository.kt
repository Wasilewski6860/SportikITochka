package com.example.domain.repositories

import com.example.domain.coroutines.Response
import com.example.domain.models.SportActivity
import java.io.File

interface ActivityRepository {

    suspend fun addActivity(sportActivity: com.example.domain.models.SportActivity)
    suspend fun getAllActivities(): List<SportActivity>
    suspend fun getActivity(id: Int): com.example.domain.models.SportActivity
    suspend fun clearAll()

    suspend fun addActivityRemote(sportActivity: com.example.domain.models.SportActivity, image: File) : Response<Unit>
    //    suspend fun deleteActivityRemote(token: String, addActivityRequest: AddActivityRequest) : Response<TaskResponse>
    suspend fun getAllActivitiesRemote()  : Response<List<SportActivity>>
}