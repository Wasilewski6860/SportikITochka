package com.example.sportikitochka.presentation.main.main

import android.content.Intent
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
import com.example.data.network.EndPoints.BASE_URL
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectInputException
import com.example.data.network.error.IncorrectPasswordException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UnknownException
import com.example.data.network.error.UserBlockedException
import com.example.domain.coroutines.Response
import com.example.sportikitochka.common.State
import com.example.sportikitochka.databinding.FragmentMainBinding
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import com.example.sportikitochka.presentation.main.MainActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModel()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainAdapter: MainAdapter
    protected lateinit var connectionLiveData: ConnectionLiveData

    private var isOnline : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this.requireContext())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        connectionLiveData.observe(viewLifecycleOwner)  {
            isOnline = it
            with(binding) {
                if (!it) {
                    tvStartNewActivity.text = "Нет интернет-соединения, начать новую активность невозможно"
                    startNewActivityButton.visibility = View.GONE
                    newActivityCard.setOnClickListener{}
                }
                else {
                    tvStartNewActivity.text = "Начать новую активность"
                    startNewActivityButton.visibility = View.VISIBLE
                    newActivityCard.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_mainFragment_to_selectActivityTypeFragment,
                            savedInstanceState
                        )
                    }
                }
            }
        }
        viewModel.fetchActivities()
        viewModel.loadUserData()
        setupRecyclerView()
        initListeners()
        setupObservers()
    }


    override fun onResume() {
        super.onResume()
        viewModel.fetchActivities()
    }

    private fun setupRecyclerView() = binding.recycler.apply {
        mainAdapter = MainAdapter(requireContext())
        adapter = mainAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListeners() = with(binding) {
        allActivities.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_allActivitiesFragment
            )
        }
        startNewActivityButton.setOnClickListener {
            if (isOnline) {
                newActivityCard.setOnClickListener{
                    findNavController().navigate(
                        R.id.action_mainFragment_to_selectActivityTypeFragment
                    )
                }
            }
            else showSnackbar("Нет интернет-соединения", requireActivity().findViewById(R.id.rootViewMain))
        }
        emptyLayoutButton.setOnClickListener {
            if (isOnline) {
                findNavController().navigate(
                    R.id.action_mainFragment_to_selectActivityTypeFragment
                )
            }
            else showSnackbar("Нет интернет-соединения", requireActivity().findViewById(R.id.rootViewMain))
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect {
                    with(binding) {
                        when(it.userProfileState) {
                            is State.Error -> {
                                loadingLayout.visibility = View.GONE
                                contentLayout.visibility = View.GONE
                                errorLayout.visibility = View.VISIBLE
                                emptyLayout.visibility = View.GONE

                                errorLayoutButton.setOnClickListener {
                                    viewModel.loadUserData()
                                }

                                when(it.userProfileState.error) {
                                    is IncorrectTokenException -> {
                                        errorLayoutTv.text = "Время сессии истекло, пожалуйста, перезайдите в приложение"
                                    }
                                    is ForbiddenException-> {
                                        errorLayoutTv.text = "Нет доступа"
                                    }
                                    is NotFoundException-> {
                                        errorLayoutTv.text = "Пользователь не найден"
                                    }
                                    else -> {
                                        errorLayoutTv.text = "К сожалению, загрузить ваш профиль. Пожалуйста, попробуйте позже"
                                    }
                                }
                            }
                            State.Loading -> {
                                loadingLayout.visibility = View.VISIBLE
                                contentLayout.visibility = View.GONE
                                errorLayout.visibility = View.GONE
                                emptyLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                loadingLayout.visibility = View.VISIBLE
                                contentLayout.visibility = View.GONE
                                errorLayout.visibility = View.GONE
                                emptyLayout.visibility = View.GONE

                                profileName.text = it.userProfileState.value.name

                                Glide.with(this@MainFragment)
                                    .load(BASE_URL+it.userProfileState.value.image)
                                    .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                                    .circleCrop()
                                    .into(profileImage)
                            }
                        }
                        when(it.activitiesState) {
                            is State.Error -> {
                                loadingLayout.visibility = View.GONE
                                contentLayout.visibility = View.GONE
                                errorLayout.visibility = View.VISIBLE
                                emptyLayout.visibility = View.GONE
                                errorLayoutTv.text = "К сожалению, загрузить ваши активности не получилось. Пожалуйста, попробуйте позже"

                                errorLayoutButton.setOnClickListener {
                                    viewModel.fetchActivities()
                                }

                            }
                            State.Loading -> {
                                loadingLayout.visibility = View.VISIBLE
                                contentLayout.visibility = View.GONE
                                errorLayout.visibility = View.GONE
                                emptyLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {

                                loadingLayout.visibility = View.GONE
                                contentLayout.visibility = View.VISIBLE
                                errorLayout.visibility = View.GONE
                                emptyLayout.visibility = View.GONE

                                val data = it.activitiesState.value
                                if (data.isEmpty()) {
                                    loadingLayout.visibility = View.GONE
                                    contentLayout.visibility = View.GONE
                                    errorLayout.visibility = View.GONE
                                    emptyLayout.visibility = View.VISIBLE
                                }
                                else
                                if (data.size<4)mainAdapter.submitList(data.subList(0,data.size))
                                else mainAdapter.submitList(data.subList(0,4))
                            }
                        }
                    }
                }
            }
        }
    }
}