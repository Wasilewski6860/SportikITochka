package com.example.data.repositories

import com.example.domain.repositories.OnboardingRepository
import com.example.domain.repositories.PreferencesRepository


class OnboardingRepositoryImpl(
    private val preferencesRepository: PreferencesRepository
) : OnboardingRepository {
    private companion object {
        const val KEY_THEME_MODE = "PREF_KEY_ONBOARDING"
    }

    override fun getViewed(): Boolean = preferencesRepository.getBoolean(KEY_THEME_MODE)

    override fun setViewed(isViewed: Boolean) {
        preferencesRepository.putBoolean(KEY_THEME_MODE, isViewed)
    }
}