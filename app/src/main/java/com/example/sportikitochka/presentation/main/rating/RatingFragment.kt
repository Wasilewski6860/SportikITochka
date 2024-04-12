package com.example.sportikitochka.presentation.main.rating

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportikitochka.R
import com.example.sportikitochka.data.models.response.auth.UserType
import com.example.sportikitochka.databinding.FragmentRatingBinding
import com.example.sportikitochka.domain.models.User
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
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

    private var userRole : UserType = UserType.Normal


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

        userRole = viewModel.getUserRole() ?: UserType.Normal

        connectionLiveData.observe(viewLifecycleOwner) {
            isOnline = it
            if (it) {
                viewModel.loadUserProfile()
            }
            else {
                showSnackbar("Произошла ошибка при интернет-соединении", requireActivity().findViewById(R.id.rootView))
            }
        }

        viewModel.userInfo.observe(viewLifecycleOwner) {
            binding.profileYou.text = "Вы, "
            binding.profileNameRaiting.text = it.name
            when(userRole) {
                is UserType.Normal -> {
                    binding.isAdminTv.visibility = View.GONE
                    binding.raitingTv.visibility = View.VISIBLE
                    binding.premiumIvRating.visibility = View.GONE
                    binding.raitingTv.text="#"+it.rating
                }
                is UserType.Premium -> {
                    binding.raitingTv.visibility = View.VISIBLE
                    binding.raitingTv.text="#"+it.rating
                    binding.premiumIvRating.visibility = View.VISIBLE
                }
                is UserType.Admin -> {
                    binding.isAdminTv.visibility = View.VISIBLE
                    binding.premiumIvRating.visibility = View.GONE
                    binding.raitingTv.visibility = View.GONE
                    binding.isAdminTv.text = "Вы админ"
                }
                else -> Unit
            }
        }


        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                ScreenRatingState.Loading -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                }
                ScreenRatingState.ProfileLoaded -> {
                    viewModel.fetchUsers()
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                }
                ScreenRatingState.ProfileError, ScreenRatingState.UsersLoadingError -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                    binding.errorLayoutTv.text = "Не удалось загрузить данные. Попробуйте еще раз"

                    binding.errorLayoutButton.setOnClickListener {
                        viewModel.loadUserProfile()
                    }
                }
                ScreenRatingState.Empty -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                    showSnackbar("Тут пока ничего нет", requireActivity().findViewById(R.id.rootView))
                }
                ScreenRatingState.AdminActionError -> {
                    showSnackbar("Не получилось выполнить операцию", requireActivity().findViewById(R.id.rootView))
                }
                else -> Unit
            }
        }

        viewModel.users.observe(viewLifecycleOwner) {
            ratingAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() = binding.recycler.apply {
        ratingAdapter = RatingAdapter(
            userRole == UserType.Admin,
            object : RatingAdapter.UserPremiumActionListener {
                override fun onClickItem(user: User) {
                    if (isOnline){
                        if (user.role == UserType.Normal) viewModel.setPremium(user.id)
                        else viewModel.removePremium(user.id)
                    }
                    else {
                        showSnackbar("Нет интернет-соединения", requireActivity().findViewById(R.id.rootView))
                    }
                }
            },
            object : RatingAdapter.UserBlockActionListener {
                override fun onClickItem(user: User) {
                    if (isOnline) viewModel.blockUser(user)
                    else showSnackbar("Нет интернет-соединения", requireActivity().findViewById(R.id.rootView))
                }
            }
        )
        adapter = ratingAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

}