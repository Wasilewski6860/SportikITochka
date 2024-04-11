package com.example.sportikitochka.di

import com.example.sportikitochka.domain.use_cases.auth.IsLoggedUseCase
import com.example.sportikitochka.domain.use_cases.auth.LoginUseCase
import com.example.sportikitochka.domain.use_cases.auth.SaveSessionUseCase
import com.example.sportikitochka.domain.use_cases.onboarding.IsOnboardingViewedUseCase
import com.example.sportikitochka.domain.use_cases.onboarding.SetOnboardingViewedUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<IsOnboardingViewedUseCase> { IsOnboardingViewedUseCase(onboardingRepository = get()) }
    factory<SetOnboardingViewedUseCase> { SetOnboardingViewedUseCase(onboardingRepository = get()) }

    factory<IsLoggedUseCase> { IsLoggedUseCase(sessionRepository = get()) }
    factory<LoginUseCase> { LoginUseCase(authRepository = get()) }
    factory<SaveSessionUseCase> { SaveSessionUseCase(authRepository = get()) }
}