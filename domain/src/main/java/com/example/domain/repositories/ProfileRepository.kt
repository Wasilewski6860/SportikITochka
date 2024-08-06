package com.example.domain.repositories

import com.example.domain.coroutines.Response
import com.example.domain.models.AdminProfile
import com.example.domain.models.UserProfile

interface ProfileRepository {

    suspend fun getUserProfileRemote(period: String): Response<UserProfile>
    suspend fun getAdminProfileUseCase(): Response<AdminProfile>
}