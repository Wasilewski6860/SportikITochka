package com.example.sportikitochka.presentation.main.all_activities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.response.activities.ActivityResponse
import com.example.data.models.response.activities.mapToSportActivity
import com.example.domain.coroutines.Response
import com.example.domain.models.SportActivity
import com.example.domain.use_cases.activity.AddActivityLocalUseCase
import com.example.domain.use_cases.activity.GetAllActivitiesLocalUseCase
import com.example.domain.use_cases.activity.GetAllActivitiesRemoteUseCase
import com.example.sportikitochka.common.State
import com.example.sportikitochka.presentation.auth.sign_up.SignUpNavigationState
import com.example.sportikitochka.presentation.auth.sign_up.SignUpScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class AllActivitiesState(
    val activitiesState: State<List<SportActivity>> = State.Loading
)
class AllActivitiesViewModel(
    private val addActivityLocalUseCase: AddActivityLocalUseCase,
    private val getAllActivitiesRemoteUseCase: GetAllActivitiesRemoteUseCase,
    private val getAllActivitiesLocalUseCase: GetAllActivitiesLocalUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<AllActivitiesState>(
        AllActivitiesState()
    )
    val screenState: StateFlow<AllActivitiesState> = _screenState


    fun fetchActivities() {
        _screenState.value = _screenState.value.copy(State.Loading)
        viewModelScope.launch {
            val result = getAllActivitiesRemoteUseCase.execute()
            if (result is Response.Success) {
                for (activity in result.value){
                    addActivityLocalUseCase.execute(activity)
                }
                _screenState.value = _screenState.value.copy(State.Success(result.value))
            }
            else {
                _screenState.value = _screenState.value.copy(State.Success(getAllActivitiesLocalUseCase.execute()))
            }
        }
    }
}



sealed class ScreenAllActivitiesState {
    object Loading: ScreenAllActivitiesState()
    object SuccessRemote: ScreenAllActivitiesState()
    object ErrorRemote: ScreenAllActivitiesState()
    class Error(val message: String): ScreenAllActivitiesState()
    object Empty: ScreenAllActivitiesState()
}