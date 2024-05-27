package com.example.sportikitochka.presentation.main.edit_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.profile.ProfilePeriod
import com.example.sportikitochka.data.models.request.profile.UserProfileRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeAdminDataRequest
import com.example.sportikitochka.data.models.request.user_data.ChangeDataUserRequest
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.data.models.response.user_data.UserDataResponse
import com.example.sportikitochka.domain.use_cases.auth.GetUserRoleUseCase
import com.example.sportikitochka.domain.use_cases.auth.SignOutUseCase
import com.example.sportikitochka.domain.use_cases.profile.GetProfileUseCase
import com.example.sportikitochka.domain.use_cases.user_data.ChangeAdminDataUseCase
import com.example.sportikitochka.domain.use_cases.user_data.ChangeUserDataUseCase
import com.example.sportikitochka.domain.use_cases.user_data.GetAdminDataUseCase
import com.example.sportikitochka.domain.use_cases.user_data.GetUserDataLocallyUseCase
import com.example.sportikitochka.domain.use_cases.user_data.GetUserDataUseCase
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class EditProfileViewModel(
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getAdminDataUseCase: GetAdminDataUseCase,
    private val getUserDataLocallyUseCase: GetUserDataLocallyUseCase,
    private val changeUserDataUseCase: ChangeUserDataUseCase,
    private val changeAdminDataUseCase: ChangeAdminDataUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenEditProfileState>()
    val screenState: LiveData<ScreenEditProfileState> = _screenState

    private val _userInfo = MutableLiveData<UserDataResponse>()
    val userInfo: LiveData<UserDataResponse> = _userInfo

    fun getUserRole() = getUserRoleUseCase.execute()



    fun loadUserData() {
        _screenState.postValue(ScreenEditProfileState.Loading)
        viewModelScope.launch {
            try {
                val userType: UserType? = getUserRole()
                if (userType == UserType.Admin) {
                    val adminDataResponse = getAdminDataUseCase.execute()
                    if (adminDataResponse.isSuccessful) {
                        var responseBody = adminDataResponse.body()

                        if (responseBody != null) {
                            val body = UserDataResponse(
                                id = 0,//TODO
                                name = responseBody.name,
                                image = responseBody.image,
                                weight = -100F,
                                phone = responseBody.phone,
                                birthday = responseBody.birthday.toString()
                            )
                            _userInfo.postValue(body)
                            _screenState.postValue(ScreenEditProfileState.LoadingSuccess)
                        }
                        else _screenState.postValue(ScreenEditProfileState.LoadingError)
                    } else {
                        _screenState.postValue(ScreenEditProfileState.LoadingError)
                    }
                }
                else {
                    val userDataResponse = getUserDataUseCase.execute()
                    if (userDataResponse.isSuccessful) {
                        var responseBody = userDataResponse.body()

                        if (responseBody != null) {
                            _userInfo.postValue(responseBody!!)
                            _screenState.postValue(ScreenEditProfileState.LoadingSuccess)
                        }
                        else {
                            val userData = getUserDataLocallyUseCase.execute()
                            if (userData != null) {
                                _userInfo.postValue(userData!!)
                                _screenState.postValue(ScreenEditProfileState.LoadingSuccessLocal)
                            }
                            else _screenState.postValue(ScreenEditProfileState.LoadingError)
                        }

                    } else {
                        val error = userDataResponse.errorBody()?.source()?.let { source ->
                            Buffer().use { buffer ->
                                source.readAll(buffer)
                                buffer.readUtf8()
                            }
                        }
                        error?.let { Log.e("GET USER DATA REMOTE ERROR", it) }

                        val userData = getUserDataLocallyUseCase.execute()
                        if (userData != null) {
                            _userInfo.postValue(userData!!)
                            _screenState.postValue(ScreenEditProfileState.LoadingSuccessLocal)
                        }
                        else {
                            error?.let { Log.e("GET USER DATA LOCAL ERROR", it) }
                            _screenState.postValue(ScreenEditProfileState.LoadingError)
                        }
                    }
                }

            } catch (httpException: HttpException) {
                Log.e("GET_USER_DATA_HTTP_EXCEPTION", httpException.toString())
                _screenState.postValue(ScreenEditProfileState.LoadingError)
            } catch (exception: Exception) {
                Log.e("GET_USER_DATA_EXCEPTION", exception.toString())
                _screenState.postValue(ScreenEditProfileState.LoadingError)
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
        viewModelScope.launch {
            try {


                val convertedBirthday = convertDateFormat(birthday)

                val saveResponse = if (getUserRole() == UserType.Admin) {
                    changeAdminDataUseCase.execute(
                        ChangeAdminDataRequest(
                            name = name,
                            image = image,
                            phone = phone,
                            birthday = convertedBirthday
                        )
                    )
                }
                else {
                    changeUserDataUseCase.execute(
                        ChangeDataUserRequest(
                            name = name,
                            image = image,
                            weight = weight!!.toInt(), //TODO
                            phone = phone,
                            birthday = convertedBirthday
                        )
                    )
                }

                if (saveResponse.isSuccessful) {
                    _screenState.postValue(ScreenEditProfileState.Success)
                } else {
                    _screenState.postValue(ScreenEditProfileState.Error)
                }
            } catch (httpException: HttpException) {
                Log.e("CHANGE_USER_DATA_HTTP_EXCEPTION", httpException.toString())
                _screenState.postValue(ScreenEditProfileState.Error)
            } catch (exception: Exception) {
                Log.e("CHANGE_USER_DATA_EXCEPTION", exception.toString())
                _screenState.postValue(ScreenEditProfileState.Error)
            }
        }
    }

    fun isInputNameValid(text: String?): Boolean {
        val regex = Regex("[^A-Za-z0-9 ]")
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