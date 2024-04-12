package com.example.sportikitochka.di

import com.example.sportikitochka.domain.use_cases.activity.AddActivityLocalUseCase
import com.example.sportikitochka.domain.use_cases.activity.GetAllActivitiesLocalUseCase
import com.example.sportikitochka.domain.use_cases.activity.GetAllActivitiesRemoteUseCase
import com.example.sportikitochka.domain.use_cases.auth.GetUserRoleUseCase
import com.example.sportikitochka.domain.use_cases.auth.IsLoggedUseCase
import com.example.sportikitochka.domain.use_cases.auth.LoginUseCase
import com.example.sportikitochka.domain.use_cases.auth.RegisterUseCase
import com.example.sportikitochka.domain.use_cases.auth.SaveSessionUseCase
import com.example.sportikitochka.domain.use_cases.auth.ValidateEmailUseCase
import com.example.sportikitochka.domain.use_cases.onboarding.IsOnboardingViewedUseCase
import com.example.sportikitochka.domain.use_cases.onboarding.SetOnboardingViewedUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetProfileUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<IsOnboardingViewedUseCase> { IsOnboardingViewedUseCase(onboardingRepository = get()) }
    factory<SetOnboardingViewedUseCase> { SetOnboardingViewedUseCase(onboardingRepository = get()) }

    factory<IsLoggedUseCase> { IsLoggedUseCase(sessionRepository = get()) }
    factory<LoginUseCase> { LoginUseCase(authRepository = get()) }
    factory<SaveSessionUseCase> { SaveSessionUseCase(authRepository = get()) }
    factory<ValidateEmailUseCase> { ValidateEmailUseCase(authRepository = get()) }
    factory<RegisterUseCase> { RegisterUseCase(authRepository = get()) }

    factory<GetUserRoleUseCase> { GetUserRoleUseCase(sessionRepository = get()) }

    factory<GetUserRoleUseCase> { GetUserRoleUseCase(sessionRepository = get()) }

    factory<GetProfileUseCase> { GetProfileUseCase(profileRepository = get()) }
    factory<AddActivityLocalUseCase> { AddActivityLocalUseCase(activityRepository = get()) }
    factory<GetAllActivitiesRemoteUseCase> { GetAllActivitiesRemoteUseCase(activityRepository = get()) }
    factory<GetAllActivitiesLocalUseCase> { GetAllActivitiesLocalUseCase(activityRepository = get()) }
}