package com.example.domain.repositories

import com.example.domain.coroutines.Response
import com.example.domain.models.AdminData
import com.example.domain.models.UserData
import java.io.File

interface UserDataRepository {
    suspend fun getAdminData(): Response<AdminData>
    suspend fun getUserData(): Response<UserData>
    suspend fun changeUserData(
        name: String,
        image: File,
        weight: Int,
        phone: String,
        birthday: String
    ): Response<Unit>
    suspend fun changeAdminData(
        name: String,
        image: File,
        phone: String,
        birthday: String
    ): Response<Unit>
}