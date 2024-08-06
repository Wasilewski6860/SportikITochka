package com.example.domain.models

data class User(
    val id : Int,
    val name: String,
    var role: UserType,
    val image: String,
    val place: Int,
    val totalCount: Int,
    val totalDistanse: Float,
    val totalTime: Long,
    val totalCalories: Long,
    val averageDistanse: Float,
    val averageTime: Long,
    val averageCalories: Long,
    val achievements: List<com.example.domain.models.Achievement> = listOf(),
    var isBlocked: Boolean = false
)