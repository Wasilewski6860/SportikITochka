package com.example.sportikitochka.data.models.response.statistic

import com.google.gson.annotations.SerializedName

data class AdminStatisticsResponse(
    @SerializedName("total_users") var totalUsers: Int,
    @SerializedName("premium_users") var premiumUsers: Int,
    @SerializedName("graph_data") var graphData: List<GraphData>
)

data class GraphData(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("users_with_premium") val usersWithPremium: Int,
    @SerializedName("users_without_premium") val usersWithoutPremium: Int
)