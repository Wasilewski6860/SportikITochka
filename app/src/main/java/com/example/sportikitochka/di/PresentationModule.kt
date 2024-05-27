package com.example.sportikitochka.di

import com.example.sportikitochka.presentation.auth.onboarding.OnboardingViewModel
import com.example.sportikitochka.presentation.auth.sign_in.SignInViewModel
import com.example.sportikitochka.presentation.auth.sign_up.SignUpViewModel
import com.example.sportikitochka.presentation.auth.splash.SplashViewModel
import com.example.sportikitochka.presentation.main.all_activities.AllActivitiesViewModel
import com.example.sportikitochka.presentation.main.edit_profile.EditProfileViewModel
import com.example.sportikitochka.presentation.main.main.MainViewModel
import com.example.sportikitochka.presentation.main.payment.PaymentViewModel
import com.example.sportikitochka.presentation.main.profile.ProfileViewModel
import com.example.sportikitochka.presentation.main.rating.RatingViewModel
import com.example.sportikitochka.presentation.main.statistics.StatisticsViewModel
import com.example.sportikitochka.presentation.main.tracking.TrackingViewModel
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
            validateEmailUseCase = get(),
            signUpAdminUseCase = get()
        )
    }

    viewModel<MainViewModel> {
        MainViewModel(
            getUserProfileUseCase = get(),
            addActivityLocalUseCase = get(),
            getAllActivitiesRemoteUseCase = get(),
            getAllActivitiesLocalUseCase = get(),

            getUserDataUseCase = get(),
            saveUserDataUseCase = get(),
            getUserRoleUseCase = get(),
            getAdminProfileUseCase = get()
        )
    }

    viewModel<RatingViewModel> {
        RatingViewModel(
            getProfileUseCase = get(),
            getAllUsersUseCase = get(),
            blockUserUseCase = get(),
            unblockUserUseCase = get(),
            getProfileLocallyUseCase = get(),
            setPremiumUseCase = get(),
            removePremiumUseCase = get(),
            getUserTypeUseCase = get()
        )
    }

    viewModel<TrackingViewModel> {
        TrackingViewModel(
            addActivityRemoteUseCase = get(),
            getUserDataLocallyUseCase = get()
        )
    }

    viewModel<AllActivitiesViewModel> {
        AllActivitiesViewModel(
           addActivityLocalUseCase = get(),
            getAllActivitiesRemoteUseCase = get(),
            getAllActivitiesLocalUseCase = get()
        )
    }

    viewModel<ProfileViewModel> {
        ProfileViewModel(
            getUserRoleUseCase = get(),
            signOutUseCase = get(),
            getProfileUseCase = get(),
            cancelPremiumUseCase = get(),
            saveSessionUseCase = get(),
            getSessionUseCase = get(),
            getAdminProfileUseCase = get()
        )
    }

    viewModel<EditProfileViewModel> {
        EditProfileViewModel(
            getUserRoleUseCase = get(),
            getUserDataLocallyUseCase = get(),
            getUserDataUseCase = get(),
            getAdminDataUseCase = get(),
            changeUserDataUseCase = get(),
            changeAdminDataUseCase = get()
        )
    }

    viewModel<PaymentViewModel> {
        PaymentViewModel(
            buyPremiumUseCase = get(),
            getAllCardsUseCase = get(),
            changeUserTypeUseCase = get(),
            saveSessionUseCase = get(),
            getSessionUseCase = get()
        )
    }

    viewModel<StatisticsViewModel> {
        StatisticsViewModel(
           getUserTypeUseCase = get(),
            getAdminStatisticUseCase = get(),
            getPremiumStatisticUseCase = get()
        )
    }
}