package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.admin_action.AdminActionRequest
import com.example.sportikitochka.data.models.response.admin_action.AdminActionResponse
import com.example.sportikitochka.data.network.AdminActionApi
import com.example.sportikitochka.domain.repositories.AdminActionRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import retrofit2.Response

class AdminActionRepositoryImpl(val adminActionApi: AdminActionApi, val sessionRepository: SessionRepository): AdminActionRepository {
    override suspend fun adminAction(adminActionRequest: AdminActionRequest): Response<AdminActionResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return adminActionApi.adminAction(token, adminActionRequest)
    }
}