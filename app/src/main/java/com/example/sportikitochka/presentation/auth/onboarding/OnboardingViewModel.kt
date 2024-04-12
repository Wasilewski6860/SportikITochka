package com.example.sportikitochka.presentation.auth.onboarding

import androidx.lifecycle.ViewModel
import com.example.sportikitochka.domain.use_cases.onboarding.SetOnboardingViewedUseCase

class OnboardingViewModel(
    private val setOnboardingViewedUseCase: SetOnboardingViewedUseCase
) : ViewModel() {

    fun setOnboardingViewed() {
        setOnboardingViewedUseCase.execute(true)
    }
    fun setOnboardingNotViewed() {
        setOnboardingViewedUseCase.execute(false)
    }

}