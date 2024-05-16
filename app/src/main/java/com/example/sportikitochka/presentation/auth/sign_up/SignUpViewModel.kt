package com.example.sportikitochka.presentation.auth.sign_up

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.auth.AdminRegisterRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.domain.use_cases.auth.RegisterAdminUseCase
import com.example.sportikitochka.domain.use_cases.auth.RegisterUseCase
import com.example.sportikitochka.domain.use_cases.auth.ValidateEmailUseCase
import com.example.sportikitochka.other.Validator
import com.example.sportikitochka.other.Validator.validatePassword
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Locale

class SignUpViewModel(
    private val signUpUseCase: RegisterUseCase,
    private val signUpAdminUseCase: RegisterAdminUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
) : ViewModel() {

    private val _screenState = MutableLiveData<SignUpScreenState>()
    val screenState: LiveData<SignUpScreenState> = _screenState


    fun signUp(
        name : String,
        email : String,
        password : String,
        birthday : String,
        phone : String,
        weight : String,
        image: String,
        isAdmin: Boolean
    ){
        _screenState.value = SignUpScreenState.Loading
        viewModelScope.launch {
            try {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) // формат даты
                val date = dateFormat.parse(birthday) // парсинг даты из строки
                val timestamp = date.time // получение времени в миллисекундах

                val convertedBirthday = convertDateFormat(birthday)
                if(!isAdmin) {
                    val registerResponse = signUpUseCase.execute(
                        email = email,
                        RegisterRequest(
                            name = name,
                            weight = weight.toInt(),
                            image = image,
                            birthday = convertedBirthday,
                            password = password,
                            phone = phone,

                            )
                    )

                    if (registerResponse.isSuccessful) {
                        _screenState.value = SignUpScreenState.Success
                    }
                    else {
                        val error = registerResponse.errorBody()?.source()?.let { source ->
                            Buffer().use { buffer ->
                                source.readAll(buffer)
                                buffer.readUtf8()
                            }
                        }
                        Log.e("REGISTER_ERROR", error!!)
                        Log.e("BIRTHDAY", convertedBirthday)
                        _screenState.postValue(SignUpScreenState.AnyError)
                    }
                }

                else {
                    val registerAdminResponse = signUpAdminUseCase.execute(
                        email = email,
                        AdminRegisterRequest(
                            name = name,
                            image = image,
                            birthday = convertedBirthday,
                            password = password,
                            phone = phone,

                            )
                    )

                    if (registerAdminResponse.isSuccessful) {
                        _screenState.value = SignUpScreenState.Success
                    }
                    else {
                        val error = registerAdminResponse.errorBody()?.source()?.let { source ->
                            Buffer().use { buffer ->
                                source.readAll(buffer)
                                buffer.readUtf8()
                            }
                        }
                        Log.e("REGISTER_ERROR", error!!)
                        Log.e("BIRTHDAY", convertedBirthday)
                        _screenState.postValue(SignUpScreenState.AnyError)
                    }
                }




            } catch (httpException: HttpException) {
                Log.e("HTTP-EXCEPTION", httpException.toString())
                _screenState.postValue(SignUpScreenState.AnyError)

            } catch (exception: Exception) {
                Log.e("EXCEPTION", exception.toString())
                _screenState.postValue(SignUpScreenState.AnyError)
            }
        }
    }

    fun isInputNameValid(text: String?): Boolean {
        val regex = Regex("[^A-Za-z0-9 ]")
        return !text.isNullOrBlank() && !regex.containsMatchIn(text)
    }

    fun isInputEmailValid(email : String?) = Validator.validateEmail(email)
    fun isInputPasswordValid(password : String?) = validatePassword(password)

    fun validateEmail(email: String) {
        _screenState.value = SignUpScreenState.Loading
        viewModelScope.launch {
            _screenState.postValue(SignUpScreenState.ValidationSuccess)
//            try {
//                val validateResponse = validateEmailUseCase.execute(
//                    email = email
//                )
//                if (validateResponse.isSuccessful) {
//                    val validateResponseBody = validateResponse.body()
//                    validateResponseBody?.let {
//                        if (it.free) {
//                            _screenState.postValue(SignUpScreenState.ValidationSuccess)
//                        }
//                        else {
//                            _screenState.postValue(SignUpScreenState.ValidationError)
//                        }
//                    }
//                }
//                else {
//                    Log.e("VALIDATION_EMAIL_ERROR", "validation_email_error")
//                    _screenState.postValue(SignUpScreenState.ValidationError)
//                }
//            } catch (httpException: HttpException) {
//                Log.e("HTTP-EXCEPTION", httpException.toString())
//                _screenState.postValue(SignUpScreenState.ValidationError)
//
//            } catch (exception: Exception) {
//                Log.e("EXCEPTION", exception.toString())
//                _screenState.postValue(SignUpScreenState.ValidationError)
//            }
        }
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