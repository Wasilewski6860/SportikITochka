package com.example.sportikitochka.domain.use_cases.users

import com.example.sportikitochka.data.models.response.rating.RatingResponse
import com.example.sportikitochka.domain.repositories.ActivityRepository
import com.example.sportikitochka.domain.repositories.UsersRepository
import retrofit2.Response

class GetAllUsersUseCase(private val usersRepository: UsersRepository) {

    suspend fun execute() : Response<RatingResponse> {
        val list = usersRepository.getAllUsesRemote()
        return list
    }
}