package com.example.sportikitochka.di

import com.example.sportikitochka.domain.use_cases.onboarding.IsOnboardingViewedUseCase
import com.example.sportikitochka.domain.use_cases.onboarding.SetOnboardingViewedUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<IsOnboardingViewedUseCase> { IsOnboardingViewedUseCase(onboardingRepository = get()) }
    factory<SetOnboardingViewedUseCase> { SetOnboardingViewedUseCase(onboardingRepository = get()) }
}