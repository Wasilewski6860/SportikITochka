package com.example.sportikitochka.di

import com.example.sportikitochka.domain.use_cases.activity.AddActivityLocalUseCase
import com.example.sportikitochka.domain.use_cases.activity.AddActivityRemoteUseCase
import com.example.sportikitochka.domain.use_cases.activity.GetAllActivitiesLocalUseCase
import com.example.sportikitochka.domain.use_cases.activity.GetAllActivitiesRemoteUseCase
import com.example.sportikitochka.domain.use_cases.admin_action.BlockUserUseCase
import com.example.sportikitochka.domain.use_cases.admin_action.GrantPremiumUseCase
import com.example.sportikitochka.domain.use_cases.admin_action.RevokePremiumUseCase
import com.example.sportikitochka.domain.use_cases.admin_action.UnblockUserUseCase
import com.example.sportikitochka.domain.use_cases.auth.ChangeUserTypeUseCase
import com.example.sportikitochka.domain.use_cases.auth.GetSessionUseCase
import com.example.sportikitochka.domain.use_cases.auth.GetUserRoleUseCase
import com.example.sportikitochka.domain.use_cases.auth.IsLoggedUseCase
import com.example.sportikitochka.domain.use_cases.auth.LoginUseCase
import com.example.sportikitochka.domain.use_cases.auth.RegisterAdminUseCase
import com.example.sportikitochka.domain.use_cases.auth.RegisterUseCase
import com.example.sportikitochka.domain.use_cases.auth.SaveSessionUseCase
import com.example.sportikitochka.domain.use_cases.auth.SignOutUseCase
import com.example.sportikitochka.domain.use_cases.auth.ValidateEmailUseCase
import com.example.sportikitochka.domain.use_cases.onboarding.IsOnboardingViewedUseCase
import com.example.sportikitochka.domain.use_cases.onboarding.SetOnboardingViewedUseCase
import com.example.sportikitochka.domain.use_cases.payment.BuyPremiumUseCase
import com.example.sportikitochka.domain.use_cases.payment.CancelPremiumUseCase
import com.example.sportikitochka.domain.use_cases.payment.GetAllCardsUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetAdminProfileUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetProfileLocallyUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetProfileUseCase
import com.example.sportikitochka.domain.use_cases.reset_password.ConfirmCodeUseCase
import com.example.sportikitochka.domain.use_cases.reset_password.ResetPasswordUseCase
import com.example.sportikitochka.domain.use_cases.reset_password.SendToEmailUseCase
import com.example.sportikitochka.domain.use_cases.statistic.GetAdminStatisticUseCase
import com.example.sportikitochka.domain.use_cases.statistic.GetPremiumStatisticUseCase
import com.example.sportikitochka.domain.use_cases.user_data.ChangeAdminDataUseCase
import com.example.sportikitochka.domain.use_cases.user_data.ChangeUserDataUseCase
import com.example.sportikitochka.domain.use_cases.user_data.GetAdminDataUseCase
import com.example.sportikitochka.domain.use_cases.user_data.GetUserDataLocallyUseCase
import com.example.sportikitochka.domain.use_cases.user_data.GetUserDataUseCase
import com.example.sportikitochka.domain.use_cases.user_data.SaveUserDataUseCase
import com.example.sportikitochka.domain.use_cases.users.GetAllUsersUseCase
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

    factory<GetUserRoleUseCase> { GetUserRoleUseCase(sessionRepository = get()) }
    factory<GetProfileUseCase> { GetProfileUseCase(profileRepository = get()) }
    factory<GetAllUsersUseCase> { GetAllUsersUseCase(usersRepository = get()) }
    factory<BlockUserUseCase> { BlockUserUseCase(adminActionRepository = get()) }
    factory<UnblockUserUseCase> { UnblockUserUseCase(adminActionRepository = get()) }
    factory<GrantPremiumUseCase> { GrantPremiumUseCase(adminActionRepository = get()) }
    factory<RevokePremiumUseCase> { RevokePremiumUseCase(adminActionRepository = get()) }

    factory<GetUserDataUseCase> { GetUserDataUseCase(userDataRepository = get()) }
    factory<GetAdminDataUseCase> { GetAdminDataUseCase(userDataRepository = get()) }
    factory<ChangeUserDataUseCase> { ChangeUserDataUseCase(userDataRepository = get()) }
    factory<ChangeAdminDataUseCase> { ChangeAdminDataUseCase(userDataRepository = get()) }
    factory<SaveUserDataUseCase> { SaveUserDataUseCase(sessionRepository = get()) }
    factory<GetUserDataLocallyUseCase> { GetUserDataLocallyUseCase(sessionRepository = get()) }

    factory<AddActivityRemoteUseCase> { AddActivityRemoteUseCase(activityRepository = get()) }

    factory<GetProfileLocallyUseCase> { GetProfileLocallyUseCase(profileRepository = get()) }

    factory<SignOutUseCase> { SignOutUseCase(sessionRepository = get(), activityRepository = get(), onboardingRepository = get()) }


    factory<BuyPremiumUseCase> { BuyPremiumUseCase(paymentRepository = get()) }
    factory<GetAllCardsUseCase> { GetAllCardsUseCase(paymentRepository = get()) }

    factory<ChangeUserTypeUseCase> { ChangeUserTypeUseCase(authRepository = get(), sessionRepository = get()) }

    factory<GetAdminStatisticUseCase> { GetAdminStatisticUseCase(statisticRepository = get()) }
    factory<GetPremiumStatisticUseCase> { GetPremiumStatisticUseCase(statisticRepository = get()) }
    factory<RegisterAdminUseCase> { RegisterAdminUseCase(authRepository = get()) }
    factory<GetAdminProfileUseCase> { GetAdminProfileUseCase(profileRepository = get()) }

    factory<GetSessionUseCase> { GetSessionUseCase(sessionRepository = get()) }
    factory<CancelPremiumUseCase> { CancelPremiumUseCase(paymentRepository = get()) }

    factory<ConfirmCodeUseCase> { ConfirmCodeUseCase(resetPasswordRepository = get()) }
    factory<ResetPasswordUseCase> { ResetPasswordUseCase(resetPasswordRepository = get()) }
    factory<SendToEmailUseCase> { SendToEmailUseCase(resetPasswordRepository = get()) }
}