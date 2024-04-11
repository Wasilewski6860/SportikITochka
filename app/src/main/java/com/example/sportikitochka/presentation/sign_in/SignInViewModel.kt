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
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.adapter.rxjava2.Result.response
import java.io.IOException
import java.util.Objects


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

                    val errorString: String? = loginResponse.errorBody()?.string()
                    try {
                        errorString?.let {
                            _screenState.postValue(
                                if (it.contains("USER_BLOCKED")) SignInScreenState.UserBlockedError
                                else if (it.contains("USER_NOT_FOUND")) SignInScreenState.UserNotFoundError
                                else if (it.contains("INCORRECT_PASSWORD")) SignInScreenState.IncorrectPasswordError
                                else SignInScreenState.AnyError
                            )
                        }
                       
                    } catch (e: IOException) {
                        _screenState.postValue(SignInScreenState.AnyError)
                    }

//
//                    val errorBody: ErrorResponse = loginResponse.errorBody()?.string() as ErrorResponse
//                    _screenState.postValue(
//                        when(errorBody.error) {
//                            "USER_BLOCKED" -> SignInScreenState.UserBlockedError
//                            "USER_NOT_FOUND" -> SignInScreenState.UserNotFoundError
//                            "INCORRECT_PASSWORD" -> SignInScreenState.IncorrectPasswordError
//                            else -> SignInScreenState.AnyError
//                        }
//                    )
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