package com.example.domain.models

data class AdminStatistic(
    var totalUsers: Int,
    var premiumUsers: Int,
    var graphData: List<GraphData>
)

data class GraphData(
    val timestamp: Long,
    val usersWithPremium: Int,
    val usersWithoutPremium: Int
)