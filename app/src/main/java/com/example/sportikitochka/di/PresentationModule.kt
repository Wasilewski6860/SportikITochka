package com.example.sportikitochka.di

import com.example.sportikitochka.presentation.auth.onboarding.OnboardingViewModel
import com.example.sportikitochka.presentation.auth.sign_in.SignInViewModel
import com.example.sportikitochka.presentation.auth.sign_up.SignUpViewModel
import com.example.sportikitochka.presentation.auth.splash.SplashViewModel
import com.example.sportikitochka.presentation.main.main.MainViewModel
import com.example.sportikitochka.presentation.main.rating.RatingViewModel
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
    viewModel<SignInViewModel> {
        SignInViewModel(
            isLoggedUseCase = get(),
            loginUseCase = get(),
            saveSessionUseCase = get(),
            getUserRoleUseCase = get()
        )
    }

    viewModel<SignUpViewModel> {
        SignUpViewModel(
            signUpUseCase = get(),
            validateEmailUseCase = get()
        )
    }

    viewModel<MainViewModel> {
        MainViewModel(
            getUserProfileUseCase = get(),
            addActivityLocalUseCase = get(),
            getAllActivitiesRemoteUseCase = get(),
            getAllActivitiesLocalUseCase = get()
        )
    }

    viewModel<RatingViewModel> {
        RatingViewModel(
            getUserRoleUseCase = get(),
            getProfileUseCase = get(),
            getAllUsersUseCase = get(),
            blockUserUseCase = get(),
            unblockUserUseCase = get(),
            grantPremiumUseCase = get(),
            revokePremiumUseCase = get()
        )
    }
}