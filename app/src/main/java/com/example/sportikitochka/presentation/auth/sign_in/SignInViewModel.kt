package com.example.sportikitochka.presentation.auth.sign_in

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.request.auth.LoginRequest
import com.example.domain.coroutines.Response
import com.example.domain.models.UserSession
import com.example.domain.use_cases.auth.GetUserRoleUseCase
import com.example.domain.use_cases.auth.IsLoggedUseCase
import com.example.domain.use_cases.auth.LoginUseCase
import com.example.domain.use_cases.auth.SaveSessionUseCase
import com.example.sportikitochka.common.State
import com.example.sportikitochka.presentation.auth.reset_password.ResetNavigationState
import com.example.sportikitochka.presentation.auth.reset_password.ResetPasswordScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException
import java.io.IOException

data class SignInScreenState(
    val signInState: State<UserSession>
)
class SignInViewModel(
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val isLoggedUseCase: IsLoggedUseCase,
    private val loginUseCase: LoginUseCase,
    private val saveSessionUseCase: SaveSessionUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<SignInScreenState>(
        SignInScreenState(State.NotStarted)
    )
    val screenState: StateFlow<SignInScreenState> = _screenState

    fun isLogged() = isLoggedUseCase.execute()
    fun getUserRole() = getUserRoleUseCase.execute()

    fun login(email : String, password : String) {
        _screenState.value = _screenState.value.copy(State.Loading)
        viewModelScope.launch {
            val result = loginUseCase.execute(email, password)
            if (result is Response.Success) {
                saveSessionUseCase.execute(result.value)
                _screenState.value = _screenState.value.copy(State.Success(result.value))
            }
            else _screenState.value = _screenState.value.copy(State.Error((result as Response.Failure).error))
        }
    }

}