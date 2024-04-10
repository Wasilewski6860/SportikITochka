package com.example.sportikitochka.domain.use_cases.onboarding

import com.example.sportikitochka.domain.repositories.OnboardingRepository

class IsOnboardingViewedUseCase(private val onboardingRepository: OnboardingRepository) {

    fun execute() = onboardingRepository.getViewed()
}