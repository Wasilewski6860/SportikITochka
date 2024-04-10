package com.example.sportikitochka.presentation.sign_in

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.response.ErrorResponse
import com.example.sportikitochka.domain.use_cases.auth.IsLoggedUseCase
import com.example.sportikitochka.domain.use_cases.auth.LoginUseCase
import com.example.sportikitochka.domain.use_cases.auth.SaveSessionUseCase
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignInViewModel(
    private val isLoggedUseCase: IsLoggedUseCase,
    private val loginUseCase: LoginUseCase,
    private val saveSessionUseCase: SaveSessionUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<SignInScreenState>()
    val screenState: LiveData<SignInScreenState> = _screenState

    fun isLogged() = isLoggedUseCase.execute()

    fun login(email : String, password : String) {
        _screenState.value = SignInScreenState.Loading
        viewModelScope.launch {
            try {
                val loginResponse = loginUseCase.execute(LoginRequest(email, password))
                if (loginResponse.isSuccessful) {
                    val loginResponseBody = loginResponse.body()
                    loginResponseBody?.let {
                        saveSessionUseCase.execute(it)
                    }
                }
                else {
                    val errorBody: ErrorResponse = loginResponse.errorBody()?.string() as ErrorResponse
                    _screenState.postValue(
                        when(errorBody.error) {
                            "USER_BLOCKED" -> SignInScreenState.UserBlockedError
                            "USER_NOT_FOUND" -> SignInScreenState.UserNotFoundError
                            "INCORRECT_PASSWORD" -> SignInScreenState.IncorrectPasswordError
                            else -> SignInScreenState.AnyError
                        }
                    )
                }
            } catch (httpException: HttpException) {
                Log.e("HTTP-EXCEPTION", httpException.toString())
                _screenState.postValue(SignInScreenState.AnyError)

            } catch (exception: Exception) {
                Log.e("EXCEPTION", exception.toString())
                _screenState.postValue(SignInScreenState.AnyError)
            }
        }
    }

}