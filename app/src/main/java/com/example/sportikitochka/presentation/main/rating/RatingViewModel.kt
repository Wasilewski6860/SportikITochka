package com.example.sportikitochka.presentation.main.rating

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.request.profile.ProfilePeriod
import com.example.data.models.request.profile.UserProfileRequest
import com.example.data.models.response.profile.AdminProfileResponse
import com.example.data.models.response.profile.UserProfileResponse
import com.example.data.models.response.users.mapToUser
import com.example.domain.coroutines.Response
import com.example.domain.models.AdminProfile
import com.example.domain.models.User
import com.example.domain.models.UserProfile
import com.example.domain.models.UserType
import com.example.domain.use_cases.admin_action.BlockUserUseCase
import com.example.domain.use_cases.admin_action.GrantPremiumUseCase
import com.example.domain.use_cases.admin_action.RevokePremiumUseCase
import com.example.domain.use_cases.admin_action.UnblockUserUseCase
import com.example.domain.use_cases.auth.GetUserRoleUseCase
import com.example.domain.use_cases.profile.GetAdminProfileUseCase
import com.example.domain.use_cases.profile.GetProfileLocallyUseCase
import com.example.domain.use_cases.profile.GetProfileUseCase
import com.example.domain.use_cases.users.GetAllUsersUseCase
import com.example.sportikitochka.common.State
import com.example.sportikitochka.presentation.main.profile.ProfileScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException

data class RatingScreenState(
    val userType: UserType = UserType.Normal,
    val users: State<List<User>> = State.Loading,
    val adminProfileState: State<AdminProfile> = State.NotStarted,
    val userProfileState: State<UserProfile> = State.NotStarted,
    val operationState: State<Unit> = State.NotStarted
)

class RatingViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getAdminProfileUseCase: GetAdminProfileUseCase,
    private val getProfileLocallyUseCase: GetProfileLocallyUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val unblockUserUseCase: UnblockUserUseCase,
    private val setPremiumUseCase: GrantPremiumUseCase,
    private val removePremiumUseCase: RevokePremiumUseCase,
    private val getUserTypeUseCase: GetUserRoleUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<RatingScreenState>(
        RatingScreenState()
    )
    val screenState: StateFlow<RatingScreenState> = _screenState


    fun getUserRole() = getUserTypeUseCase.execute()

    fun loadUserProfile(period: ProfilePeriod) {
        _screenState.value = _screenState.value.copy(userProfileState = State.Loading)
        viewModelScope.launch {
            val userDataResponse = getProfileUseCase.execute(period.toString())
            if (userDataResponse is Response.Success) {
                _screenState.value = _screenState.value.copy(userProfileState = State.Success(userDataResponse.value))
            }
            else {
                _screenState.value = _screenState.value.copy(userProfileState = State.Error((userDataResponse as Response.Failure).error))
            }
        }
    }

    fun loadAdminProfile() {
        _screenState.value = _screenState.value.copy(adminProfileState = State.Loading)
        viewModelScope.launch {
            val userDataResponse = getAdminProfileUseCase.execute()
            if (userDataResponse is Response.Success) {
                _screenState.value = _screenState.value.copy(adminProfileState = State.Success(userDataResponse.value))
            }
            else {
                _screenState.value = _screenState.value.copy(adminProfileState = State.Error((userDataResponse as Response.Failure).error))
            }
        }
    }

    fun loadUserProfile() {
        if (getUserRole() == UserType.Admin) {
            loadAdminProfile()
        }
        else loadUserProfile(ProfilePeriod.ALL_TIME)
    }



    fun fetchUsers() {
        _screenState.value = _screenState.value.copy(users = State.Loading)
        viewModelScope.launch {
            val result = getAllUsersUseCase.execute()
            if (result is Response.Success) {
                _screenState.value = _screenState.value.copy(users = State.Success(result.value))
            }
            else {
                _screenState.value = _screenState.value.copy(users = State.Error((result as Response.Failure).error))
            }
        }
    }


    fun getType(): UserType = getUserTypeUseCase.execute()!!

    fun blockUser(user: User) {
        _screenState.value = _screenState.value.copy(operationState = State.Loading)
        viewModelScope.launch {
            val blockResponse = if (user.isBlocked) unblockUserUseCase.execute(userId = user.id.toString()) else blockUserUseCase.execute(userId = user.id.toString())
            if (blockResponse is Response.Success) {
                _screenState.value = _screenState.value.copy(operationState = State.Success(blockResponse.value))
                fetchUsers()
            }
            else {
                _screenState.value = _screenState.value.copy(operationState = State.Error((blockResponse as Response.Failure).error))
            }
        }
    }

    fun setPremium(id: Int) {
        _screenState.value = _screenState.value.copy(operationState = State.Loading)
        viewModelScope.launch {
            val setPremiumResponse = setPremiumUseCase.execute(id.toString())
            if (setPremiumResponse is Response.Success) {
                _screenState.value = _screenState.value.copy(operationState = State.Success(setPremiumResponse.value))
                fetchUsers()
            }
            else {
                _screenState.value = _screenState.value.copy(operationState = State.Error((setPremiumResponse as Response.Failure).error))
            }
        }
    }

    fun removePremium(id: Int) {
        _screenState.value = _screenState.value.copy(operationState = State.Loading)
        viewModelScope.launch {
            val removePremiumResponse = removePremiumUseCase.execute(id.toString())
            if (removePremiumResponse is Response.Success) {
                _screenState.value = _screenState.value.copy(operationState = State.Success(removePremiumResponse.value))
                fetchUsers()
            }
            else {
                _screenState.value = _screenState.value.copy(operationState = State.Error((removePremiumResponse as Response.Failure).error))
            }
        }
    }

    fun getUserType() {
        _screenState.value = _screenState.value.copy(userType = getType())
    }

}

sealed class ScreenRatingState {
    object Loading: ScreenRatingState()
    object SuccessRemote: ScreenRatingState()
    class ErrorInfo(val message: String): ScreenRatingState()
    class ErrorBlock(val message: String): ScreenRatingState()
    object Empty: ScreenRatingState()
}