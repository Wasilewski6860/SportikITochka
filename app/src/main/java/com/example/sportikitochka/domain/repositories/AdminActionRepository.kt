package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.request.admin_action.AdminActionRequest
import com.example.sportikitochka.data.models.request.admin_action.AdminSetPremiumRequest
import com.example.sportikitochka.data.models.response.admin_action.AdminActionResponse
import retrofit2.Response

interface AdminActionRepository {

    suspend fun adminAction(adminActionRequest: AdminActionRequest): Response<AdminActionResponse>
    suspend fun grantPremium(adminSetPremiumRequest: AdminSetPremiumRequest): Response<AdminActionResponse>
}