package com.example.sportikitochka.presentation.main.rating

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.sportikitochka.R
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.data.network.EndPoints
import com.example.sportikitochka.databinding.FragmentRatingBinding
import com.example.sportikitochka.domain.models.User
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import io.appmetrica.analytics.AppMetrica
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

        when(viewModel.getType()) {
            is UserType.Normal -> {
                binding.isAdminTv.visibility = View.GONE
                binding.raitingTv.visibility = View.VISIBLE
                binding.premiumIvRating.visibility = View.GONE

                viewModel.userInfo.observe(viewLifecycleOwner) {
                    binding.profileYou.text = "Вы, "
                    binding.profileNameRaiting.text = it.name


                    Glide.with(this@RatingFragment)
                        .load(EndPoints.BASE_URL +it.image)
                        .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                        .circleCrop()
                        .into(binding.profileImageRaiting)

                    binding.isAdminTv.visibility = View.GONE
                    binding.raitingTv.visibility = View.VISIBLE
                    binding.premiumIvRating.visibility = View.GONE
                    binding.raitingTv.text="#"+it.rating
                }
            }
            is UserType.Premium -> {
                binding.raitingTv.visibility = View.VISIBLE
                binding.premiumIvRating.visibility = View.VISIBLE
                viewModel.userInfo.observe(viewLifecycleOwner) {
                    binding.profileYou.text = "Вы, "
                    binding.profileNameRaiting.text = it.name


                    Glide.with(this@RatingFragment)
                        .load(EndPoints.BASE_URL +it.image)
                        .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                        .circleCrop()
                        .into(binding.profileImageRaiting)

                    binding.isAdminTv.visibility = View.GONE
                    binding.raitingTv.visibility = View.VISIBLE
                    binding.premiumIvRating.visibility = View.GONE
                    binding.raitingTv.text="#"+it.rating
                }
            }
            is UserType.Admin -> {
                binding.isAdminTv.visibility = View.VISIBLE
                binding.premiumIvRating.visibility = View.GONE
                binding.raitingTv.visibility = View.GONE
                binding.isAdminTv.text = "Вы админ"
                viewModel.adminInfo.observe(viewLifecycleOwner) {
                    binding.profileYou.text = "Вы, "
                    binding.profileNameRaiting.text = it.name

                    Glide.with(this@RatingFragment)
                        .load(EndPoints.BASE_URL +it.image)
                        .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                        .circleCrop()
                        .into(binding.profileImageRaiting)
                }
            }
            else -> Unit
        }



        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                ScreenRatingState.Loading -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                }
                ScreenRatingState.SuccessRemote -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                }
                is ScreenRatingState.ErrorInfo -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE

                    showSnackbar(it.message, requireActivity().findViewById(R.id.rootViewMain))
                }
                is ScreenRatingState.ErrorBlock -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                    binding.errorLayoutTv.text = it.message

                    binding.errorLayoutButton.setOnClickListener {
                        viewModel.fetchUsers()
                        viewModel.loadUserProfile()
                    }
                }
                ScreenRatingState.Empty -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                    showSnackbar("Пустовато как-то(добавить экран)", requireActivity().findViewById(R.id.rootViewMain))
                }
            }
        }

        viewModel.users.observe(viewLifecycleOwner) {
            ratingAdapter.submitList(it)
        }
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

}