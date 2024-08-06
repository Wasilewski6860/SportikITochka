package com.example.data.network

import com.example.data.models.request.admin_action.AdminActionRequest
import com.example.data.models.request.admin_action.AdminSetPremiumRequest
import com.example.data.models.response.admin_action.AdminActionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AdminActionApi {

    @PUT(EndPoints.ADMIN_ACTION)
    suspend fun adminAction(
        @Header("Authorization") token: String,
        @Body adminActionRequest: AdminActionRequest
    ): Response<AdminActionResponse>

    @POST(EndPoints.ADMIN_ACTION)
    suspend fun setPremium(
        @Header("Authorization") token: String,
        @Body adminActionRequest: AdminSetPremiumRequest
    ): Response<AdminActionResponse>

}