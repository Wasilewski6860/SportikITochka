package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.domain.repositories.OnboardingRepository
import com.example.sportikitochka.domain.repositories.PreferencesRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter


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