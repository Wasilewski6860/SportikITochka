package com.example.sportikitochka.domain.use_cases.onboarding

import com.example.sportikitochka.domain.repositories.OnboardingRepository

class SetOnboardingViewedUseCase(private val onboardingRepository: OnboardingRepository) {

    fun execute(isViewed: Boolean) = onboardingRepository.setViewed(isViewed)
}