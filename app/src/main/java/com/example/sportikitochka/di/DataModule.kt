package com.example.sportikitochka.di

import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.example.sportikitochka.R
import com.example.sportikitochka.data.db.SportActivitiesDatabase
import com.example.sportikitochka.data.db.SportActivitiesStorage
import com.example.sportikitochka.data.db.impl.SportActivityStorageImpl
import com.example.sportikitochka.data.models.request.activities.AddActivityRequest
import com.example.sportikitochka.data.models.request.admin_action.AdminAction
import com.example.sportikitochka.data.models.request.admin_action.AdminActionRequest
import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.data.models.request.payment.AddCardRequest
import com.example.sportikitochka.data.models.request.payment.BuyPremiumRequest
import com.example.sportikitochka.data.models.request.payment.DeleteCardRequest
import com.example.sportikitochka.data.models.request.payment.EditCardRequest
import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.request.statistic.StatisticsRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeAdminDataRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.AchievementResponse
import com.example.sportikitochka.data.models.response.ErrorResponse
import com.example.sportikitochka.data.models.response.activities.ActivityResponse
import com.example.sportikitochka.data.models.response.activities.AddActivityResponse
import com.example.sportikitochka.data.models.response.admin_action.AdminActionResponse
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.RegisterResponse
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.auth.ValidateEmailResponse
import com.example.sportikitochka.data.models.response.payment.BuyPremiumResponse
import com.example.sportikitochka.data.models.response.payment.CancelPremiumResponse
import com.example.sportikitochka.data.models.response.payment.CardOperationResponse
import com.example.sportikitochka.data.models.response.payment.CreditCardResponse
import com.example.sportikitochka.data.models.response.profile.Statistics
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.data.models.response.statistic.AdminStatisticsResponse
import com.example.sportikitochka.data.models.response.statistic.GraphData
import com.example.sportikitochka.data.models.response.statistic.PremiumStatisticsResponse
import com.example.sportikitochka.data.models.response.statistic.SportActivityStatistic
import com.example.sportikitochka.data.models.response.user_data.AdminDataResponse
import com.example.sportikitochka.data.models.response.user_data.ChangeDataUserResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import com.example.sportikitochka.data.models.response.users.UserResponse
import com.example.sportikitochka.data.network.ActivitiesApi
import com.example.sportikitochka.data.network.AdminActionApi
import com.example.sportikitochka.data.network.AuthApi
import com.example.sportikitochka.data.network.PaymentApi
import com.example.sportikitochka.data.network.StatisticsApi
import com.example.sportikitochka.data.network.UserApi
import com.example.sportikitochka.data.network.UserDataApi
import com.example.sportikitochka.data.network.UserProfileApi
import com.example.sportikitochka.data.repositories.ActivitiesRepositoryImpl
import com.example.sportikitochka.data.repositories.AdminActionRepositoryImpl
import com.example.sportikitochka.data.repositories.AuthRepositoryImpl
import com.example.sportikitochka.data.repositories.OnboardingRepositoryImpl
import com.example.sportikitochka.data.repositories.PaymentRepositoryImpl
import com.example.sportikitochka.data.repositories.PreferencesRepositoryImpl
import com.example.sportikitochka.data.repositories.ProfileRepositoryImpl
import com.example.sportikitochka.data.repositories.SessionRepositoryImpl
import com.example.sportikitochka.data.repositories.StatisticRepositoryImpl
import com.example.sportikitochka.data.repositories.UserDataRepositoryImpl
import com.example.sportikitochka.data.repositories.UsersRepositoryImpl
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.models.User
import com.example.sportikitochka.domain.repositories.ActivityRepository
import com.example.sportikitochka.domain.repositories.AdminActionRepository
import com.example.sportikitochka.domain.repositories.AuthRepository
import com.example.sportikitochka.domain.repositories.OnboardingRepository
import com.example.sportikitochka.domain.repositories.PaymentRepository
import com.example.sportikitochka.domain.repositories.PreferencesRepository
import com.example.sportikitochka.domain.repositories.ProfileRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import com.example.sportikitochka.domain.repositories.StatisticRepository
import com.example.sportikitochka.domain.repositories.UserDataRepository
import com.example.sportikitochka.domain.repositories.UsersRepository
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
    var userWeight: Float = 81F
    var userRating: Int = 0
    var userBirthday: Long = 0
    var userPhone: String = "+7 (980) 343-86-78"
    var userPassword: String = "2143658709"
    var userAccessToken: String = "token"
    var uid: Int = 12345
    var userRole: UserType = UserType. Admin
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

    val cards = mutableListOf<CreditCardResponse>(
        CreditCardResponse(
            "ARTIM BARYSHEV",
            "2201700161794921",
            11,
            16,
            1234
        ),
        CreditCardResponse(
            "ARTIM IVANOV",
            "2201700161794775",
            11,
            12,
            1294
        ),
        CreditCardResponse(
            "ARTIM PETROV",
            "2201700161794777",
            4,
            11,
            1134
        ),
        CreditCardResponse(
            "ARTIM HANIN",
            "2101775161794888",
            11,
            11,
            1244
        )
    )
    val users = listOf<User>(
        User(
            id = 0,
            name = "User123412",
            role = UserType.Premium,
            image = "",
            place = 1,
            totalCount = 11,
            totalDistanse = 15400F,
            totalTime = 112414141L,
            totalCalories = 1442L,
            averageTime = 1000,
            averageDistanse = 100F,
            averageCalories = 100L,
            isBlocked = false
        ),
        User(
            id = 1,
            name = "U1213",
            role = UserType.Normal,
            image = "",
            place = 2,
            totalCount = 15,
            totalDistanse = 13400F,
            totalTime = 1122414141L,
            totalCalories = 1441L,
            averageTime = 1000,
            averageDistanse = 100F,
            averageCalories = 100L,
            isBlocked = false
        ),
        User(
            id = 2,
            name = "Andrew",
            role = UserType.Normal,
            image = "",
            place = 3,
            totalCount = 9,
            totalDistanse = 14400F,
            totalTime = 112471141L,
            totalCalories = 1541L,
            averageTime = 1000,
            averageDistanse = 100F,
            averageCalories = 100L,
            isBlocked = true
        ),
        User(
            id = 3,
            name = "Test User",
            role = UserType.Premium,
            image = "",
            place = 4,
            totalCount = 10,
            totalDistanse = 15600F,
            totalTime = 442414141L,
            totalCalories = 1332L,
            averageTime = 1000,
            averageDistanse = 100F,
            averageCalories = 100L,
            isBlocked = true
        )
    )

    var premiumStatistic =  PremiumStatisticsResponse(
        totalDistanceInMeters = 100000,
        totalTime = 442414141L,
        totalCalories = 1332L,
        avgSpeed = 10F,
        activities = mutableListOf(
            SportActivityStatistic(
                id = 0,
                timestamp = Calendar.getInstance().timeInMillis,
                avgSpeed = 14.4F,
                distanceInMeters = 10321,
                timeInMillis = (212141L)*20,
                caloriesBurned = 101,
                activityType = ActivityType.RUNNING.toString()
            ),
            SportActivityStatistic(
                id = 1,
                timestamp = Calendar.getInstance().timeInMillis - 10000000000,
                avgSpeed = 13.4F,
                distanceInMeters = 10380,
                timeInMillis = 212141L*20,
                caloriesBurned = 100,
                activityType = ActivityType.BYCICLE.toString()
            ),
            SportActivityStatistic(
                id = 2,
                timestamp = Calendar.getInstance().timeInMillis-14000000000,
                avgSpeed = 15.4F,
                distanceInMeters = 9321,
                timeInMillis = 112141L*20,
                caloriesBurned = 120,
                activityType = ActivityType.SWIMMING.toString()
            ),
            SportActivityStatistic(
                id = 3,
                timestamp = Calendar.getInstance().timeInMillis - 19000000000,
                avgSpeed = 15.4F,
                distanceInMeters = 8354,
                timeInMillis = 112141L*20,
                caloriesBurned = 132,
                activityType = ActivityType.SWIMMING.toString()
            )
        )
    )

    var adminStatisticResponse = AdminStatisticsResponse(
        totalUsers = 100,
        premiumUsers = 20,
        graphData = listOf(
            GraphData(
                timestamp = Calendar.getInstance().timeInMillis - 19000000000,
                usersWithPremium = 20,
                usersWithoutPremium = 2
            ),
            GraphData(
                timestamp = Calendar.getInstance().timeInMillis - 14000000000,
                usersWithPremium = 29,
                usersWithoutPremium = 12
            ),
            GraphData(
                timestamp = Calendar.getInstance().timeInMillis - 10000000000,
                usersWithPremium = 68,
                usersWithoutPremium = 17
            ),
            GraphData(
                timestamp = Calendar.getInstance().timeInMillis,
                usersWithPremium = 80,
                usersWithoutPremium = 20
            )
        )
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
                    userName = name
                    userImage = image
                    userWeight = weight.toFloat()
                    userPhone = phone
                    userBirthday = birthday
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
//                val drawable = getDrawable(get(), R.drawable.ic_bycicle)
//                var bitmap = (drawable as BitmapDrawable).bitmap
//
//                val encodedString = bitmapToString(bitmap)
//                if (userImage==""){
//                    userImage = encodedString
//                }

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

    single<AdminActionApi> {
        object : AdminActionApi {
            override suspend fun adminAction(
                token: String,
                adminActionRequest: AdminActionRequest
            ): Response<AdminActionResponse> {
                when(adminActionRequest.action) {
                    AdminAction.GRANT_PREMIUM.action -> {
                        for (user in users) {
                            if (user.id == adminActionRequest.user_id.toInt()) {
                                user.role = UserType.Premium
                                return Response.success(
                                    AdminActionResponse(
                                        success = true,
                                        action = AdminAction.GRANT_PREMIUM.action,
                                        timestamp = 0L
                                    )
                                )
                            }
                        }
                    }
                    AdminAction.REVOKE_PREMIUM.action -> {
                        for (user in users) {
                            if (user.id == adminActionRequest.user_id.toInt()) {
                                user.role = UserType.Normal
                                return Response.success(
                                    AdminActionResponse(
                                        success = true,
                                        action = AdminAction.REVOKE_PREMIUM.action,
                                        timestamp = 0L
                                    )
                                )
                            }
                        }
                    }
                    AdminAction.BLOCK.action -> {
                        for (user in users) {
                            if (user.id == adminActionRequest.user_id.toInt()) {
                                user.isBlocked = true
                                Response.success(
                                    AdminActionResponse(
                                        success = true,
                                        action = AdminAction.BLOCK.action,
                                        timestamp = 0L
                                    )
                                )
                            }
                        }
                    }
                    AdminAction.UNBLOCK.action -> {
                        for (user in users) {
                            if (user.id == adminActionRequest.user_id.toInt()) {
                                user.isBlocked = false
                                return Response.success(
                                    AdminActionResponse(
                                        success = true,
                                        action = AdminAction.UNBLOCK.action,
                                        timestamp = 0L
                                    )
                                )
                            }
                        }
                    }

                    else -> {
                        // Создаем объект ResponseBody для передачи ошибки
                        val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "Error message")
                        // Создаем неуспешный Response с кодом ошибки и объектом ResponseBody
                        val response = Response.error<AdminActionResponse>(400, errorResponseBody)
                        return response
                    }
                }

                // Создаем объект ResponseBody для передачи ошибки
                val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "Error message")
                // Создаем неуспешный Response с кодом ошибки и объектом ResponseBody
                val response = Response.error<AdminActionResponse>(400, errorResponseBody)
                return response
            }

        }
    }

    single<UserApi> {
        object :UserApi {
            override suspend fun getUsers(token: String): Response<List<UserResponse>> {
                Handler().postDelayed({},3000)
                return Response.success(
                    users.map {
                            user ->
                        UserResponse(
                            id = user.id,
                            name = user.name,
                            role = user.role.toString(),
                            image = user.image,
                            rating = user.place,
                            totalCount = user.totalCount,
                            totalDistanse = user.totalDistanse,
                            totalTime = user.totalTime,
                            totalCalories = user.totalCalories,
                            averageDistanse = user.averageDistanse,
                            averageCalories = user.averageCalories,
                            averageTime = user.averageTime,
                            achievements = user.achievements.map {
                                    it -> AchievementResponse(
                                it.achievementId,
                                it.achievementName,
                                it.achievementImage,
                                it.achievementDistance
                            )
                            },
                            isBlocked = user.isBlocked,
                            averageSpeed = user.averageDistanse/user.averageTime//TODO
                        )
                    }
                )
            }

        }
    }

    single<UserDataApi> {
        object: UserDataApi {
            override suspend fun getUserData(token: String): Response<UserDataResponse> {
                return Response.success(
                    UserDataResponse(
                        name = userName,
                        image = userImage,
                        weight = userWeight,
                        phone = userPhone,
                        birthday = userBirthday
                    )
                )
            }

            override suspend fun getAdminData(token: String): Response<AdminDataResponse> {
                return Response.success(
                    AdminDataResponse(
                        name = userName,
                        image = userImage,
                        phone = userPhone,
                        birthday = userBirthday
                    )
                )
            }

            override suspend fun changeUserData(
                token: String,
                changeDataUserRequest: ChangeDataUserRequest
            ): Response<ChangeDataUserResponse> {
                userName = changeDataUserRequest.name
                userImage = changeDataUserRequest.image
                userWeight = changeDataUserRequest.weight
                userPhone = changeDataUserRequest.phone
                userBirthday = changeDataUserRequest.birthday
                return Response.success(
                    ChangeDataUserResponse(
                        success = true
                    )
                )
            }

            override suspend fun changeAdminData(
                token: String,
                changeAdminDataRequest: ChangeAdminDataRequest
            ): Response<ChangeDataUserResponse> {
                userName = changeAdminDataRequest.name
                userImage = changeAdminDataRequest.image
                userPhone = changeAdminDataRequest.phone
                userBirthday = changeAdminDataRequest.birthday
                return Response.success(
                    ChangeDataUserResponse(
                        success = true
                    )
                )
            }

        }
    }

    single<PaymentApi> {
        object : PaymentApi {
            override suspend fun getAllCards(token: String): Response<List<CreditCardResponse>> {
                return Response.success(
                    cards
                )
            }

            override suspend fun addCard(
                token: String,
                addCardRequest: AddCardRequest
            ): Response<CardOperationResponse> {
                cards.add(CreditCardResponse(addCardRequest.cardName, addCardRequest.cardNumber, addCardRequest.month, addCardRequest.year, addCardRequest.cvv))
                return Response.success(
                    CardOperationResponse(true, 0L)
                )
            }

            override suspend fun editCard(
                token: String,
                editCardRequest: EditCardRequest
            ): Response<CardOperationResponse> {
                for (card in cards) {
                    if (card.cardNumber == editCardRequest.cardNumber) {
                        card.cardName = editCardRequest.cardName
                        card.cardNumber = editCardRequest.cardNumber
                        card.month = editCardRequest.month
                        card.year = editCardRequest.year
                        card.cvv = editCardRequest.cvv
                        return Response.success(
                            CardOperationResponse(true, 0L)
                        )
                    }
                }
                val errorResponseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "Error message")
                // Создаем неуспешный Response с кодом ошибки и объектом ResponseBody
                val response = Response.error<CardOperationResponse>(400, errorResponseBody)
                return response
            }

            override suspend fun deleteCard(
                token: String,
                deleteCardRequest: DeleteCardRequest
            ): Response<CardOperationResponse> {
                var i: Int =0
                for (card in cards) {
                    if (card.cardNumber == deleteCardRequest.cardNumber) {
                        break
                    }
                    i++
                }
                cards.removeAt(i)
                return Response.success(
                    CardOperationResponse(true, 0L)
                )
            }

            override suspend fun buyPremium(
                token: String,
                buyPremiumRequest: BuyPremiumRequest
            ): Response<BuyPremiumResponse> {
                userRole = UserType.Premium
                return Response.success(
                    BuyPremiumResponse(true, 0L)
                )
            }

            override suspend fun cancelPremium(token: String): Response<CancelPremiumResponse> {
                TODO("Not yet implemented")
            }

        }

    }

    single<StatisticsApi> {
        object: StatisticsApi {
            override suspend fun getPremiumStatistic(
                token: String,
                statisticsRequest: StatisticsRequest
            ): Response<PremiumStatisticsResponse> {
                return Response.success(
                    premiumStatistic
                )
            }

            override suspend fun getAdminStatistic(
                token: String,
                statisticsRequest: StatisticsRequest
            ): Response<AdminStatisticsResponse> {
                return Response.success(
                    adminStatisticResponse
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

    single<AdminActionRepository> { AdminActionRepositoryImpl(adminActionApi = get(), sessionRepository = get()) }
    single<UsersRepository> { UsersRepositoryImpl(api = get(), sessionRepository = get()) }

    single<UserDataRepository> { UserDataRepositoryImpl(userDataApi = get(), sessionRepository = get()) }

    single<PaymentRepository> { PaymentRepositoryImpl(paymentApi = get(), sessionRepository = get()) }

    single<StatisticRepository> { StatisticRepositoryImpl(statisticsApi = get(), sessionRepository = get()) }


    single<SportActivitiesStorage> { SportActivityStorageImpl(sportActivitiesDatabase = get()) }
    single<SportActivitiesDatabase> { SportActivitiesDatabase.getDataBase(context = get()) }

}