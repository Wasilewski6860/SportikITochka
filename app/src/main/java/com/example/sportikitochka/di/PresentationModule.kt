package com.example.sportikitochka.di

import com.example.sportikitochka.presentation.onboarding.OnboardingViewModel
import com.example.sportikitochka.presentation.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel<OnboardingViewModel> {
        OnboardingViewModel(
            setOnboardingViewedUseCase = get()
        )
    }
    viewModel<SplashViewModel> {
        SplashViewModel(
            setOnboardingViewedUseCase = get(),
            isOnboardingViewedUseCase = get()
        )
    }
}