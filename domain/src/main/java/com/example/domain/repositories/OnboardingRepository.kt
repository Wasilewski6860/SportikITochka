package com.example.domain.repositories

interface OnboardingRepository {
    fun getViewed(): Boolean
    fun setViewed(isViewed: Boolean)
}