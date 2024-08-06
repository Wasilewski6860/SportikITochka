package com.example.sportikitochka.presentation.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.request.profile.ProfilePeriod
import com.example.data.models.request.profile.UserProfileRequest
import com.example.data.models.response.profile.AdminProfileResponse
import com.example.data.models.response.profile.UserProfileResponse
import com.example.domain.coroutines.Response
import com.example.domain.models.AdminProfile
import com.example.domain.models.UserData
import com.example.domain.models.UserProfile
import com.example.domain.models.UserType
import com.example.domain.use_cases.auth.GetSessionUseCase
import com.example.domain.use_cases.auth.GetUserRoleUseCase
import com.example.domain.use_cases.auth.SaveSessionUseCase
import com.example.domain.use_cases.auth.SignOutUseCase
import com.example.domain.use_cases.payment.CancelPremiumUseCase
import com.example.domain.use_cases.profile.GetAdminProfileUseCase
import com.example.domain.use_cases.profile.GetProfileUseCase
import com.example.sportikitochka.common.State
import com.example.sportikitochka.presentation.main.payment.PaymentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException

data class ProfileScreenState(
    val profileState: State<UserProfile> = State.Loading,
    val adminProfileState: State<AdminProfile> = State.Loading,
    val cancelProfileState: State<Unit> = State.NotStarted,
    val userRole: UserType = UserType.Normal
)
class ProfileViewModel(
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getAdminProfileUseCase: GetAdminProfileUseCase,
    private val cancelPremiumUseCase: CancelPremiumUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val getSessionUseCase: GetSessionUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ProfileScreenState>(
        ProfileScreenState()
    )
    val screenState: StateFlow<ProfileScreenState> = _screenState

    fun getRole()  = getUserRoleUseCase.execute()

    fun signOut(){
        viewModelScope.launch {
            signOutUseCase.execute()
        }
    }

    fun getUserRole(){
        _screenState.value = _screenState.value.copy(userRole = getUserRoleUseCase.execute()!!)
    }
    fun loadUserProfile(period: ProfilePeriod) {
        _screenState.value = _screenState.value.copy(profileState = State.Loading)
        viewModelScope.launch {
            val userDataResponse = getProfileUseCase.execute(period.toString())
            if (userDataResponse is Response.Success) {
                _screenState.value = _screenState.value.copy(profileState = State.Success(userDataResponse.value))
            }
            else {
                _screenState.value = _screenState.value.copy(profileState = State.Error((userDataResponse as Response.Failure).error))
            }
        }
    }

    fun loadAdminProfile() {
        _screenState.value = _screenState.value.copy(adminProfileState = State.Loading)
        viewModelScope.launch {
            val userDataResponse = getAdminProfileUseCase.execute()
            if (userDataResponse is Response.Success) {
                _screenState.value = _screenState.value.copy(adminProfileState = State.Success(userDataResponse.value))
            }
            else {
                _screenState.value = _screenState.value.copy(adminProfileState = State.Error((userDataResponse as Response.Failure).error))
            }
        }
    }

    fun loadProfileInitial() {
        loadUserProfile(ProfilePeriod.WEEK)
    }
    fun loadProfileForWeek() {
        loadUserProfile(ProfilePeriod.WEEK)
    }
    fun loadProfileForMonth() {
        loadUserProfile(ProfilePeriod.MONTH)
    }
    fun loadProfileForYear() {
        loadUserProfile(ProfilePeriod.YEAR)
    }
    fun loadProfileForAllTime() {
        loadUserProfile(ProfilePeriod.ALL_TIME)
    }


    fun cancelPremium() {
        _screenState.value = _screenState.value.copy(cancelProfileState = State.Loading)
        viewModelScope.launch {
            val userDataResponse = cancelPremiumUseCase.execute()
            if (userDataResponse is Response.Success) {
                _screenState.value = _screenState.value.copy(
                    cancelProfileState = State.Success(userDataResponse.value),
                    userRole = UserType.Normal
                )
                val session = getSessionUseCase.execute()
                session?.role = UserType.Normal.toString()
                session?.let {
                    saveSessionUseCase.execute(it)
                }
                loadProfileForWeek()
            }
            else {
                _screenState.value = _screenState.value.copy(
                    cancelProfileState = State.Error((userDataResponse as Response.Failure).error),
                    userRole = UserType.Premium
                )
            }
        }
    }
}

sealed class ScreenProfileState {
    object Loading: ScreenProfileState()
    object LoadingPeriod: ScreenProfileState()
    object Success: ScreenProfileState()
    object Error: ScreenProfileState()
}