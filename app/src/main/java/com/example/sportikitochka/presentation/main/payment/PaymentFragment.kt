package com.example.sportikitochka.presentation.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.FragmentPaymentBinding
import com.example.sportikitochka.domain.models.CreditCard
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.Revenue
import org.koin.androidx.viewmodel.ext.android.viewModel
import world.mappable.runtime.Runtime.getApplicationContext
import java.util.Currency


class PaymentFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentFragment()
    }
    private val viewModel: PaymentViewModel by viewModel()
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var cardsAdapter: CardsAdapter
    protected lateinit var connectionLiveData: ConnectionLiveData

    private var isOnline : Boolean = false


    private var selectedCard: CreditCard? = null
    private var selectedCardLiveData: MutableLiveData<CreditCard?>  = MutableLiveData<CreditCard?>(null)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this.requireContext())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        connectionLiveData.observe(viewLifecycleOwner) {
            isOnline = it
            if(it)viewModel.fetchCards()
        }
        setupRecyclerView()
        viewModel.cards.observe(viewLifecycleOwner) {
            cardsAdapter.submitList(it)
        }
        binding.errorLayoutButton.setOnClickListener {
            viewModel.fetchCards()
        }
        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                ScreenPaymentState.BuyingError -> {
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.VISIBLE
                        cardLayout.visibility = View.GONE
                        showSnackbar("К сожалению, не удалось выполнить операцию", requireActivity().findViewById(R.id.rootViewMain))
                    }
                }
                ScreenPaymentState.BuyingSuccess -> {

//                    AppMetrica.reportEvent("Premium bought")
//                    val revenue = Revenue.newBuilder(100, Currency.getInstance("RUB"))
//                        .withProductID("com.example.sportikitochka")
//                        .withQuantity(1)
//                        .build()
                    // Sending the Revenue instance using reporter.
//                    AppMetrica.getReporter(getApplicationContext(), "0ee9624d-3997-4356-94d4-052ff53bb44d")
//                        .reportRevenue(revenue)

                    findNavController().navigate(
                        R.id.action_paymentFragment_to_profileFragment,
                        savedInstanceState
                    )
                }
                ScreenPaymentState.CardOperationError -> {
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.VISIBLE
                        cardLayout.visibility = View.GONE
                        showSnackbar("К сожалению, не удалось выполнить операцию", requireActivity().findViewById(R.id.rootViewMain))
                    }
                }
                ScreenPaymentState.CardOperationSuccess -> {
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.VISIBLE
                        cardLayout.visibility = View.GONE
                    }
                    viewModel.fetchCards()
                }
                ScreenPaymentState.CardsLoaded -> {
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.VISIBLE
                        cardLayout.visibility = View.GONE
                    }
                }
                ScreenPaymentState.CardsLoadingError -> {
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.VISIBLE
                        contentLayout.visibility = View.GONE
                        cardLayout.visibility = View.GONE
                    }
                }
                ScreenPaymentState.Loading -> {
                    with(binding) {
                        loadingLayout.visibility = View.VISIBLE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.GONE
                        cardLayout.visibility = View.GONE
                    }
                }

                ScreenPaymentState.BuyingError -> TODO()
                ScreenPaymentState.BuyingSuccess -> TODO()
                ScreenPaymentState.CardOperationError -> TODO()
                ScreenPaymentState.CardOperationSuccess -> TODO()
                ScreenPaymentState.CardsLoaded -> TODO()
                ScreenPaymentState.CardsLoadingError -> TODO()
                ScreenPaymentState.Loading -> TODO()
                ScreenPaymentState.BuyingError -> TODO()
                ScreenPaymentState.BuyingSuccess -> TODO()
                ScreenPaymentState.CardOperationError -> TODO()
                ScreenPaymentState.CardOperationSuccess -> TODO()
                ScreenPaymentState.CardsLoaded -> TODO()
                ScreenPaymentState.CardsLoadingError -> TODO()
                ScreenPaymentState.Loading -> TODO()
            }
        }
        selectedCardLiveData.observe(viewLifecycleOwner) {
            selectedCard = it
            if (it != null) {
                binding.carnNumberTv.text = "*"+it.cardNumber?.substring(12)
            }
            else {
                binding.carnNumberTv.text = "Не выбрано"
            }
        }
        with(binding) {
            payButton.setOnClickListener {
                selectedCard?.let {
                    viewModel.buyPremium(it.cardName!!,it.cardNumber!!, it.month!!, it.year!!, it.cvv!!)
                }
            }
            contentLayout.visibility = View.VISIBLE
            bottomCardView.visibility = View.GONE
            cardLayout.visibility = View.GONE
        }
        // TODO: Use the ViewModel
//        requireActivity().findViewById<com.github.credit_card_view.CreditCardView>(R.id.creditCardView).setBankName("TINKOFF")
//
//        val imageResource = R.raw.tinkoff_logo // идентификатор ресурса изображения
//        val inputStream = resources.openRawResource(imageResource)
//        val bitmap = BitmapFactory.decodeStream(inputStream)
//        requireActivity().findViewById<com.github.credit_card_view.CreditCardView>(R.id.creditCardView).setCardProviderLogo(bitmap)
    }

    private fun setupRecyclerView() = binding.recycler.apply {
        cardsAdapter = CardsAdapter(
            cardActionListener = object : CardsAdapter.CardActionListener {
                override fun onClickItem(card: CreditCard) {
                    selectedCard = card
                    selectedCardLiveData.postValue(card)

                    with(binding) {
                        contentLayout.visibility = View.VISIBLE
                        bottomCardView.visibility = View.VISIBLE
                        cardLayout.visibility = View.GONE
                    }
                }
            },
            buttonActionListener = object : CardsAdapter.CardActionListener {
                override fun onClickItem(card: CreditCard) {
                    showSnackbar("Не удалось совершить оплату", requireActivity().findViewById(R.id.rootViewMain))
                    //TODO Редактирование карты
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.GONE
                        bottomCardView.visibility = View.GONE
                        cardLayout.visibility = View.VISIBLE

                        confirmButton.setOnClickListener {
                            //TODO сделать проверку на валидность
                            val cardValue = creditCardView.getCardValue()
                            val expirityDate = cardValue.cardValidDate
                            val name = cardValue.cardUsersName
                            val number = cardValue.cardNumber
                            val cvv = cardValue.cardCvcNumber

                            if (validate()) {
                                val month = expirityDate?.substring(0,1)
                                val year = expirityDate?.substring(2,3)
                                viewModel.addCard(
                                    name!!,
                                    number!!,
                                    month!!.toInt(),
                                    year!!.toInt(),
                                    cvv!!.toInt(),
                                )
                                selectedCardLiveData.postValue(
                                    CreditCard(
                                        name,
                                        number,
                                        month.toInt(),
                                        year.toInt(),
                                        cvv.toInt(),
                                    )
                                )
                                contentLayout.visibility = View.VISIBLE
                                bottomCardView.visibility = View.VISIBLE
                                cardLayout.visibility = View.GONE
                            }

                        }
                        deleteButton.visibility = View.VISIBLE
                    }

                }
            },
            newCardClickActionListener = object : CardsAdapter.CardActionListener {
                override fun onClickItem(card: CreditCard) {
                    //TODO Редактирование карты
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.GONE
                        bottomCardView.visibility = View.GONE
                        cardLayout.visibility = View.VISIBLE

                        confirmButton.setOnClickListener {
                            //TODO сделать проверку на валидность
                            val cardValue = creditCardView.getCardValue()
                            val expirityDate = cardValue.cardValidDate
                            val name = cardValue.cardUsersName
                            val number = cardValue.cardNumber
                            val cvv = cardValue.cardCvcNumber
                            if (validate()) {
                                val month = expirityDate?.substring(0,2)
                                val year = expirityDate?.substring(2,4)
                                viewModel.addCard(
                                    name!!,
                                    number!!,
                                    month!!.toInt(),
                                    year!!.toInt(),
                                    cvv!!.toInt(),
                                )
                                selectedCardLiveData.postValue(
                                    CreditCard(
                                        name,
                                        number,
                                        month.toInt(),
                                        year.toInt(),
                                        cvv!!.toInt(),
                                    )
                                )
                                contentLayout.visibility = View.VISIBLE
                                bottomCardView.visibility = View.VISIBLE
                                cardLayout.visibility = View.GONE
                            }

                        }
                        deleteButton.visibility = View.GONE
                    }

                }
            }
        )
        adapter = cardsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun validate(): Boolean  {
        with(binding) {
            val expirityDate = creditCardView.cardValidDate
            val expitityRegex = Regex("^\\d{4}$")
            val cardValue = creditCardView.getCardValue()
            val name = cardValue.cardUsersName
            val number = cardValue.cardNumber
            val cvv = cardValue.cardCvcNumber

            if (expirityDate == null || !expitityRegex.matches(expirityDate)){
                showSnackbar("Неверно введен конец срока действия карты", requireActivity().findViewById(R.id.rootViewMain))
            }
            else if (name == null || name.isNullOrBlank()) {
                showSnackbar("Неверно введено имя владельца", requireActivity().findViewById(R.id.rootViewMain))
            }
            else if (number == null || number.isNullOrBlank()) {
                showSnackbar("Неверно введен номер карты", requireActivity().findViewById(R.id.rootViewMain))
            }
            else if (cvv == null || cvv.isNullOrBlank()) {
                showSnackbar("Неверно введен cvv", requireActivity().findViewById(R.id.rootViewMain))
            }
            else {
                return true
            }
        }
        return false
    }
}