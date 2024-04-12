package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.response.users.UserResponse
import com.example.sportikitochka.data.network.UserApi
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.domain.repositories.UsersRepository
import retrofit2.Response

class UsersRepositoryImpl(private val api: UserApi, private val sessionRepository: SessionRepository): UsersRepository {
    override suspend fun getAllUsesRemote(): Response<List<UserResponse>> {
        val token = sessionRepository.getSession()!!.accessToken
        return api.getUsers(token)
    }
}