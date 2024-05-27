package com.example.sportikitochka.presentation.main.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportikitochka.data.models.request.payment.BuyPremiumRequest
import com.example.sportikitochka.data.models.request.payment.DeleteCardRequest
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.domain.models.CreditCard
import com.example.sportikitochka.domain.use_cases.auth.ChangeUserTypeUseCase
import com.example.sportikitochka.domain.use_cases.auth.GetSessionUseCase
import com.example.sportikitochka.domain.use_cases.auth.SaveSessionUseCase
import com.example.sportikitochka.domain.use_cases.payment.BuyPremiumUseCase
import com.example.sportikitochka.domain.use_cases.payment.GetAllCardsUseCase
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException

class PaymentViewModel(
    private val buyPremiumUseCase: BuyPremiumUseCase,
    private val getAllCardsUseCase: GetAllCardsUseCase,
    private val changeUserTypeUseCase: ChangeUserTypeUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val getSessionUseCase: GetSessionUseCase,
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
                        if (responseBody.cardNumbers == null) {
                            var list: MutableList<CreditCard> = mutableListOf<CreditCard>()
                            list.add(CreditCard())
                            _cards.postValue(list)
                        }
                        else {
                            var list: MutableList<CreditCard> =
                                responseBody.cardNumbers.map { number -> CreditCard(cardNumber = number) }.toMutableList()
                            list.add(CreditCard())
                            _cards.postValue(list)
                        }
                        _screenState.value = ScreenPaymentState.CardsLoaded
                    }
                }
                else {
                    val error = cardsResponse.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    error?.let { Log.e("GET CARDS", it) }
                    _screenState.value = ScreenPaymentState.CardsLoadingError
                }
            }
            catch (httpException: HttpException) {
                Log.e("GET CARDS", httpException.toString())
                _screenState.value = ScreenPaymentState.CardsLoadingError
            } catch (exception: Exception) {
                Log.e("GET CARDS", exception.toString())
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
        viewModelScope.launch {
            val list: MutableList<CreditCard>? = _cards.value?.toMutableList()
            list?.add(CreditCard(cardName, cardNumber, month, year, cvv))
            list?.let {
                _cards.postValue(it)
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
                if (buyResponse.isSuccessful) {

                    val responseBody = buyResponse.body()

                    if (responseBody!=null && responseBody.success){
                        _screenState.value = ScreenPaymentState.BuyingSuccess
                        changeUserTypeUseCase.execute(UserType.Premium)
                        val session = getSessionUseCase.execute()
                        session?.role = UserType.Premium.toString()
                        session?.let {
                            saveSessionUseCase.execute(it)
                        }
                    }
                }
                else {
                    val error = buyResponse.errorBody()?.source()?.let { source ->
                        Buffer().use { buffer ->
                            source.readAll(buffer)
                            buffer.readUtf8()
                        }
                    }
                    error?.let { Log.e("BUY PREMIUM", it) }
                    _screenState.value = ScreenPaymentState.BuyingError
                }
            }
            catch (httpException: HttpException) {
                Log.e("BUY PREMIUM", httpException.toString())
                _screenState.value = ScreenPaymentState.BuyingError
            } catch (exception: Exception) {
                Log.e("BUY PREMIUM", exception.toString())
                if (exception.toString().startsWith("com.google.gson.JsonSyntaxException: java.lang.NumberFormatException: For input string:")) {
                    val session = getSessionUseCase.execute()
                    session?.role = UserType.Premium.toString()
                    session?.let {
                        saveSessionUseCase.execute(it)
                    }
                    _screenState.value = ScreenPaymentState.BuyingSuccess
                }
                else
                _screenState.value = ScreenPaymentState.BuyingError
            }
        }
    }
}