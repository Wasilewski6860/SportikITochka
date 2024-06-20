package com.example.sportikitochka.presentation.auth.reset_password

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.auth.AdminRegisterRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.domain.use_cases.auth.RegisterAdminUseCase
import com.example.sportikitochka.domain.use_cases.auth.RegisterUseCase
import com.example.sportikitochka.domain.use_cases.auth.ValidateEmailUseCase
import com.example.sportikitochka.domain.use_cases.reset_password.ConfirmCodeUseCase
import com.example.sportikitochka.domain.use_cases.reset_password.ResetPasswordUseCase
import com.example.sportikitochka.domain.use_cases.reset_password.SendToEmailUseCase
import com.example.sportikitochka.other.Validator
import com.example.sportikitochka.presentation.auth.sign_up.SignUpScreenState
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

sealed interface ResetPasswordScreenState {
    object Loading: ResetPasswordScreenState
    class SendToEmailSuccess(val code: String): ResetPasswordScreenState
    object SendToEmailError: ResetPasswordScreenState
    object ConfirmCodeSuccess : ResetPasswordScreenState
    object ConfirmCodeError : ResetPasswordScreenState
    object ResetPasswordSuccess : ResetPasswordScreenState
    object ResetPasswordError : ResetPasswordScreenState
    object AnyError : ResetPasswordScreenState
}

class ResetPasswordViewModel(
    private val confirmCodeUseCase: ConfirmCodeUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val sendToEmailUseCase: SendToEmailUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<ResetPasswordScreenState>()
    val screenState: LiveData<ResetPasswordScreenState> = _screenState

    fun sendEmail(email: String) {
        _screenState.value = ResetPasswordScreenState.Loading
        viewModelScope.launch {
            try {
                val response = sendToEmailUseCase.execute(email)
                if (response.isSuccessful) {
                    val body = response.body()
                    if ( body != null && body.message=="Code sent to email") {
                        _screenState.value = ResetPasswordScreenState.SendToEmailSuccess(body.code)
                    }
                    else {
                        val error = response.errorBody()?.source()?.let { source ->
                            Buffer().use { buffer ->
                                source.readAll(buffer)
                                buffer.readUtf8()
                            }
                        }
                        Log.e("SEND_EMAIL_ERROR", error!!)
                        _screenState.postValue(ResetPasswordScreenState.SendToEmailError)
                    }
                }
                else {
                    val error = response.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    Log.e("SEND_EMAIL_ERROR", error!!)
                    _screenState.postValue(ResetPasswordScreenState.SendToEmailError)
                }
            }
            catch (httpException: HttpException) {
                Log.e("SEND_EMAIL HTTP-EXCEPTION", httpException.toString())
                _screenState.postValue(ResetPasswordScreenState.SendToEmailError)

            } catch (exception: Exception) {
                Log.e("SEND_EMAIL EXCEPTION", exception.toString())
                _screenState.postValue(ResetPasswordScreenState.SendToEmailError)
            }
        }
    }

    fun confirmCode(code: String, receivedCode: String) {
        _screenState.value = ResetPasswordScreenState.Loading
        viewModelScope.launch {
            try {
                val response = confirmCodeUseCase.execute(code, receivedCode)
                if (response.isSuccessful) {
                    val body = response.body()
                    if ( body != null && body.success) {
                        _screenState.value = ResetPasswordScreenState.ConfirmCodeSuccess
                    }
                    else {
                        val error = response.errorBody()?.source()?.let { source ->
                            Buffer().use { buffer ->
                                source.readAll(buffer)
                                buffer.readUtf8()
                            }
                        }
                        Log.e("CONFIRM_CODE_ERROR", error!!)
                        _screenState.postValue(ResetPasswordScreenState.ConfirmCodeError)
                    }
                }
                else {
                    val error = response.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    Log.e("CONFIRM_CODE_ERROR", error!!)
                    _screenState.postValue(ResetPasswordScreenState.ConfirmCodeError)
                }
            }
            catch (httpException: HttpException) {
                Log.e("CONFIRM_CODE HTTP-EXCEPTION", httpException.toString())
                _screenState.postValue(ResetPasswordScreenState.ConfirmCodeError)

            } catch (exception: Exception) {
                Log.e("CONFIRM_CODE EXCEPTION", exception.toString())
                _screenState.postValue(ResetPasswordScreenState.ConfirmCodeError)
            }
        }
    }

    fun resetPassword(email: String,password: String, confirmPassword: String) {
        _screenState.value = ResetPasswordScreenState.Loading
        viewModelScope.launch {
            try {
                val response = resetPasswordUseCase.execute(email,password, confirmPassword)
                if (response.isSuccessful) {
                    val body = response.body()
                    if ( body != null && body.success) {
                        _screenState.value = ResetPasswordScreenState.ResetPasswordSuccess
                    }
                    else {
                        val error = response.errorBody()?.source()?.let { source ->
                            Buffer().use { buffer ->
                                source.readAll(buffer)
                                buffer.readUtf8()
                            }
                        }
                        Log.e("RESET_PASS_ERROR", error!!)
                        _screenState.postValue(ResetPasswordScreenState.ResetPasswordError)
                    }
                }
                else {
                    val error = response.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    Log.e("RESET_PASS_ERROR", error!!)
                    _screenState.postValue(ResetPasswordScreenState.ResetPasswordError)
                }
            }
            catch (httpException: HttpException) {
                Log.e(" RESET_PASS HTTP-EXCEPTION", httpException.toString())
                _screenState.postValue(ResetPasswordScreenState.ResetPasswordError)

            } catch (exception: Exception) {
                Log.e("RESET_PASS EXCEPTION", exception.toString())
                _screenState.postValue(ResetPasswordScreenState.ResetPasswordError)
            }
        }
    }

}