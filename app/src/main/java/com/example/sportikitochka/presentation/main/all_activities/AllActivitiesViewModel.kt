package com.example.sportikitochka.presentation.main.all_activities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.response.activities.ActivityResponse
import com.example.sportikitochka.data.models.response.activities.mapToSportActivity
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.use_cases.activity.AddActivityLocalUseCase
import com.example.sportikitochka.domain.use_cases.activity.GetAllActivitiesLocalUseCase
import com.example.sportikitochka.domain.use_cases.activity.GetAllActivitiesRemoteUseCase
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AllActivitiesViewModel(
    private val addActivityLocalUseCase: AddActivityLocalUseCase,
    private val getAllActivitiesRemoteUseCase: GetAllActivitiesRemoteUseCase,
    private val getAllActivitiesLocalUseCase: GetAllActivitiesLocalUseCase
) : ViewModel() {
    private val _screenState = MutableLiveData<ScreenAllActivitiesState>()
    val screenState: LiveData<ScreenAllActivitiesState> = _screenState

    private val _activities = MutableLiveData<List<SportActivity>>()
    val activities: LiveData<List<SportActivity>> = _activities

    fun fetchActivities() {
        _screenState.value = ScreenAllActivitiesState.Loading
        viewModelScope.launch {
            try {
                val activitiesRemoteResponse = getAllActivitiesRemoteUseCase.execute()
                if (activitiesRemoteResponse.isSuccessful){

                    val responseBody: List<ActivityResponse>? = activitiesRemoteResponse.body()

                    if (responseBody!=null){
                        var list = responseBody.map { activityResponse -> activityResponse.mapToSportActivity() }
                        _activities.postValue(list)
                        for (activity in list){
                            addActivityLocalUseCase.execute(activity)
                        }
                        _screenState.value =
                            if (activities.value?.isEmpty() == true) ScreenAllActivitiesState.Empty else ScreenAllActivitiesState.SuccessRemote
                    }
                }
                else {
                    _activities.value = getAllActivitiesLocalUseCase.execute()

                    _screenState.value =
                        if (activities.value?.isEmpty() == true) ScreenAllActivitiesState.Empty else ScreenAllActivitiesState.ErrorRemote
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.postValue(ScreenAllActivitiesState.Error("К сожалению, не можем загрузить данные о пробежках"))
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.postValue(ScreenAllActivitiesState.Error("К сожалению, не можем загрузить данные о пробежках"))
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