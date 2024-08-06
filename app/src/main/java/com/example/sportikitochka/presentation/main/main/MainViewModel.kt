package com.example.sportikitochka.presentation.main.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.request.profile.UserProfileRequest
import com.example.data.models.response.activities.ActivityResponse
import com.example.data.models.response.activities.mapToSportActivity
import com.example.data.models.response.profile.StatisticsResponse
import com.example.data.models.response.profile.UserProfileResponse
import com.example.domain.coroutines.Response
import com.example.domain.models.SportActivity
import com.example.domain.models.Statistics
import com.example.domain.models.UserData
import com.example.domain.models.UserProfile
import com.example.domain.models.UserType
import com.example.domain.use_cases.activity.AddActivityLocalUseCase
import com.example.domain.use_cases.activity.GetAllActivitiesLocalUseCase
import com.example.domain.use_cases.activity.GetAllActivitiesRemoteUseCase
import com.example.domain.use_cases.auth.GetUserRoleUseCase
import com.example.domain.use_cases.profile.GetAdminProfileUseCase
import com.example.domain.use_cases.profile.GetProfileUseCase
import com.example.domain.use_cases.user_data.GetUserDataUseCase
import com.example.domain.use_cases.user_data.SaveUserDataUseCase
import com.example.sportikitochka.common.State
import com.example.sportikitochka.presentation.main.all_activities.AllActivitiesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException

data class MainScreenState(
    val activitiesState: State<List<SportActivity>> = State.Loading,
    val userProfileState: State<UserProfile> = State.Loading
)
class MainViewModel(
    private val getUserProfileUseCase: GetProfileUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val addActivityLocalUseCase: AddActivityLocalUseCase,
    private val getAllActivitiesRemoteUseCase: GetAllActivitiesRemoteUseCase,
    private val getAllActivitiesLocalUseCase: GetAllActivitiesLocalUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val getAdminProfileUseCase: GetAdminProfileUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow<MainScreenState>(
        MainScreenState()
    )
    val screenState: StateFlow<MainScreenState> = _screenState

    fun fetchActivities() {
        _screenState.value = _screenState.value.copy(activitiesState = State.Loading)
        viewModelScope.launch {
            val result = getAllActivitiesRemoteUseCase.execute()
            if (result is Response.Success) {
                for (activity in result.value){
                    addActivityLocalUseCase.execute(activity)
                }
                _screenState.value = _screenState.value.copy(activitiesState = State.Success(result.value))
            }
            else {
                _screenState.value = _screenState.value.copy(activitiesState = State.Success(getAllActivitiesLocalUseCase.execute()))
            }
        }
    }

    fun loadUserData() {
        _screenState.value = _screenState.value.copy(State.Loading)
        viewModelScope.launch {
            val userType: UserType? = getUserRoleUseCase.execute()
            val userDataResponse = getUserDataUseCase.execute()

            if(userDataResponse is Response.Success) {
                saveUserDataUseCase.execute(userDataResponse.value)
            }
            else {
                _screenState.value = _screenState.value.copy(userProfileState = State.Error((userDataResponse as Response.Failure).error))
            }

            if(userType is UserType.Admin) {
                val adminProfile = getAdminProfileUseCase.execute()
                if (adminProfile is Response.Success) {
                    _screenState.value = _screenState.value.copy(
                        userProfileState =
                        State.Success(
                            UserProfile(
                                name = adminProfile.value.name,
                                image = adminProfile.value.image,
                                rating = -1,
                                statistics = Statistics(0L,0L,0L),
                                achievements = listOf()
                            )
                        )
                    )
                }
                else {
                    _screenState.value = _screenState.value.copy(userProfileState = State.Error((adminProfile as Response.Failure).error))
                }

            }
            else {
                val normalProfile = getUserProfileUseCase.execute("week")
                if (normalProfile is Response.Success) {
                    _screenState.value = _screenState.value.copy(
                        userProfileState = State.Success(normalProfile.value)
                    )
                }
                else {
                    _screenState.value = _screenState.value.copy(userProfileState = State.Error((normalProfile as Response.Failure).error))
                }
            }
        }
    }
}


