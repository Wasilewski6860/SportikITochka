package com.example.sportikitochka.presentation.main.statistics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.request.profile.ProfilePeriod
import com.example.data.models.response.statistic.AdminStatisticsResponse
import com.example.data.models.response.statistic.PremiumStatisticsResponse
import com.example.domain.coroutines.Response
import com.example.domain.models.AdminStatistic
import com.example.domain.models.PremiumStatistic
import com.example.domain.models.UserType
import com.example.domain.use_cases.auth.GetUserRoleUseCase
import com.example.domain.use_cases.statistic.GetAdminStatisticUseCase
import com.example.domain.use_cases.statistic.GetPremiumStatisticUseCase
import com.example.sportikitochka.common.State
import com.example.sportikitochka.presentation.main.rating.RatingScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException

data class StatisticsScreenState(
    val premiumState: State<PremiumStatistic> = State.NotStarted,
    val adminState: State<AdminStatistic> = State.NotStarted,
)
class StatisticsViewModel(
    private val getUserTypeUseCase: GetUserRoleUseCase,
    private val getAdminStatisticUseCase: GetAdminStatisticUseCase,
    private val getPremiumStatisticUseCase: GetPremiumStatisticUseCase
) : ViewModel() {
    fun getType(): UserType = getUserTypeUseCase.execute()!!

    private val _screenState = MutableStateFlow<StatisticsScreenState>(
        StatisticsScreenState()
    )
    val screenState: StateFlow<StatisticsScreenState> = _screenState

    fun fetchPremiumStatistic(period: ProfilePeriod) {
        _screenState.value = _screenState.value.copy(premiumState = State.Loading)
        viewModelScope.launch {
            val userDataResponse = getPremiumStatisticUseCase.execute(period.toString())
            if (userDataResponse is Response.Success) {
                _screenState.value = _screenState.value.copy(premiumState = State.Success(userDataResponse.value))
            }
            else {
                _screenState.value = _screenState.value.copy(premiumState = State.Error((userDataResponse as Response.Failure).error))
            }
        }
    }

    fun fetchAdminStatistic(period: com.example.data.models.request.profile.ProfilePeriod) {
        _screenState.value = _screenState.value.copy(adminState = State.Loading)
        viewModelScope.launch {
            val userDataResponse = getAdminStatisticUseCase.execute(period.toString())
            if (userDataResponse is Response.Success) {
                _screenState.value = _screenState.value.copy(adminState = State.Success(userDataResponse.value))
            }
            else {
                _screenState.value = _screenState.value.copy(adminState = State.Error((userDataResponse as Response.Failure).error))
            }
        }
    }
}