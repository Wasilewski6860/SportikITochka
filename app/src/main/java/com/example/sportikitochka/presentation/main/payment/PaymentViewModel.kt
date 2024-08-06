package com.example.sportikitochka.presentation.main.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.request.payment.BuyPremiumRequest
import com.example.domain.coroutines.Response
import com.example.domain.models.CreditCard
import com.example.domain.models.UserType
import com.example.domain.use_cases.auth.ChangeUserTypeUseCase
import com.example.domain.use_cases.auth.GetSessionUseCase
import com.example.domain.use_cases.auth.SaveSessionUseCase
import com.example.domain.use_cases.payment.BuyPremiumUseCase
import com.example.domain.use_cases.payment.GetAllCardsUseCase
import com.example.sportikitochka.common.State
import com.example.sportikitochka.presentation.main.main.MainScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Buffer
import retrofit2.HttpException

data class PaymentState(
    val buyPremiumState: State<Unit> = State.NotStarted,
    val cardsState: State<List<CreditCard>> = State.Loading,
    val selectedCard: CreditCard? = null
)
class PaymentViewModel(
    private val buyPremiumUseCase: BuyPremiumUseCase,
    private val getAllCardsUseCase: GetAllCardsUseCase,
    private val changeUserTypeUseCase: ChangeUserTypeUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val getSessionUseCase: GetSessionUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow<PaymentState>(
        PaymentState()
    )
    val screenState: StateFlow<PaymentState> = _screenState


    fun fetchCards() {
        _screenState.value = _screenState.value.copy(cardsState = State.Loading)
        viewModelScope.launch {
            val result = getAllCardsUseCase.execute()
            if (result is Response.Success) {
                if (result.value==null) {
                    _screenState.value = _screenState.value.copy(cardsState = State.Success(listOf(CreditCard())))
                }
                else {
                    _screenState.value = _screenState.value.copy(cardsState = State.Success(result.value+CreditCard()))
                }
            }
            else {
                _screenState.value = _screenState.value.copy(State.Error((result as Response.Failure).error))
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
            val list: MutableList<CreditCard> = (_screenState.value.cardsState as State.Success<List<CreditCard>>).value.toMutableList()
            list.removeLast()
            list.add(CreditCard(cardName, cardNumber, month, year, cvv))
            list.add(CreditCard())
            list.let {
                _screenState.value.copy(cardsState = State.Success(it))
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
        _screenState.value = _screenState.value.copy(buyPremiumState = State.Loading)
        viewModelScope.launch {
            val result = buyPremiumUseCase.execute(
                cardName,
                cardNumber,
                month,
                year,
                cvv
            )
            if (result is Response.Success) {
                changeUserTypeUseCase.execute(UserType.Premium)
                val session = getSessionUseCase.execute()
                session?.role = UserType.Premium.toString()
                session?.let {
                    saveSessionUseCase.execute(it)
                }
                _screenState.value = _screenState.value.copy(buyPremiumState = State.Success(Unit))
            }
            else {
                _screenState.value = _screenState.value.copy(State.Error((result as Response.Failure).error))
            }
        }

    }

    fun selectCard(card: CreditCard) {
        _screenState.value = _screenState.value.copy(selectedCard = card)
    }
}