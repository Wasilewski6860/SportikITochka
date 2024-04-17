package com.example.sportikitochka.data.models.response.statistic

import com.google.gson.annotations.SerializedName

data class AdminStatisticsResponse(
    @SerializedName("total_users") val totalUsers: Int,
    @SerializedName("premium_users") val premiumUsers: Int,
    @SerializedName("graph_data") val graphData: List<GraphData>
)

data class GraphData(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("users_with_premium") val usersWithPremium: Int,
    @SerializedName("users_without_premium") val usersWithoutPremium: Int
)