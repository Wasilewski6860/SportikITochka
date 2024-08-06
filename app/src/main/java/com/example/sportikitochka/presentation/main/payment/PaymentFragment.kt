package com.example.sportikitochka.presentation.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.data.network.EndPoints
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NotFoundException
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.FragmentPaymentBinding
import com.example.domain.models.CreditCard
import com.example.sportikitochka.common.State
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.Revenue
import kotlinx.coroutines.launch
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
        initObservers()
        binding.errorLayoutButton.setOnClickListener {
            viewModel.fetchCards()
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
    }

    private fun setupRecyclerView() = binding.recycler.apply {
        cardsAdapter = CardsAdapter(
            cardActionListener = object : CardsAdapter.CardActionListener {
                override fun onClickItem(card: CreditCard) {
                    selectedCard = card
                    viewModel.selectCard(card)

                    with(binding) {
                        contentLayout.visibility = View.VISIBLE
                        bottomCardView.visibility = View.VISIBLE
                        cardLayout.visibility = View.GONE
                    }
                }
            },
            buttonActionListener = object : CardsAdapter.CardActionListener {
                override fun onClickItem(card: com.example.domain.models.CreditCard) {
                    showSnackbar("Не удалось совершить оплату", requireActivity().findViewById(R.id.rootViewMain))
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
                                viewModel.selectCard(
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
                override fun onClickItem(card: com.example.domain.models.CreditCard) {
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
                                viewModel.selectCard(
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

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect {
                    with(binding) {
                        when(it.cardsState) {
                            is State.Error -> {
                                loadingLayout.visibility = View.GONE
                                errorLayout.visibility = View.VISIBLE
                                contentLayout.visibility = View.GONE
                                cardLayout.visibility = View.GONE
                            }
                            State.Loading -> {
                                loadingLayout.visibility = View.VISIBLE
                                errorLayout.visibility = View.GONE
                                contentLayout.visibility = View.GONE
                                cardLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                loadingLayout.visibility = View.GONE
                                errorLayout.visibility = View.GONE
                                contentLayout.visibility = View.VISIBLE
                                cardLayout.visibility = View.GONE

                                cardsAdapter.submitList(it.cardsState.value)
                            }
                        }
                        when(it.buyPremiumState) {
                            is State.Error -> {
                                loadingLayout.visibility = View.GONE
                                errorLayout.visibility = View.VISIBLE
                                contentLayout.visibility = View.GONE
                                cardLayout.visibility = View.GONE

                                showSnackbar("К сожалению, не удалось выполнить операцию", requireActivity().findViewById(R.id.rootViewMain))
                            }
                            State.Loading -> {
                                loadingLayout.visibility = View.VISIBLE
                                errorLayout.visibility = View.GONE
                                contentLayout.visibility = View.GONE
                                cardLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                loadingLayout.visibility = View.GONE
                                errorLayout.visibility = View.GONE
                                contentLayout.visibility = View.VISIBLE
                                cardLayout.visibility = View.GONE
                                viewModel.fetchCards()
                            }
                        }
                        selectedCard = it.selectedCard
                            if (it.selectedCard != null) {
                            binding.carnNumberTv.text = "*"+it.selectedCard.cardNumber?.substring(12)
                        }
                        else {
                            binding.carnNumberTv.text = "Не выбрано"
                        }
                    }
                }
            }
        }
    }
}