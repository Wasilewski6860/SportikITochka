package com.example.sportikitochka.presentation.auth.splash

import androidx.lifecycle.ViewModel
import com.example.sportikitochka.domain.use_cases.onboarding.IsOnboardingViewedUseCase
import com.example.sportikitochka.domain.use_cases.onboarding.SetOnboardingViewedUseCase

class SplashViewModel(
    private val isOnboardingViewedUseCase: IsOnboardingViewedUseCase,
    private val setOnboardingViewedUseCase: SetOnboardingViewedUseCase
) : ViewModel() {

    fun isOnboardingViewed() : Boolean = isOnboardingViewedUseCase.execute()
    fun setOnboardingViewed() {
        setOnboardingViewedUseCase.execute(true)
    }
    fun setOnboardingNotViewed() {
        setOnboardingViewedUseCase.execute(false)
    }

}