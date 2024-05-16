package com.example.sportikitochka.presentation.main.tracking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.use_cases.activity.AddActivityRemoteUseCase
import com.example.sportikitochka.domain.use_cases.user_data.GetUserDataLocallyUseCase
import com.example.sportikitochka.domain.use_cases.user_data.GetUserDataUseCase
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException

class TrackingViewModel(
    private val addActivityRemoteUseCase: AddActivityRemoteUseCase,
    private val getUserDataLocallyUseCase: GetUserDataLocallyUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenTrackingState>()
    val screenState: LiveData<ScreenTrackingState> = _screenState

    private val _userInfo = MutableLiveData<UserDataResponse>()
    val userInfo: LiveData<UserDataResponse> = _userInfo

    val weight: Float? = getUserDataLocallyUseCase.execute()?.weight


    fun stopActivity(activity: SportActivity) {
        _screenState.value = ScreenTrackingState.Loading

        viewModelScope.launch {
            try {
                val result = addActivityRemoteUseCase.execute(activity)
                if (result.isSuccessful){
                    _screenState.value = ScreenTrackingState.Success
                }
                else{
                    val error = result.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    error?.let { Log.e("SAVE TRACKING", it) }
                    _screenState.value = ScreenTrackingState.Error("Произошла ошибка")
                }

            } catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.value = ScreenTrackingState.Error()
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.value = ScreenTrackingState.Error()
            }
            catch (exception: NoDataException) {
                Log.e("TAG", exception.toString())
                _screenState.value = ScreenTrackingState.Error(exception.message)
            }
        }
    }

    private fun getAvSpeed(distance: Int, time: Long): Float{
        return distance.toFloat() / (time / 1000)
    }

    class NoDataException(message: String) : Exception(message)

}