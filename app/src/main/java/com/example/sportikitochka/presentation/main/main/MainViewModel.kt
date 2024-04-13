package com.example.sportikitochka.presentation.main.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.response.activities.mapToSportActivity
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.use_cases.activity.AddActivityLocalUseCase
import com.example.sportikitochka.domain.use_cases.activity.GetAllActivitiesLocalUseCase
import com.example.sportikitochka.domain.use_cases.activity.GetAllActivitiesRemoteUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetProfileUseCase
import com.example.sportikitochka.domain.use_cases.user_data.GetUserDataUseCase
import com.example.sportikitochka.domain.use_cases.user_data.SaveUserDataUseCase
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(
    private val getUserProfileUseCase: GetProfileUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val addActivityLocalUseCase: AddActivityLocalUseCase,
    private val getAllActivitiesRemoteUseCase: GetAllActivitiesRemoteUseCase,
    private val getAllActivitiesLocalUseCase: GetAllActivitiesLocalUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenMainState>()
    val screenState: LiveData<ScreenMainState> = _screenState

    private val _activities = MutableLiveData<List<SportActivity>>()
    val activities: LiveData<List<SportActivity>> = _activities

    private val _userProfile = MutableLiveData<UserProfileResponse>()
    val userProfile: LiveData<UserProfileResponse> = _userProfile

    fun fetchActivities() {
        _screenState.value = ScreenMainState.Loading
        viewModelScope.launch {
            try {
                val activitiesRemoteResponse = getAllActivitiesRemoteUseCase.execute()
                if (activitiesRemoteResponse.isSuccessful){

                    val responseBody = activitiesRemoteResponse.body()

                    if (responseBody!=null){
                        var list = responseBody.map { activityResponse -> activityResponse.mapToSportActivity() }
                        _activities.postValue(list)
                        for (activity in list){
                            addActivityLocalUseCase.execute(activity)
                        }
                        _screenState.value =
                            if (activities.value?.isEmpty() == true) ScreenMainState.Empty else ScreenMainState.ActivitiesLoadedRemote
                    }
                }
                else {
                    _activities.value = getAllActivitiesLocalUseCase.execute()

                    _screenState.value =
                        if (activities.value?.isEmpty() == true) ScreenMainState.Empty else ScreenMainState.ActivitiesLoadedLocal
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.postValue(ScreenMainState.ErrorActivities)
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.postValue(ScreenMainState.ErrorActivities)
            }
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val userProfileResponse = getUserProfileUseCase.execute(UserProfileRequest("week"))
                val userDataResponse = getUserDataUseCase.execute()
                if (userProfileResponse.isSuccessful) {
                    var responseBody = userProfileResponse.body()
                    var responseDataBody = userDataResponse.body()

                    if (responseBody != null) {
                        _userProfile.postValue(responseBody!!)
                    }
                    if (responseDataBody != null) {
                        saveUserDataUseCase.execute(responseDataBody)
                    }
                    else _screenState.postValue(ScreenMainState.ProfileLoadingError)

                } else {
                    _screenState.postValue(ScreenMainState.ProfileLoadingError)
                }
            } catch (httpException: HttpException) {
                Log.e("GET_PROFILE_HTTP_EXCEPTION", httpException.toString())
                _screenState.postValue(ScreenMainState.ProfileLoadingError)
            } catch (exception: Exception) {
                Log.e("GET_PROFILE_EXCEPTION", exception.toString())
                _screenState.postValue(ScreenMainState.ProfileLoadingError)
            }
        }
    }
}


