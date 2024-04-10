package com.example.sportikitochka.domain.repositories

interface OnboardingRepository {
    fun getViewed(): Boolean
    fun setViewed(isViewed: Boolean)
}