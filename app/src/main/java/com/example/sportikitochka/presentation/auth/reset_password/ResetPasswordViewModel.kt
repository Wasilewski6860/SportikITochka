package com.example.sportikitochka.presentation.auth.reset_password

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.coroutines.Response
import com.example.domain.use_cases.reset_password.ConfirmCodeUseCase
import com.example.domain.use_cases.reset_password.ResetPasswordUseCase
import com.example.domain.use_cases.reset_password.SendToEmailUseCase
import com.example.sportikitochka.common.State
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException

@Parcelize
data class ResetPasswordScreenState(
    val currentScreen: ResetNavigationState,
    val sendEmailState: State<String>,
    val confirmPasswordState: State<Unit>,
    val resetPasswordState: State<Unit>
): Parcelable

class ResetPasswordViewModel(
    private val confirmCodeUseCase: ConfirmCodeUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val sendToEmailUseCase: SendToEmailUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<ResetPasswordScreenState>(
        ResetPasswordScreenState(ResetNavigationState.FirstScreen, State.NotStarted, State.NotStarted, State.NotStarted)
    )
    val screenState: StateFlow<ResetPasswordScreenState> = _screenState


    fun sendEmail(email: String) {
        viewModelScope.launch {
            _screenState.emit(screenState.value.copy(sendEmailState = State.Loading))
            val result = sendToEmailUseCase.execute(email)
            _screenState.emit(
                screenState.value.copy(
                    sendEmailState = if (result is Response.Success) {
                        State.Success(result.value.code)
                    } else State.Error((result as Response.Failure).error)
                )
            )
        }
    }

    fun confirmCode(code: String, receivedCode: String) {
        viewModelScope.launch {
            _screenState.emit(screenState.value.copy(confirmPasswordState = State.Loading))
            val result = confirmCodeUseCase.execute(code, receivedCode)
            _screenState.emit(
                screenState.value.copy(
                    confirmPasswordState = if (result is Response.Success) {
                        State.Success(Unit)
                    } else State.Error((result as Response.Failure).error)
                )
            )
        }
    }

    fun resetPassword(email: String,password: String, confirmPassword: String) {
        viewModelScope.launch {
            _screenState.emit(screenState.value.copy(resetPasswordState = State.Loading))
            val result = resetPasswordUseCase.execute(email,password, confirmPassword)
            _screenState.emit(
                screenState.value.copy(
                    resetPasswordState = if (result is Response.Success) {
                        State.Success(Unit)
                    } else State.Error((result as Response.Failure).error)
                )
            )
        }
    }

    fun navigateToScreen(screen: ResetNavigationState) {
        _screenState.value = _screenState.value.copy(
            currentScreen = screen
        )
    }
}