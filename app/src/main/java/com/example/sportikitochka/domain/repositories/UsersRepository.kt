package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.response.users.UserResponse
import retrofit2.Response

interface UsersRepository {
    suspend fun getAllUsesRemote()  : Response<List<UserResponse>>
}