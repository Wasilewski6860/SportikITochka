package com.example.sportikitochka.presentation.main.payment

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportikitochka.R
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.databinding.FragmentMainBinding
import com.example.sportikitochka.databinding.FragmentPaymentBinding
import com.example.sportikitochka.domain.models.CreditCard
import com.example.sportikitochka.domain.models.User
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import com.example.sportikitochka.presentation.main.main.MainAdapter
import com.example.sportikitochka.presentation.main.main.MainViewModel
import com.example.sportikitochka.presentation.main.rating.RatingAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        viewModel.cards.observe(viewLifecycleOwner) {
            cardsAdapter.submitList(it)
        }
        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                ScreenPaymentState.BuyingError -> {
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.GONE
                        cardLayout.visibility = View.VISIBLE
                        showSnackbar("К сожалению, не удалось выполнить операцию", requireActivity().findViewById(R.id.rootView))
                    }
                }
                ScreenPaymentState.BuyingSuccess -> {
                    findNavController().navigate(
                        R.id.action_paymentFragment_to_profileFragment,
                        savedInstanceState
                    )
                }
                ScreenPaymentState.CardOperationError -> {
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.GONE
                        cardLayout.visibility = View.VISIBLE
                        showSnackbar("К сожалению, не удалось выполнить операцию", requireActivity().findViewById(R.id.rootView))
                    }
                }
                ScreenPaymentState.CardOperationSuccess -> {
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.VISIBLE
                        cardLayout.visibility = View.INVISIBLE
                    }
                    viewModel.fetchCards()
                }
                ScreenPaymentState.CardsLoaded -> {
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.VISIBLE
                        cardLayout.visibility = View.INVISIBLE
                    }
                }
                ScreenPaymentState.CardsLoadingError -> {
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.VISIBLE
                        contentLayout.visibility = View.GONE
                        cardLayout.visibility = View.INVISIBLE
                    }
                }
                ScreenPaymentState.Loading -> {
                    with(binding) {
                        loadingLayout.visibility = View.VISIBLE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.GONE
                        cardLayout.visibility = View.INVISIBLE
                    }
                }
            }
        }

        with(binding) {
            payButton.setOnClickListener {
                selectedCard?.let {
                    viewModel.buyPremium(it.cardName!!,it.cardNumber!!, it.month!!, it.year!!, it.cvv!!)
                }
            }
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
//                    else {
//                        with(binding) {
//                            loadingLayout.visibility = View.GONE
//                            errorLayout.visibility = View.GONE
//                            contentLayout.visibility = View.GONE
//                            cardLayout.visibility = View.VISIBLE
//
//                            confirmButton.setOnClickListener {
//                                //TODO Создание карты
//                                selectedCard?.let {
//                                    viewModel.addCard(it.cardName!!, it.cardNumber!!, it.month!!, it.year!!, it.cvv!!)
//                                }
//                            }
//                            deleteButton.visibility = View.GONE
//                        }
//                    }
                }
            },
            buttonActionListener = object : CardsAdapter.CardActionListener {
                override fun onClickItem(card: CreditCard) {
                    //TODO Редактирование карты
                    with(binding) {
                        loadingLayout.visibility = View.GONE
                        errorLayout.visibility = View.GONE
                        contentLayout.visibility = View.GONE
                        cardLayout.visibility = View.VISIBLE

                        confirmButton.setOnClickListener {
                            val expirityDate = creditCardView.getExpiryDate()
                            val month = creditCardView.getExpiryDate()!!.month
                            val year = creditCardView.getExpiryDate()!!.year
                            viewModel.editCard(
                                creditCardView.getNameOnCard()!!,
                                creditCardView.getCardNumber()!!,
                                creditCardView.getCvv()!!.toInt(),
                                month,
                                year
                            )
                        }
                        deleteButton.visibility = View.VISIBLE
                        deleteButton.setOnClickListener{
                            viewModel.deleteCard(card.cardNumber!!)
                        }
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
                        cardLayout.visibility = View.VISIBLE

                        confirmButton.setOnClickListener {
                            //TODO сделать проверку на валидность
                            val expirityDate = creditCardView.getExpiryDate()
                            val month = creditCardView.getExpiryDate()!!.month
                            val year = creditCardView.getExpiryDate()!!.year
                            viewModel.addCard(
                                creditCardView.getNameOnCard()!!,
                                creditCardView.getCardNumber()!!,
                                month,
                                year,
                                creditCardView.getCvv()!!.toInt(),
                            )
                        }
                        deleteButton.visibility = View.GONE
                    }

                }
            }
        )
        adapter = cardsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}