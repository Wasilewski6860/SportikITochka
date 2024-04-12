package com.example.sportikitochka.domain.use_cases.users

import com.example.sportikitochka.domain.repositories.ActivityRepository
import com.example.sportikitochka.domain.repositories.UsersRepository

class GetAllUsersUseCase(private val usersRepository: UsersRepository) {

    suspend fun execute()  = usersRepository.getAllUsesRemote()
}