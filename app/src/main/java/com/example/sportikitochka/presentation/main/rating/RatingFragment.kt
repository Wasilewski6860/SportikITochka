package com.example.sportikitochka.presentation.main.rating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.sportikitochka.R
import com.example.data.network.EndPoints
import com.example.sportikitochka.databinding.FragmentRatingBinding
import com.example.domain.models.User
import com.example.domain.models.UserType
import com.example.sportikitochka.common.State
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatingFragment : Fragment() {

    companion object {
        fun newInstance() = RatingFragment()
    }

    private val viewModel: RatingViewModel by viewModel()
    private lateinit var ratingAdapter: RatingAdapter
    private var _binding: FragmentRatingBinding? = null
    private val binding get() = _binding!!

    protected lateinit var connectionLiveData: ConnectionLiveData
    private var isOnline : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this.requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        AppMetrica.reportEvent("Rating screen viewed")
        connectionLiveData.observe(viewLifecycleOwner) {
            isOnline = it
            if (it) {
                viewModel.fetchUsers()
                viewModel.loadUserProfile()
            }
            else {
                showSnackbar("Произошла ошибка при интернет-соединении", requireActivity().findViewById(R.id.rootViewMain))
            }

        }
        viewModel.getUserType()
        viewModel.loadUserProfile()
        viewModel.fetchUsers()

        initObservers()
    }

    private fun setupRecyclerView() = binding.recycler.apply {
        ratingAdapter = RatingAdapter(
            viewModel.getType() == UserType.Admin,
            object : RatingAdapter.UserPremiumActionListener {
                override fun onClickItem(user: User) {
                    if (isOnline){
                        if (user.role == UserType.Normal) viewModel.setPremium(user.id)
                        else viewModel.removePremium(user.id)
                    }
                    else {
                        showSnackbar("Нет интернет-соединения", requireActivity().findViewById(R.id.rootViewMain))
                    }
                }
            },
            object : RatingAdapter.UserBlockActionListener {
                override fun onClickItem(user: User) {
                    if (isOnline) viewModel.blockUser(user)
                    else showSnackbar("Нет интернет-соединения", requireActivity().findViewById(R.id.rootViewMain))
                }
            }
        )
        adapter = ratingAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect {
                    with(binding) {
                       when(it.userType) {
                           UserType.Admin -> {
                               binding.isAdminTv.visibility = View.VISIBLE
                               binding.premiumIvRating.visibility = View.GONE
                               binding.raitingTv.visibility = View.GONE
                               binding.isAdminTv.text = "Вы админ"
                           }
                           UserType.Normal -> {
                               binding.isAdminTv.visibility = View.GONE
                               binding.raitingTv.visibility = View.VISIBLE
                               binding.premiumIvRating.visibility = View.GONE
                           }
                           UserType.Premium -> {
                               binding.raitingTv.visibility = View.VISIBLE
                               binding.premiumIvRating.visibility = View.VISIBLE
                           }
                       }
                        when(it.adminProfileState) {
                            is State.Error -> {
                                binding.loadingLayout.visibility = View.GONE
                                binding.contentLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.VISIBLE
                                binding.errorLayoutTv.text = it.adminProfileState.error.message

                                binding.errorLayoutButton.setOnClickListener {
                                    viewModel.fetchUsers()
                                    viewModel.loadUserProfile()
                                }
                            }
                            State.Loading -> {
                                binding.loadingLayout.visibility = View.VISIBLE
                                binding.contentLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                binding.profileYou.text = "Вы, "
                                binding.profileNameRaiting.text = it.adminProfileState.value.name
                                Glide.with(this@RatingFragment)
                                    .load(EndPoints.BASE_URL +it.adminProfileState.value.image)
                                    .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                                    .circleCrop()
                                    .into(binding.profileImageRaiting)
                            }
                        }
                        when(it.userProfileState) {
                            is State.Error -> {
                                binding.loadingLayout.visibility = View.GONE
                                binding.contentLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.VISIBLE
                                binding.errorLayoutTv.text = it.userProfileState.error.message

                                binding.errorLayoutButton.setOnClickListener {
                                    viewModel.fetchUsers()
                                    viewModel.loadUserProfile()
                                }
                            }
                            State.Loading -> {
                                binding.loadingLayout.visibility = View.VISIBLE
                                binding.contentLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                if(it.userType == UserType.Normal) {
                                    binding.profileYou.text = "Вы, "
                                    binding.profileNameRaiting.text = it.userProfileState.value.name
                                    Glide.with(this@RatingFragment)
                                        .load(EndPoints.BASE_URL +it.userProfileState.value.image)
                                        .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                                        .circleCrop()
                                        .into(binding.profileImageRaiting)

                                    binding.isAdminTv.visibility = View.GONE
                                    binding.raitingTv.visibility = View.VISIBLE
                                    binding.premiumIvRating.visibility = View.GONE
                                    binding.raitingTv.text="#"+it.userProfileState.value.rating
                                }
                                else {
                                    binding.profileYou.text = "Вы, "
                                    binding.profileNameRaiting.text = it.userProfileState.value.name
                                    Glide.with(this@RatingFragment)
                                        .load(com.example.data.network.EndPoints.BASE_URL +it.userProfileState.value.image)
                                        .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                                        .circleCrop()
                                        .into(binding.profileImageRaiting)

                                    binding.isAdminTv.visibility = View.GONE
                                    binding.raitingTv.visibility = View.VISIBLE
                                    binding.premiumIvRating.visibility = View.GONE
                                    binding.raitingTv.text="#"+it.userProfileState.value.rating
                                }
                            }
                        }
                        when(it.users) {
                            is State.Error -> {
                                binding.loadingLayout.visibility = View.GONE
                                binding.contentLayout.visibility = View.VISIBLE
                                binding.errorLayout.visibility = View.GONE
                                showSnackbar(it.users.error.message.toString(), requireActivity().findViewById(R.id.rootViewMain))
                            }
                            State.Loading -> {
                                binding.loadingLayout.visibility = View.VISIBLE
                                binding.contentLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                ratingAdapter.submitList(it.users.value)
                            }
                        }
                        when(it.operationState) {
                            is State.Error -> {
                                binding.loadingLayout.visibility = View.GONE
                                binding.contentLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.VISIBLE
                                binding.errorLayoutTv.text = it.operationState.error.message

                                binding.errorLayoutButton.setOnClickListener {
                                    viewModel.fetchUsers()
                                    viewModel.loadUserProfile()
                                }
                            }
                            State.Loading -> {
                                binding.loadingLayout.visibility = View.VISIBLE
                                binding.contentLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                binding.loadingLayout.visibility = View.GONE
                                binding.contentLayout.visibility = View.VISIBLE
                                binding.errorLayout.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

}