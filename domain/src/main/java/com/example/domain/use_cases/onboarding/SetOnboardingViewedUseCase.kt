package com.example.domain.use_cases.onboarding

import com.example.domain.repositories.OnboardingRepository

class SetOnboardingViewedUseCase(private val onboardingRepository: OnboardingRepository) {

    fun execute(isViewed: Boolean) = onboardingRepository.setViewed(isViewed)
}