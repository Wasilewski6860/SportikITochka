package com.example.sportikitochka.di

import com.example.data.db.SportActivitiesDatabase
import com.example.data.db.SportActivitiesStorage
import com.example.data.db.impl.SportActivityStorageImpl
import com.example.data.network.ActivitiesApi
import com.example.data.network.AdminActionApi
import com.example.data.network.AuthApi
import com.example.data.network.EndPoints.BASE_URL
import com.example.data.network.PaymentApi
import com.example.data.network.ResetPasswordApi
import com.example.data.network.StatisticsApi
import com.example.data.network.UserApi
import com.example.data.network.UserDataApi
import com.example.data.network.UserProfileApi
import com.example.data.repositories.ActivitiesRepositoryImpl
import com.example.data.repositories.AdminActionRepositoryImpl
import com.example.data.repositories.AuthRepositoryImpl
import com.example.data.repositories.OnboardingRepositoryImpl
import com.example.data.repositories.PaymentRepositoryImpl
import com.example.data.repositories.PreferencesRepositoryImpl
import com.example.data.repositories.ProfileRepositoryImpl
import com.example.data.repositories.ResetPasswordRepositoryImpl
import com.example.data.repositories.SessionRepositoryImpl
import com.example.data.repositories.StatisticRepositoryImpl
import com.example.data.repositories.UserDataRepositoryImpl
import com.example.data.repositories.UsersRepositoryImpl
import com.example.domain.repositories.ActivityRepository
import com.example.domain.repositories.AdminActionRepository
import com.example.domain.repositories.AuthRepository
import com.example.domain.repositories.OnboardingRepository
import com.example.domain.repositories.PaymentRepository
import com.example.domain.repositories.PreferencesRepository
import com.example.domain.repositories.ProfileRepository
import com.example.domain.repositories.ResetPasswordRepository
import com.example.domain.repositories.SessionRepository
import com.example.domain.repositories.StatisticRepository
import com.example.domain.repositories.UserDataRepository
import com.example.domain.repositories.UsersRepository
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<Moshi> {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<com.example.data.network.AuthApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                ))
            .build()
            .create(com.example.data.network.AuthApi::class.java)
    }

    single<com.example.data.network.UserProfileApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                ))
            .build()
            .create(com.example.data.network.UserProfileApi::class.java)
    }

    single<com.example.data.network.UserDataApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                ))
            .build()
            .create(com.example.data.network.UserDataApi::class.java)
    }

    single<com.example.data.network.UserApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                ))
            .build()
            .create(com.example.data.network.UserApi::class.java)
    }

    single<com.example.data.network.AdminActionApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                ))
            .build()
            .create(com.example.data.network.AdminActionApi::class.java)
    }

    single<com.example.data.network.StatisticsApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                ))
            .build()
            .create(com.example.data.network.StatisticsApi::class.java)
    }

    single<com.example.data.network.UserDataApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                ))
            .build()
            .create(com.example.data.network.UserDataApi::class.java)
    }

    single<com.example.data.network.ActivitiesApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                ))
            .build()
            .create(com.example.data.network.ActivitiesApi::class.java)
    }

    single<com.example.data.network.PaymentApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                ))
            .build()
            .create(com.example.data.network.PaymentApi::class.java)
    }

    single<com.example.data.network.ResetPasswordApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                ))
            .build()
            .create(com.example.data.network.ResetPasswordApi::class.java)
    }

    single<com.example.domain.repositories.PreferencesRepository> {
        com.example.data.repositories.PreferencesRepositoryImpl(
            context = get()
        )
    }
    single<com.example.domain.repositories.OnboardingRepository> {
        com.example.data.repositories.OnboardingRepositoryImpl(
            preferencesRepository = get()
        )
    }

    single<com.example.domain.repositories.SessionRepository> {
        com.example.data.repositories.SessionRepositoryImpl(
            preferencesRepository = get(),
            moshi = get()
        )
    }
    single<com.example.domain.repositories.AuthRepository> {
        com.example.data.repositories.AuthRepositoryImpl(
            authApi = get(),
            sessionRepository = get()
        )
    }

    single<com.example.domain.repositories.ProfileRepository> {
        com.example.data.repositories.ProfileRepositoryImpl(
            userProfileApi = get(),
            sessionRepository = get()
        )
    }
    single<com.example.domain.repositories.ActivityRepository> {
        com.example.data.repositories.ActivitiesRepositoryImpl(
            sportActivitiesStorage = get(),
            api = get(),
            sessionRepository = get()
        )
    }

    single<com.example.domain.repositories.AdminActionRepository> {
        com.example.data.repositories.AdminActionRepositoryImpl(
            adminActionApi = get(),
            sessionRepository = get()
        )
    }
    single<com.example.domain.repositories.UsersRepository> {
        com.example.data.repositories.UsersRepositoryImpl(
            api = get(),
            sessionRepository = get()
        )
    }

    single<com.example.domain.repositories.UserDataRepository> {
        com.example.data.repositories.UserDataRepositoryImpl(
            userDataApi = get(),
            sessionRepository = get()
        )
    }

    single<com.example.domain.repositories.PaymentRepository> {
        com.example.data.repositories.PaymentRepositoryImpl(
            paymentApi = get(),
            sessionRepository = get()
        )
    }

    single<com.example.domain.repositories.StatisticRepository> {
        com.example.data.repositories.StatisticRepositoryImpl(
            statisticsApi = get(),
            sessionRepository = get()
        )
    }
    single<com.example.domain.repositories.ResetPasswordRepository> {
        com.example.data.repositories.ResetPasswordRepositoryImpl(
            resetPasswordApi = get()
        )
    }


    single<com.example.data.db.SportActivitiesStorage> {
        com.example.data.db.impl.SportActivityStorageImpl(
            sportActivitiesDatabase = get()
        )
    }
    single<com.example.data.db.SportActivitiesDatabase> { com.example.data.db.SportActivitiesDatabase.getDataBase(context = get()) }

}