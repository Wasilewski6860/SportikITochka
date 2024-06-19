package com.example.sportikitochka.presentation.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.profile.ProfilePeriod
import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.profile.AdminProfileResponse
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.domain.use_cases.auth.GetSessionUseCase
import com.example.sportikitochka.domain.use_cases.auth.GetUserRoleUseCase
import com.example.sportikitochka.domain.use_cases.auth.SaveSessionUseCase
import com.example.sportikitochka.domain.use_cases.auth.SignOutUseCase
import com.example.sportikitochka.domain.use_cases.payment.CancelPremiumUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetAdminProfileUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetProfileUseCase
import com.example.sportikitochka.presentation.main.main.ScreenMainState
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException

class ProfileViewModel(
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getAdminProfileUseCase: GetAdminProfileUseCase,
    private val cancelPremiumUseCase: CancelPremiumUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val getSessionUseCase: GetSessionUseCase,
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenProfileState>()
    val screenState: LiveData<ScreenProfileState> = _screenState

    private val _userInfo = MutableLiveData<UserProfileResponse>()
    val userInfo: LiveData<UserProfileResponse> = _userInfo

    private val _adminInfo = MutableLiveData<AdminProfileResponse>()
    val adminInfo: LiveData<AdminProfileResponse> = _adminInfo

    private val _userRole = MutableLiveData<UserType>()
    val userRole: LiveData<UserType> = _userRole

    fun getUserRole() = _userRole.postValue(getUserRoleUseCase.execute())
    fun getRole()  = getUserRoleUseCase.execute()

    fun signOut(){
        viewModelScope.launch {
            signOutUseCase.execute()
        }
    }

    fun loadProfileInitial() {
        _screenState.postValue(ScreenProfileState.Loading)
        loadUserProfile(ProfilePeriod.WEEK)
    }
    fun loadProfileForWeek() {
        _screenState.postValue(ScreenProfileState.LoadingPeriod)
        loadUserProfile(ProfilePeriod.WEEK)
    }
    fun loadProfileForMonth() {
        _screenState.postValue(ScreenProfileState.LoadingPeriod)
        loadUserProfile(ProfilePeriod.MONTH)
    }
    fun loadProfileForYear() {
        _screenState.postValue(ScreenProfileState.LoadingPeriod)
        loadUserProfile(ProfilePeriod.YEAR)
    }
    fun loadProfileForAllTime() {
        _screenState.postValue(ScreenProfileState.LoadingPeriod)
        loadUserProfile(ProfilePeriod.ALL_TIME)
    }

    private fun loadUserProfile(period: ProfilePeriod) {
        viewModelScope.launch {
            try {
                val userProfileResponse = getProfileUseCase.execute(UserProfileRequest(period.period))
                if (userProfileResponse.isSuccessful) {
                    var responseBody = userProfileResponse.body()

                    if (responseBody != null) {
                        _userInfo.postValue(responseBody!!)
                        _screenState.postValue(ScreenProfileState.Success)
                    }
                    else _screenState.postValue(ScreenProfileState.Error)

                } else {
                    val error = userProfileResponse.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    error?.let { Log.e("LOAD USER PROFILE", it) }
                    _screenState.postValue(ScreenProfileState.Error)
                }
            } catch (httpException: HttpException) {
                Log.e("LOAD PROFILE", httpException.toString())
                _screenState.postValue(ScreenProfileState.Error)
            } catch (exception: Exception) {
                Log.e("LOAD PROFILE", exception.toString())
                _screenState.postValue(ScreenProfileState.Error)
            }
        }
    }

    fun loadAdminProfile() {
        _screenState.postValue(ScreenProfileState.Loading)
        viewModelScope.launch {
            try {
                val userProfileResponse = getAdminProfileUseCase.execute()
                if (userProfileResponse.isSuccessful) {
                    var responseBody = userProfileResponse.body()

                    if (responseBody != null) {
                        _adminInfo.postValue(responseBody!!)
                        _screenState.postValue(ScreenProfileState.Success)
                    }
                    else _screenState.postValue(ScreenProfileState.Error)

                } else {
                    val error = userProfileResponse.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    error?.let { Log.e("LOAD ADMIN PROFILE", it) }
                    _screenState.postValue(ScreenProfileState.Error)
                }
            } catch (httpException: HttpException) {
                Log.e("LOAD ADMIN PROFILE", httpException.toString())
                _screenState.postValue(ScreenProfileState.Error)
            } catch (exception: Exception) {
                Log.e("LOAD ADMIN PROFILE", exception.toString())
                _screenState.postValue(ScreenProfileState.Error)
            }
        }
    }

    fun cancelPremium() {
        _screenState.postValue(ScreenProfileState.Loading)
        viewModelScope.launch {
            try {
                val response = cancelPremiumUseCase.execute()
                if (response.isSuccessful) {
                    _screenState.postValue(ScreenProfileState.Success)
                    val session = getSessionUseCase.execute()
                    session?.role = UserType.Normal.toString()
                    session?.let {
                        saveSessionUseCase.execute(it)
                    }
                    loadProfileForWeek()
                    getUserRole()
                } else {
                    val error = response.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    error?.let { Log.e("CANCEL PREMIUM", it) }
                    _screenState.postValue(ScreenProfileState.Error)
                }
            } catch (httpException: HttpException) {
                Log.e("CANCEL PREMIUM", httpException.toString())
                _screenState.postValue(ScreenProfileState.Error)
            } catch (exception: Exception) {
                Log.e("CANCEL PREMIUM", exception.toString())
                _screenState.postValue(ScreenProfileState.Error)
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