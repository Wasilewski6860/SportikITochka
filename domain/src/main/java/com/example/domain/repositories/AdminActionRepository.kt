package com.example.domain.repositories

import com.example.domain.coroutines.Response


interface AdminActionRepository {

    suspend fun adminAction(userId: String, action: String): Response<Unit>
    suspend fun grantPremium(userId: String): Response<Unit>
}