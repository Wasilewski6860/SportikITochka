package com.example.sportikitochka.presentation.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.profile.ProfilePeriod
import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.domain.use_cases.auth.GetUserRoleUseCase
import com.example.sportikitochka.domain.use_cases.auth.SignOutUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetProfileUseCase
import com.example.sportikitochka.presentation.main.main.ScreenMainState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel(
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenProfileState>()
    val screenState: LiveData<ScreenProfileState> = _screenState

    private val _userInfo = MutableLiveData<UserProfileResponse>()
    val userInfo: LiveData<UserProfileResponse> = _userInfo

    fun getUserRole() = getUserRoleUseCase.execute()

    fun signOut(){
        viewModelScope.launch {
            signOutUseCase.execute()
        }
    }

    fun loadProfileForWeek() = loadUserProfile(ProfilePeriod.WEEK)
    fun loadProfileForMonth() = loadUserProfile(ProfilePeriod.MONTH)
    fun loadProfileForYear() = loadUserProfile(ProfilePeriod.YEAR)
    fun loadProfileForAllTime() = loadUserProfile(ProfilePeriod.ALL_TIME)

    private fun loadUserProfile(period: ProfilePeriod) {
        _screenState.postValue(ScreenProfileState.Loading)
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
                    _screenState.postValue(ScreenProfileState.Error)
                }
            } catch (httpException: HttpException) {
                Log.e("GET_PROFILE_HTTP_EXCEPTION", httpException.toString())
                _screenState.postValue(ScreenProfileState.Error)
            } catch (exception: Exception) {
                Log.e("GET_PROFILE_EXCEPTION", exception.toString())
                _screenState.postValue(ScreenProfileState.Error)
            }
        }
    }

}

sealed class ScreenProfileState {
    object Loading: ScreenProfileState()
    object Success: ScreenProfileState()
    object Error: ScreenProfileState()
}