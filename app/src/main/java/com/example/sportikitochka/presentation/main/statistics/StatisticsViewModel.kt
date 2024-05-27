package com.example.sportikitochka.presentation.main.statistics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.profile.ProfilePeriod
import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.statistic.AdminStatisticsResponse
import com.example.sportikitochka.data.models.response.statistic.PremiumStatisticsResponse
import com.example.sportikitochka.domain.models.User
import com.example.sportikitochka.domain.use_cases.auth.GetUserRoleUseCase
import com.example.sportikitochka.domain.use_cases.statistic.GetAdminStatisticUseCase
import com.example.sportikitochka.domain.use_cases.statistic.GetPremiumStatisticUseCase
import com.example.sportikitochka.presentation.main.profile.ScreenProfileState
import com.example.sportikitochka.presentation.main.rating.ScreenRatingState
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException
import java.time.Period

class StatisticsViewModel(
    private val getUserTypeUseCase: GetUserRoleUseCase,
    private val getAdminStatisticUseCase: GetAdminStatisticUseCase,
    private val getPremiumStatisticUseCase: GetPremiumStatisticUseCase
) : ViewModel() {
    fun getType(): UserType = getUserTypeUseCase.execute()!!

    private val _screenState = MutableLiveData<StatisticScreenState>()
    val screenState: LiveData<StatisticScreenState> = _screenState

    private val _premiumStatistic = MutableLiveData<PremiumStatisticsResponse>()
    val premiumStatistic: LiveData<PremiumStatisticsResponse> = _premiumStatistic

    private val _adminStatistic = MutableLiveData<AdminStatisticsResponse>()
    val adminStatistic: LiveData<AdminStatisticsResponse> = _adminStatistic

    fun fetchPremiumStatistic(period: ProfilePeriod) {
        _screenState.postValue(StatisticScreenState.Loading)
        viewModelScope.launch {
            try {
                val getPremiumStatisticsResponse = getPremiumStatisticUseCase.execute(period)
                if (getPremiumStatisticsResponse.isSuccessful) {
                    var responseBody = getPremiumStatisticsResponse.body()

                    if (responseBody != null) {
                        _premiumStatistic.postValue(responseBody!!)
                        _screenState.postValue(StatisticScreenState.Success)
                    }
                    else _screenState.postValue(StatisticScreenState.Error)

                } else {
                    var error = getPremiumStatisticsResponse.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    error?.let { Log.e("PREMIUM_STATISTIC", it) }
                    _screenState.postValue(StatisticScreenState.Error)
                }
            } catch (httpException: HttpException) {
                Log.e("PREMIUM_STATISTIC", httpException.toString())
                _screenState.postValue(StatisticScreenState.Error)
            } catch (exception: Exception) {
                Log.e("PREMIUM_STATISTIC", exception.toString())
                _screenState.postValue(StatisticScreenState.Error)
            }
        }
    }

    fun fetchAdminStatistic(period: ProfilePeriod) {
        _screenState.postValue(StatisticScreenState.Loading)
        viewModelScope.launch {
            try {
                val getAdminStatisticsResponse = getAdminStatisticUseCase.execute(period)
                if (getAdminStatisticsResponse.isSuccessful) {
                    var responseBody = getAdminStatisticsResponse.body()

                    if (responseBody != null) {
                        _adminStatistic.postValue(responseBody!!)
                        _screenState.postValue(StatisticScreenState.Success)
                    }
                    else _screenState.postValue(StatisticScreenState.Error)

                } else {
                    var error = getAdminStatisticsResponse.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    error?.let { Log.e("ADMIN_STATISTIC", it) }
                    _screenState.postValue(StatisticScreenState.Error)
                }
            } catch (httpException: HttpException) {
                Log.e("ADMIN_STATISTIC", httpException.toString())
                _screenState.postValue(StatisticScreenState.Error)
            } catch (exception: Exception) {
                Log.e("ADMIN_STATISTIC", exception.toString())
                _screenState.postValue(StatisticScreenState.Error)
            }
        }
    }
}