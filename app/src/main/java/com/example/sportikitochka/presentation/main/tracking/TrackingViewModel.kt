package com.example.sportikitochka.presentation.main.tracking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.response.user_data.UserDataResponse
import com.example.domain.coroutines.Response
import com.example.domain.models.SportActivity
import com.example.domain.models.UserData
import com.example.domain.use_cases.activity.AddActivityRemoteUseCase
import com.example.domain.use_cases.user_data.GetUserDataLocallyUseCase
import com.example.sportikitochka.common.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException
import java.io.File

data class TrackingState(
    val userDataState: State<UserData> = State.Loading,
    val saveState: State<Unit> = State.NotStarted,
    val weight: Float = -1F
)
class TrackingViewModel(
    private val addActivityRemoteUseCase: AddActivityRemoteUseCase,
    private val getUserDataLocallyUseCase: GetUserDataLocallyUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<TrackingState>(
        TrackingState()
    )
    val screenState: StateFlow<TrackingState> = _screenState

    fun stopActivity(activity: SportActivity, image: File) {
        _screenState.value = _screenState.value.copy(saveState = State.Loading)
        viewModelScope.launch {
            val response = addActivityRemoteUseCase.execute(activity, image)
            if (response is Response.Success) {
                _screenState.value = _screenState.value.copy(saveState = State.Success(response.value))
            }
            else {
                _screenState.value = _screenState.value.copy(saveState = State.Error((response as Response.Failure).error))
            }
        }
    }

    private fun getAvSpeed(distance: Int, time: Long): Float{
        return distance.toFloat() / (time / 1000)
    }

    class NoDataException(message: String) : Exception(message)

}