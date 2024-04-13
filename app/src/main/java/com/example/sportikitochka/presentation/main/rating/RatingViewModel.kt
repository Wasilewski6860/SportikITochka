package com.example.sportikitochka.presentation.main.rating

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.data.models.response.users.mapToUser
import com.example.sportikitochka.domain.models.User
import com.example.sportikitochka.domain.use_cases.admin_action.BlockUserUseCase
import com.example.sportikitochka.domain.use_cases.admin_action.GrantPremiumUseCase
import com.example.sportikitochka.domain.use_cases.admin_action.RevokePremiumUseCase
import com.example.sportikitochka.domain.use_cases.admin_action.UnblockUserUseCase
import com.example.sportikitochka.domain.use_cases.auth.GetUserRoleUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetProfileLocallyUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetProfileUseCase
import com.example.sportikitochka.domain.use_cases.users.GetAllUsersUseCase
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RatingViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getProfileLocallyUseCase: GetProfileLocallyUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val unblockUserUseCase: UnblockUserUseCase,
    private val setPremiumUseCase: GrantPremiumUseCase,
    private val removePremiumUseCase: RevokePremiumUseCase,
    private val getUserTypeUseCase: GetUserRoleUseCase
) : ViewModel() {

    private val _userType = MutableLiveData<UserType>()
    val userType: LiveData<UserType> = _userType

    private val _screenState = MutableLiveData<ScreenRatingState>()
    val screenState: LiveData<ScreenRatingState> = _screenState

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _userInfo = MutableLiveData<UserProfileResponse>()
    val userInfo: LiveData<UserProfileResponse> = _userInfo

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val userProfileResponse = getProfileUseCase.execute(UserProfileRequest("all time"))
                if (userProfileResponse.isSuccessful) {
                    var responseBody = userProfileResponse.body()

                    if (responseBody != null) {
                        _userInfo.postValue(responseBody!!)
                    }
                    else {
                        //TODO Переделать
                       // _userInfo.postValue(getProfileLocallyUseCase.execute(UserProfileRequest("all time")))
                        _screenState.postValue(ScreenRatingState.ErrorInfo("К сожалению, не можем загрузить данные о профиле"))
                    }
                } else {
                    _screenState.postValue(ScreenRatingState.ErrorInfo("К сожалению, не можем загрузить данные о профиле"))
                }
            } catch (httpException: HttpException) {
                Log.e("GET_PROFILE_HTTP_EXCEPTION", httpException.toString())
                _screenState.postValue(ScreenRatingState.ErrorInfo("К сожалению, не можем загрузить данные о профиле"))
            } catch (exception: Exception) {
                Log.e("GET_PROFILE_EXCEPTION", exception.toString())
                _screenState.postValue(ScreenRatingState.ErrorInfo("К сожалению, не можем загрузить данные о профиле"))
            }
        }
    }

    fun fetchUsers() {
        _screenState.value = ScreenRatingState.Loading
        viewModelScope.launch {
            try {
                val usersResponse = getAllUsersUseCase.execute()
                if (usersResponse.isSuccessful){

                    val responseBody = usersResponse.body()

                    if (responseBody!=null){
                        var list = responseBody.map { userResponse -> userResponse.mapToUser() }
                        _users.postValue(list)

                        _screenState.value =
                            if (users.value?.isEmpty() == true) ScreenRatingState.Empty else ScreenRatingState.SuccessRemote
                    }
                }
                else {
                    _screenState.postValue(ScreenRatingState.ErrorBlock("К сожалению, не можем загрузить данные о пользователях"))
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.postValue(ScreenRatingState.ErrorBlock("К сожалению, не можем загрузить данные о пользователях"))
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.postValue(ScreenRatingState.ErrorBlock("К сожалению, не можем загрузить данные о пользователях"))
            }
        }
    }

    fun getUserType() {
        try {
            val type = getUserTypeUseCase.execute()
            type?.let {
                _userType.postValue(it)
            }
        }
        catch (exception: Exception) {
            Log.e("TAG", exception.toString())
            _screenState.postValue(ScreenRatingState.ErrorBlock("Хто я?"))
        }
    }

    fun getType(): UserType = getUserTypeUseCase.execute()!!

    fun blockUser(user: User) {
        _screenState.value = ScreenRatingState.Loading
        viewModelScope.launch {
            try {
                val blockResponse = if (user.isBlocked) unblockUserUseCase.execute(userId = user.id.toString()) else blockUserUseCase.execute(userId = user.id.toString())
                if (blockResponse.isSuccessful){
                    val responseBody = blockResponse.body()
                    if (responseBody!=null){
                        if (responseBody.isSuccess()) fetchUsers()
                        else _screenState.value = ScreenRatingState.ErrorInfo("Не удалось изменить информацию о пользователе")
                    }
                }
                else {
                    _screenState.value = ScreenRatingState.ErrorInfo("Не удалось изменить информацию о пользователе")
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.postValue(ScreenRatingState.ErrorInfo("К сожалению, не можем загрузить данные о пользователях"))
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.postValue(ScreenRatingState.ErrorInfo("К сожалению, не можем загрузить данные о пользователях"))
            }
        }
    }

    fun setPremium(id: Int) {
        _screenState.value = ScreenRatingState.Loading
        viewModelScope.launch {
            try {
                val setPremiumResponse = setPremiumUseCase.execute(id.toString())
                if (setPremiumResponse.isSuccessful){
                    val responseBody = setPremiumResponse.body()
                    if (responseBody!=null){
                        if (responseBody.isSuccess()) fetchUsers()
                        else _screenState.value = ScreenRatingState.ErrorInfo("Не удалось выдать пользователю премиум")
                    }
                }
                else {
                    _screenState.value = ScreenRatingState.ErrorInfo("Не удалось выдать пользователю премиум")
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.postValue(ScreenRatingState.ErrorInfo("Не удалось выдать пользователю премиум"))
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.postValue(ScreenRatingState.ErrorInfo("Не удалось выдать пользователю премиум"))
            }
        }
    }

    fun removePremium(id: Int) {
        _screenState.value = ScreenRatingState.Loading
        viewModelScope.launch {
            try {
                val removePremiumResponse = removePremiumUseCase.execute(id.toString())
                if (removePremiumResponse.isSuccessful){
                    val responseBody = removePremiumResponse.body()
                    if (responseBody!=null){
                        if (responseBody.isSuccess()) fetchUsers()
                        else _screenState.value = ScreenRatingState.ErrorInfo("Не удалось лишить пользователя премиума")
                    }
                }
                else {
                    _screenState.value = ScreenRatingState.ErrorInfo("Не удалось лишить пользователя премиума")
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.postValue(ScreenRatingState.ErrorInfo("Не удалось лишить пользователя премиума"))
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.postValue(ScreenRatingState.ErrorInfo("Не удалось лишить пользователя премиума"))
            }
        }
    }

}

sealed class ScreenRatingState {
    object Loading: ScreenRatingState()
    object SuccessRemote: ScreenRatingState()
    class ErrorInfo(val message: String): ScreenRatingState()
    class ErrorBlock(val message: String): ScreenRatingState()
    object Empty: ScreenRatingState()
}