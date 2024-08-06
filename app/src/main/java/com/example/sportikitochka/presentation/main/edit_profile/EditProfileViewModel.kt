package com.example.sportikitochka.presentation.main.edit_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.request.user_data.ChangeAdminDataRequest
import com.example.data.models.request.user_data.ChangeDataUserRequest
import com.example.data.models.response.user_data.UserDataResponse
import com.example.domain.coroutines.Response
import com.example.domain.models.UserData
import com.example.domain.models.UserType
import com.example.domain.use_cases.auth.GetUserRoleUseCase
import com.example.domain.use_cases.user_data.ChangeAdminDataUseCase
import com.example.domain.use_cases.user_data.ChangeUserDataUseCase
import com.example.domain.use_cases.user_data.GetAdminDataUseCase
import com.example.domain.use_cases.user_data.GetUserDataLocallyUseCase
import com.example.domain.use_cases.user_data.GetUserDataUseCase
import com.example.sportikitochka.common.State
import com.example.sportikitochka.presentation.main.all_activities.AllActivitiesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


data class EditProfileState(
    val userDataState: State<UserData> = State.Loading,
    val changeDataState: State<Unit> = State.NotStarted,
    val image: String? = null
)

class EditProfileViewModel(
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getAdminDataUseCase: GetAdminDataUseCase,
    private val getUserDataLocallyUseCase: GetUserDataLocallyUseCase,
    private val changeUserDataUseCase: ChangeUserDataUseCase,
    private val changeAdminDataUseCase: ChangeAdminDataUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<EditProfileState>(
        EditProfileState()
    )
    val screenState: StateFlow<EditProfileState> = _screenState

    fun getUserRole() = getUserRoleUseCase.execute()

    fun loadUserData() {
        _screenState.value = _screenState.value.copy(State.Loading)
        viewModelScope.launch {
            val userType: UserType? = getUserRole()
            if(userType is UserType.Admin) {
                val adminDataResponse = getAdminDataUseCase.execute()
                if (adminDataResponse is Response.Success) {
                    val data = adminDataResponse.value
                    _screenState.value = _screenState.value.copy(
                        State.Success(
                            UserData(
                                id = 0,
                                name = data.name,
                                image = data.image,
                                weight = -1F,
                                phone = data.phone,
                                birthday = data.birthday
                            )
                        )
                    )
                }
                else {
                    _screenState.value = _screenState.value.copy(State.Error((adminDataResponse as Response.Failure).error))
                }
            }
            else {
                val userDataResponse = getUserDataUseCase.execute()
                if (userDataResponse is Response.Success) {
                    _screenState.value = _screenState.value.copy(State.Success(userDataResponse.value))
                }
                else {
                    _screenState.value = _screenState.value.copy(State.Error((userDataResponse as Response.Failure).error))
                }
            }
        }
    }

    fun changeUserData(
        name: String,
        image: File,
        weight: Float?,
        phone: String,
        birthday: String
    ) {
        _screenState.value = _screenState.value.copy(State.Loading)
        viewModelScope.launch {
            val userType: UserType? = getUserRole()
            val convertedBirthday = convertDateFormat(birthday)

            val response = if(userType is UserType.Admin) changeAdminDataUseCase.execute(
                name = name,
                image = image,
                phone = phone,
                birthday = convertedBirthday
            )
            else changeUserDataUseCase.execute(
                name = name,
                image = image,
                weight = weight!!.toInt(), //TODO
                phone = phone,
                birthday = convertedBirthday
            )

            if(response is Response.Success) {
                _screenState.value = _screenState.value.copy(
                    changeDataState = State.Success(Unit)
                )
            }
            else {
                _screenState.value = _screenState.value.copy(changeDataState =  State.Error((response as Response.Failure).error))
            }
        }
    }

    fun loadImage(image: String?) {
        _screenState.value = _screenState.value.copy(image = image)
    }

    fun isInputNameValid(text: String?): Boolean {
        val regex = Regex("[^A-Za-zА-Яа-я0-9 ]")
        return !text.isNullOrBlank() && !regex.containsMatchIn(text)
    }

    fun isInputDateValid(text: String?): Boolean {
        val regex = Regex("^\\d{2}\\.\\d{2}\\.\\d{4}$")
        return !text.isNullOrBlank() && regex.matches(text)
    }

    fun isInputPhoneValid(text: String?): Boolean {
        val len = text?.replace("[^\\d]".toRegex(), "")?.length
        return !text.isNullOrBlank() && len==11
    }
    fun isInputWeightValid(text: String?): Boolean {
        return !text.isNullOrBlank()
    }

    fun convertDateFormat(date: String): String {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate)
    }
}

sealed class ScreenEditProfileState {
    object Loading: ScreenEditProfileState()
    object LoadingError: ScreenEditProfileState()
    object LoadingSuccess: ScreenEditProfileState()
    object LoadingSuccessLocal: ScreenEditProfileState()
    object Success: ScreenEditProfileState()
    object Error: ScreenEditProfileState()
}