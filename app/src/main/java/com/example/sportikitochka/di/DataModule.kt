package com.example.sportikitochka.di

import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.example.sportikitochka.R
import com.example.sportikitochka.data.db.SportActivitiesDatabase
import com.example.sportikitochka.data.db.SportActivitiesStorage
import com.example.sportikitochka.data.db.impl.SportActivityStorageImpl
import com.example.sportikitochka.data.models.request.activities.AddActivityRequest
import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.response.ErrorResponse
import com.example.sportikitochka.data.models.response.activities.ActivityResponse
import com.example.sportikitochka.data.models.response.activities.AddActivityResponse
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.RegisterResponse
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.auth.ValidateEmailResponse
import com.example.sportikitochka.data.models.response.profile.Achievement
import com.example.sportikitochka.data.models.response.profile.Statistics
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.data.network.ActivitiesApi
import com.example.sportikitochka.data.network.AuthApi
import com.example.sportikitochka.data.network.UserProfileApi
import com.example.sportikitochka.data.repositories.ActivitiesRepositoryImpl
import com.example.sportikitochka.data.repositories.AuthRepositoryImpl
import com.example.sportikitochka.data.repositories.OnboardingRepositoryImpl
import com.example.sportikitochka.data.repositories.PreferencesRepositoryImpl
import com.example.sportikitochka.data.repositories.ProfileRepositoryImpl
import com.example.sportikitochka.data.repositories.SessionRepositoryImpl
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.repositories.ActivityRepository
import com.example.sportikitochka.domain.repositories.AuthRepository
import com.example.sportikitochka.domain.repositories.OnboardingRepository
import com.example.sportikitochka.domain.repositories.PreferencesRepository
import com.example.sportikitochka.domain.repositories.ProfileRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.other.ActivityType
import com.example.sportikitochka.other.TrackingUtility.bitmapToString
import com.example.sportikitochka.other.Validator
import com.example.sportikitochka.other.mapToActivityType
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.koin.dsl.module
import retrofit2.Response
import java.util.Calendar

val dataModule = module {

    var userEmail: String = "trollivanovich6860@gmail.com"
    var userName: String = "Bob"
    var userImage: String = ""
    var userRating: Int = 0
    var userPassword: String = "2143658709"
    var userAccessToken: String = "token"
    var uid: Int = 12345
    var userRole: UserType = UserType. Normal
    var isBlocked: Boolean = false

    var activities: MutableList<SportActivity> = mutableListOf(
        SportActivity(
            id = 0,
            img = "",
            timestamp = Calendar.getInstance().timeInMillis,
            avgSpeed = 14.4F,
            distanceInMeters = 10321,
            timeInMillis = (212141L)*20,
            caloriesBurned = 101,
            activityType = ActivityType.RUNNING
        ),
        SportActivity(
            id = 1,
            img = "",
            timestamp = Calendar.getInstance().timeInMillis - 10000000000,
            avgSpeed = 13.4F,
            distanceInMeters = 10380,
            timeInMillis = 212141L*20,
            caloriesBurned = 100,
            activityType = ActivityType.BYCICLE
        ),
        SportActivity(
            id = 2,
            img = "",
            timestamp = Calendar.getInstance().timeInMillis-14000000000,
            avgSpeed = 15.4F,
            distanceInMeters = 9321,
            timeInMillis = 112141L*20,
            caloriesBurned = 120,
            activityType = ActivityType.SWIMMING
        ),
        SportActivity(
            id = 3,
            img = "",
            timestamp = Calendar.getInstance().timeInMillis - 19000000000,
            avgSpeed = 15.4F,
            distanceInMeters = 8354,
            timeInMillis = 112141L*20,
            caloriesBurned = 132,
            activityType = ActivityType.SWIMMING
        ),
    )


    val userTotalDistance: Long = activities.map { it.distanceInMeters }.sum()
    val userTotalTime: Long = activities.map { it.timeInMillis }.sum()
    val userTotalCalories: Long = activities.map { it.caloriesBurned }.sum()

    single<Moshi> {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single<AuthApi> {
        object : AuthApi {
            override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {

                if (userEmail == loginRequest.email && userPassword == loginRequest.password && !isBlocked) {
                    return Response.success(
                        LoginResponse (
                            accessToken = userAccessToken,
                            role = userRole.toString(),
                            userId = uid,
                            success = true
                        )
                    )
                } else if (userEmail == loginRequest.email && userPassword == loginRequest.password && isBlocked) {
                    // Создаем объект ResponseBody для передачи ошибки
                    val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "USER_BLOCKED")

                    // Создаем неуспешный Response с кодом ошибки и объектом ResponseBody
                    val response = Response.error<LoginResponse>(400, errorResponseBody)

                    return response
                }
                else if (userEmail == loginRequest.email && userPassword != loginRequest.password) {
                    // Создаем объект ResponseBody для передачи ошибки
                    val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "INCORRECT_PASSWORD")

                    // Создаем неуспешный Response с кодом ошибки и объектом ResponseBody
                    val response = Response.error<LoginResponse>(400, errorResponseBody)

                    return response
                }

                // Создаем объект ResponseBody для передачи ошибки
                val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "Error message")

                // Создаем неуспешный Response с кодом ошибки и объектом ResponseBody
                val response = Response.error<LoginResponse>(400, errorResponseBody)

                return response
            }

            override suspend fun validateEmail(email: String): Response<ValidateEmailResponse> {
                return Response.success(
                    ValidateEmailResponse (
                        free = Validator.validateEmail(email) && email != userEmail
                    )
                )
            }

            override suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> {
                registerRequest.apply {
                    userEmail = email
                    userPassword = password
                    isBlocked = false
                }
                return Response.success(
                    RegisterResponse (
                        success = true,
                        accessToken = userAccessToken,
                        userId = uid
                    )
                )
            }
        }
    }


    single<ActivitiesApi> {
        object : ActivitiesApi {
            override suspend fun getAllActivities(token: String): Response<List<ActivityResponse>> {
                Handler().postDelayed({},2000)
                return Response.success(
                    activities.map {
                        ActivityResponse(
                            id = it.id,
                            activityType = it.activityType.activityName,
                            img = it.img,
                            timestamp = it.timestamp,
                            avgSpeed = it.avgSpeed,
                            distanceInMeters = it.distanceInMeters,
                            timeInMillis = it.timeInMillis,
                            caloriesBurned = it.caloriesBurned
                        )
                    }
                )
            }

            override suspend fun addActivity(
                token: String,
                addActivityRequest: AddActivityRequest
            ): Response<AddActivityResponse> {
                activities.add(
                    SportActivity(
                        id = activities.size,
                        activityType = addActivityRequest.activityType.mapToActivityType(),
                        img = addActivityRequest.img,
                        timestamp = addActivityRequest.timestamp,
                        timeInMillis = addActivityRequest.timeInMillis,
                        avgSpeed = addActivityRequest.avgSpeed,
                        distanceInMeters = addActivityRequest.distanceInMeters,
                        caloriesBurned = addActivityRequest.caloriesBurned
                    )
                )
                return Response.success (
                    AddActivityResponse(
                        success = true,
                        activityId = 0
                    )
                )
            }

        }
    }

    single<UserProfileApi> {
        object : UserProfileApi {
            override suspend fun getUserProfile(
                token: String,
                getUserProfileRequest: UserProfileRequest
            ): Response<UserProfileResponse> {
                val drawable = getDrawable(get(), R.drawable.ic_bycicle)
                var bitmap = (drawable as BitmapDrawable).bitmap

                val encodedString = bitmapToString(bitmap)
                userImage = encodedString
                Handler().postDelayed({},1000)
                return Response.success(
                    UserProfileResponse(
                        name = userName,
                        image = userImage,
                        rating = userRating,
                        statistics = Statistics(
                            totalDistanceInMeters = userTotalDistance,
                            totalTime = userTotalTime,
                            totalCalories = userTotalCalories
                        ),
                        achievements = listOf()
                    )
                )
            }

        }
    }


    single<PreferencesRepository> { PreferencesRepositoryImpl(context = get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(preferencesRepository = get()) }

    single<SessionRepository> { SessionRepositoryImpl(preferencesRepository = get(), moshi = get()) }
    single<AuthRepository> { AuthRepositoryImpl(authApi = get(), sessionRepository = get()) }

    single<ProfileRepository> { ProfileRepositoryImpl(userProfileApi = get(), sessionRepository = get()) }
    single<ActivityRepository> { ActivitiesRepositoryImpl(sportActivitiesStorage = get(), api = get(), sessionRepository = get()) }


    single<SportActivitiesStorage> { SportActivityStorageImpl(sportActivitiesDatabase = get()) }
    single<SportActivitiesDatabase> { SportActivitiesDatabase.getDataBase(context = get()) }

}