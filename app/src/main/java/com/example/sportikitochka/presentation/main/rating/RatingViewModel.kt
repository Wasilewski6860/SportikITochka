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
import com.example.sportikitochka.domain.use_cases.profile.GetProfileUseCase
import com.example.sportikitochka.domain.use_cases.users.GetAllUsersUseCase
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RatingViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val unblockUserUseCase: UnblockUserUseCase,
    private val grantPremiumUseCase: GrantPremiumUseCase,
    private val revokePremiumUseCase: RevokePremiumUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenRatingState>()
    val screenState: LiveData<ScreenRatingState> = _screenState

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _userInfo = MutableLiveData<UserProfileResponse>()
    val userInfo: LiveData<UserProfileResponse> = _userInfo

    fun getUserRole() = getUserRoleUseCase.execute()

    fun loadUserProfile() {
        _screenState.value = ScreenRatingState.Loading
        viewModelScope.launch {
            try {
                val userProfileResponse = getProfileUseCase.execute(UserProfileRequest("all time"))
                if (userProfileResponse.isSuccessful) {
                    var responseBody = userProfileResponse.body()

                    if (responseBody != null) {
                        _userInfo.postValue(responseBody!!)
                        _screenState.postValue(ScreenRatingState.ProfileLoaded)
                    }
                    else {
                        _screenState.postValue(ScreenRatingState.ProfileError)
                    }
                } else {
                    _screenState.postValue(ScreenRatingState.ProfileError)
                }
            } catch (httpException: HttpException) {
                Log.e("GET_PROFILE_HTTP_EXCEPTION", httpException.toString())
                _screenState.postValue(ScreenRatingState.ProfileError)
            } catch (exception: Exception) {
                Log.e("GET_PROFILE_EXCEPTION", exception.toString())
                _screenState.postValue(ScreenRatingState.ProfileError)
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
                            if (users.value?.isEmpty() == true) ScreenRatingState.Empty else ScreenRatingState.UsersLoaded
                    }
                }
                else {
                    _screenState.postValue(ScreenRatingState.UsersLoadingError)
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.postValue(ScreenRatingState.UsersLoadingError)
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.postValue(ScreenRatingState.UsersLoadingError)
            }
        }
    }

    fun blockUser(user: User) {
        _screenState.value = ScreenRatingState.Loading
        viewModelScope.launch {
            try {

                val response = if (user.isBlocked){
                    unblockUserUseCase.execute(user.id.toString())
                } else {
                    blockUserUseCase.execute(user.id.toString())
                }

                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody!=null) {
                        if (responseBody.isSuccess()) _screenState.value = ScreenRatingState.AdminActionSuccess
                        else _screenState.value = ScreenRatingState.AdminActionError
                    }
                }
                else {
                    _screenState.value = ScreenRatingState.AdminActionError
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.value = ScreenRatingState.AdminActionError
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.value = ScreenRatingState.AdminActionError
            }
        }
    }

    fun setPremium(id: Int) {
        _screenState.value = ScreenRatingState.Loading
        viewModelScope.launch {
            try {
                val setPremiumResponse = grantPremiumUseCase.execute(id.toString())
                if (setPremiumResponse.isSuccessful){
                    val responseBody = setPremiumResponse.body()
                    if (responseBody!=null){
                        if (responseBody.isSuccess()) _screenState.value = ScreenRatingState.AdminActionSuccess
                        else _screenState.value = ScreenRatingState.AdminActionError
                    }
                }
                else {
                    _screenState.value = ScreenRatingState.AdminActionError
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.value = ScreenRatingState.AdminActionError
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.value = ScreenRatingState.AdminActionError
            }
        }
    }

    fun removePremium(id: Int) {
        _screenState.value = ScreenRatingState.Loading
        viewModelScope.launch {
            try {
                val removePremiumResponse = revokePremiumUseCase.execute(id.toString())
                if (removePremiumResponse.isSuccessful){
                    val responseBody = removePremiumResponse.body()
                    if (responseBody!=null){
                        if (responseBody.isSuccess()) _screenState.value = ScreenRatingState.AdminActionSuccess
                        else _screenState.value = ScreenRatingState.AdminActionError
                    }
                }
                else {
                    _screenState.value = ScreenRatingState.AdminActionError
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.value = ScreenRatingState.AdminActionError
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.value = ScreenRatingState.AdminActionError
            }
        }
    }

}

sealed class ScreenRatingState {
    object Loading: ScreenRatingState()
    object UsersLoaded: ScreenRatingState()
    object UsersLoadingError: ScreenRatingState()
    object ProfileLoaded: ScreenRatingState()
    object ProfileError: ScreenRatingState()
    object AdminActionSuccess: ScreenRatingState()
    object AdminActionError: ScreenRatingState()
    object Empty: ScreenRatingState()
}