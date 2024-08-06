package com.example.domain.repositories

import com.example.domain.coroutines.Response
import com.example.domain.models.User

interface UsersRepository {
    suspend fun getAllUsesRemote()  : Response<List<User>>
}