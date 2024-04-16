package com.example.sportikitochka.presentation.main.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.payment.AddCardRequest
import com.example.sportikitochka.data.models.request.payment.BuyPremiumRequest
import com.example.sportikitochka.data.models.request.payment.DeleteCardRequest
import com.example.sportikitochka.data.models.request.payment.EditCardRequest
import com.example.sportikitochka.data.models.response.activities.mapToSportActivity
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.models.response.payment.mapToCreditCard
import com.example.sportikitochka.data.models.response.profile.UserProfileResponse
import com.example.sportikitochka.domain.models.CreditCard
import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.domain.use_cases.auth.ChangeUserTypeUseCase
import com.example.sportikitochka.domain.use_cases.auth.SaveSessionUseCase
import com.example.sportikitochka.domain.use_cases.payment.AddCardUseCase
import com.example.sportikitochka.domain.use_cases.payment.BuyPremiumUseCase
import com.example.sportikitochka.domain.use_cases.payment.DeleteCardUseCase
import com.example.sportikitochka.domain.use_cases.payment.EditCardUseCase
import com.example.sportikitochka.domain.use_cases.payment.GetAllCardsUseCase
import com.example.sportikitochka.presentation.main.main.ScreenMainState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PaymentViewModel(
    private val addCardUseCase: AddCardUseCase,
    private val buyPremiumUseCase: BuyPremiumUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val editCardUseCase: EditCardUseCase,
    private val getAllCardsUseCase: GetAllCardsUseCase,
    private val changeUserTypeUseCase: ChangeUserTypeUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenPaymentState>()
    val screenState: LiveData<ScreenPaymentState> = _screenState

    private val _cards = MutableLiveData<List<CreditCard>>()
    val cards: LiveData<List<CreditCard>> = _cards

    fun fetchCards() {
        _screenState.value = ScreenPaymentState.Loading
        viewModelScope.launch {
            try {
                val cardsResponse = getAllCardsUseCase.execute()
                if (cardsResponse.isSuccessful){

                    val responseBody = cardsResponse.body()

                    if (responseBody!=null){
                        var list: MutableList<CreditCard> =
                            responseBody.map { cardResponse -> cardResponse.mapToCreditCard() }.toMutableList()
                        list.add(CreditCard())
                        _cards.postValue(list)

                        _screenState.value = ScreenPaymentState.CardsLoaded
                    }
                }
                else {
                    _screenState.value = ScreenPaymentState.CardsLoadingError
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.value = ScreenPaymentState.CardsLoadingError
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.value = ScreenPaymentState.CardsLoadingError
            }
        }
    }

    fun addCard(
        cardName : String,
         cardNumber: String,
         month: Int,
         year: Int,
         cvv: Int
    ) {
        _screenState.value = ScreenPaymentState.Loading
        viewModelScope.launch {
            try {
                val addResponse = addCardUseCase.execute(AddCardRequest(cardName, cardNumber, month, year, cvv))
                if (addResponse.isSuccessful){

                    val responseBody = addResponse.body()

                    if (responseBody!=null && responseBody.success){
                        _screenState.value = ScreenPaymentState.CardOperationSuccess
                    }
                }
                else {
                    _screenState.value = ScreenPaymentState.CardOperationError
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.value = ScreenPaymentState.CardOperationError
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.value = ScreenPaymentState.CardOperationError
            }
        }
    }

    fun editCard(
        cardName : String,
        cardNumber: String,
        month: Int,
        year: Int,
        cvv: Int
    ) {
        _screenState.value = ScreenPaymentState.Loading
        viewModelScope.launch {
            try {
                val editResponse = editCardUseCase.execute(EditCardRequest(cardName, cardNumber, month, year, cvv))
                if (editResponse.isSuccessful){

                    val responseBody = editResponse.body()

                    if (responseBody!=null && responseBody.success){
                        _screenState.value = ScreenPaymentState.CardOperationSuccess
                    }
                }
                else {
                    _screenState.value = ScreenPaymentState.CardOperationError
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.value = ScreenPaymentState.CardOperationError
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.value = ScreenPaymentState.CardOperationError
            }
        }
    }

    fun deleteCard(
        cardNumber : String
    ) {
        _screenState.value = ScreenPaymentState.Loading
        viewModelScope.launch {
            try {
                val deleteResponse = deleteCardUseCase.execute(DeleteCardRequest(cardNumber))
                if (deleteResponse.isSuccessful){

                    val responseBody = deleteResponse.body()

                    if (responseBody!=null && responseBody.success){
                        _screenState.value = ScreenPaymentState.CardOperationSuccess
                    }
                }
                else {
                    _screenState.value = ScreenPaymentState.CardOperationError
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.value = ScreenPaymentState.CardOperationError
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.value = ScreenPaymentState.CardOperationError
            }
        }
    }

    fun buyPremium(
        cardName : String,
        cardNumber: String,
        month: Int,
        year: Int,
        cvv: Int
    ) {
        _screenState.value = ScreenPaymentState.Loading
        viewModelScope.launch {
            try {
                val buyResponse = buyPremiumUseCase.execute(BuyPremiumRequest(cardName, cardNumber, month, year, cvv))
                if (buyResponse.isSuccessful){

                    val responseBody = buyResponse.body()

                    if (responseBody!=null && responseBody.success){
                        _screenState.value = ScreenPaymentState.BuyingSuccess
                        changeUserTypeUseCase.execute(UserType.Premium)
                    }
                }
                else {
                    _screenState.value = ScreenPaymentState.BuyingError
                }
            }
            catch (httpException: HttpException) {
                Log.e("TAG", httpException.toString())
                _screenState.value = ScreenPaymentState.BuyingError
            } catch (exception: Exception) {
                Log.e("TAG", exception.toString())
                _screenState.value = ScreenPaymentState.BuyingError
            }
        }
    }
}