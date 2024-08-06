package com.example.sportikitochka.presentation.auth.sign_up

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.request.auth.AdminRegisterRequest
import com.example.data.models.request.auth.RegisterRequest
import com.example.domain.coroutines.Response
import com.example.domain.models.UserSession
import com.example.domain.use_cases.auth.RegisterAdminUseCase
import com.example.domain.use_cases.auth.RegisterUseCase
import com.example.domain.use_cases.auth.ValidateEmailUseCase
import com.example.sportikitochka.common.State
import com.example.sportikitochka.other.Validator
import com.example.sportikitochka.other.Validator.validatePassword
import com.example.sportikitochka.presentation.auth.reset_password.ResetNavigationState
import com.example.sportikitochka.presentation.auth.sign_in.SignInScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

data class SignUpScreenState(
    val registrationState: State<Unit>,
    val validationState: State<Unit>,
    val navigationState: SignUpNavigationState,
    val image: String? = null
)

class SignUpViewModel(
    private val signUpUseCase: RegisterUseCase,
    private val signUpAdminUseCase: RegisterAdminUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow<SignUpScreenState>(
        SignUpScreenState(registrationState = State.NotStarted, navigationState =  SignUpNavigationState.FirstScreen, validationState = State.NotStarted)
    )
    val screenState: StateFlow<SignUpScreenState> = _screenState

    fun validateEmail(email : String) {
        _screenState.value = _screenState.value.copy(validationState = State.Loading)
        viewModelScope.launch {
            val result = validateEmailUseCase.execute(email)
            if (result is Response.Success) {
                _screenState.value = _screenState.value.copy(validationState = State.Success(result.value))
            }
            else _screenState.value = _screenState.value.copy(validationState = State.Error((result as Response.Failure).error))
        }
    }

    fun signUp(
        name : String,
        email : String,
        password : String,
        birthday : String,
        phone : String,
        weight : String,
        image: File,
        isAdmin: Boolean
    ){
        _screenState.value = _screenState.value.copy(registrationState = State.Loading)
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) // формат даты
            val date = dateFormat.parse(birthday) // парсинг даты из строки
            val timestamp = date.time // получение времени в миллисекундах
            val convertedBirthday = convertDateFormat(birthday)

            if(!isAdmin) {
                val result = signUpUseCase.execute(
                    email = email,
                    name = name,
                    weight = weight.toInt(),
                    image = image,
                    birthday = convertedBirthday,
                    password = password,
                    phone = phone
                )
                if (result is Response.Success) {
                    _screenState.value = _screenState.value.copy(registrationState = State.Success(result.value))
                }
                else _screenState.value = _screenState.value.copy(registrationState = State.Error((result as Response.Failure).error))
            }
            else {
                val result = signUpAdminUseCase.execute(
                    email = email,
                    name = name,
                    image = image,
                    birthday = convertedBirthday,
                    password = password,
                    phone = phone
                )
                if (result is Response.Success) {
                    _screenState.value = _screenState.value.copy(State.Success(result.value))
                }
                else _screenState.value = _screenState.value.copy(State.Error((result as Response.Failure).error))
            }
        }
    }

    fun loadImage(image: String?) {
        _screenState.value = _screenState.value.copy(image = image)
    }

    fun navigateToScreen(screen: SignUpNavigationState) {
        _screenState.value = _screenState.value.copy(
            navigationState = screen
        )
    }

    fun isInputNameValid(text: String?): Boolean {
        val regex = Regex("[^A-Za-zА-Яа-я0-9 ]")
        return !text.isNullOrBlank() && !regex.containsMatchIn(text)
    }

    fun isInputEmailValid(email : String?) = Validator.validateEmail(email)
    fun isInputPasswordValid(password : String?) = validatePassword(password)

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