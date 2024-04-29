package com.example.sportikitochka.presentation.main.main

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.FragmentMainBinding
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
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

        connectionLiveData.observe(viewLifecycleOwner) {
            isOnline = it
            viewModel.fetchActivities()

            if (!it) {
                binding.tvStartNewActivity.text = "Нет интернет-соединения, начать новую активность невозможно"
                binding.startNewActivityButton.visibility = View.GONE
                binding.newActivityCard.setOnClickListener{}
            }
            else {
                binding.tvStartNewActivity.text = "Начать новую активность"
                binding.startNewActivityButton.visibility = View.VISIBLE
                binding.newActivityCard.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_mainFragment_to_selectActivityTypeFragment,
                        savedInstanceState
                    )
                }
            }
        }
        viewModel.loadUserProfile()
        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                ScreenMainState.Loading -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    binding.emptyLayout.visibility = View.GONE
                }
                ScreenMainState.ProfileLoaded -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    binding.emptyLayout.visibility = View.GONE
                }
                ScreenMainState.ActivitiesLoadedRemote -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                    binding.emptyLayout.visibility = View.GONE
                }
                ScreenMainState.ActivitiesLoadedLocal -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                    binding.emptyLayout.visibility = View.GONE
                    showSnackbar("Произошла ошибка при интернет-соединении", requireActivity().findViewById(R.id.rootViewMain))
                }
                ScreenMainState.ErrorActivities -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                    binding.emptyLayout.visibility = View.GONE
                    binding.errorLayoutTv.text = "К сожалению, загрузить ваши активности не получилось. Пожалуйста, попробуйте позже"

                    binding.errorLayoutButton.setOnClickListener {
                        viewModel.fetchActivities()
                    }
                }
                ScreenMainState.ProfileLoadingError -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                    binding.emptyLayout.visibility = View.GONE
                    binding.errorLayoutTv.text = "К сожалению, загрузить ваш профиль. Пожалуйста, попробуйте позже"

                    binding.errorLayoutButton.setOnClickListener {
                        viewModel.loadUserProfile()
                    }
                }
                ScreenMainState.Empty -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    binding.emptyLayout.visibility = View.VISIBLE

                }
            }
        }

        viewModel.userProfile.observe(viewLifecycleOwner) {
            binding.profileName.text = it.name
            val decodedString: ByteArray? = Base64.decode(it.image, Base64.DEFAULT)

            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString?.size ?: 0)
            binding.profileImage.setImageBitmap(bitmap)
        }

        binding.profileHello.text = "Привет, "
        binding.tvStartNewActivity.text = "Начать новую активность"
        binding.recentActivity.text = "Последние активности"
        binding.allActivities.text = "Все"


        binding.startNewActivityButton.setOnClickListener {
            if (isOnline) {
                binding.newActivityCard.setOnClickListener{
                    findNavController().navigate(
                        R.id.action_mainFragment_to_selectActivityTypeFragment,
                        savedInstanceState
                    )
                }
            }
            else showSnackbar("Нет интернет-соединения", requireActivity().findViewById(R.id.rootViewMain))
        }
        binding.allActivities.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_allActivitiesFragment,
                savedInstanceState
            )
        }
        setupRecyclerView()



//        val activities: List<SportActivity> = listOf(
//            SportActivity(
//                id = 0,
//                img = "",
//                timestamp = Calendar.getInstance().timeInMillis,
//                avgSpeedInKMH = 14.4F,
//                distanceInMeters = 10321,
//                timeInMillis = (212141L)*20,
//                caloriesBurned = 101,
//                activityType = ActivityType.RUNNING
//            ),
//            SportActivity(
//                id = 0,
//                img = "",
//                timestamp = Calendar.getInstance().timeInMillis - 10000000000,
//                avgSpeedInKMH = 13.4F,
//                distanceInMeters = 10380,
//                timeInMillis = 212141L*20,
//                caloriesBurned = 100,
//                activityType = ActivityType.BYCICLE
//            ),
//            SportActivity(
//                id = 1,
//                img = "",
//                timestamp = Calendar.getInstance().timeInMillis-14000000000,
//                avgSpeedInKMH = 15.4F,
//                distanceInMeters = 9321,
//                timeInMillis = 112141L*20,
//                caloriesBurned = 120,
//                activityType = ActivityType.SWIMMING
//            ),
//            SportActivity(
//                id = 1,
//                img = "",
//                timestamp = Calendar.getInstance().timeInMillis - 19000000000,
//                avgSpeedInKMH = 15.4F,
//                distanceInMeters = 8354,
//                timeInMillis = 112141L*20,
//                caloriesBurned = 132,
//                activityType = ActivityType.SWIMMING
//            ),
//        )

        viewModel.activities.observe(viewLifecycleOwner) {
            if (it.size<4)mainAdapter.submitList(it.subList(0,it.size))
            else mainAdapter.submitList(it.subList(0,4))
        }
    }

    private fun setupRecyclerView() = binding.recycler.apply {
        mainAdapter = MainAdapter()
        adapter = mainAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

}