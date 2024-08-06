package com.example.domain.use_cases.users

import com.example.domain.coroutines.Response
import com.example.domain.models.User
import com.example.domain.repositories.UsersRepository

class GetAllUsersUseCase(private val usersRepository: UsersRepository) {

    suspend fun execute() : Response<List<User>> {
        val list = usersRepository.getAllUsesRemote()
        return list
    }
}