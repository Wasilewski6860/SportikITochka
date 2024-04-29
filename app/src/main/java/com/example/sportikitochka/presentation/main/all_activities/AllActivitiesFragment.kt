package com.example.sportikitochka.presentation.main.all_activities

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportikitochka.R
import com.example.sportikitochka.databinding.FragmentAllActivitiesBinding
import com.example.sportikitochka.other.ConnectionLiveData
import com.example.sportikitochka.other.TrackingUtility.showSnackbar
import com.example.sportikitochka.presentation.main.main.MainAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class AllActivitiesFragment : Fragment() {

    companion object {
        fun newInstance() = AllActivitiesFragment()
    }

    private val viewModel: AllActivitiesViewModel by viewModel()
    private var _binding: FragmentAllActivitiesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainAdapter: MainAdapter

    protected lateinit var connectionLiveData: ConnectionLiveData

    private var isOnline : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllActivitiesBinding.inflate(layoutInflater, container, false)
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
                showSnackbar("Нет интернет-соединения", requireActivity().findViewById(R.id.rootViewMain))
            }
        }

        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                ScreenAllActivitiesState.Loading -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    binding.emptyLayout.visibility = View.GONE
                }
                ScreenAllActivitiesState.SuccessRemote -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                    binding.emptyLayout.visibility = View.GONE
                }
                ScreenAllActivitiesState.ErrorRemote -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                    binding.emptyLayout.visibility = View.GONE
                    showSnackbar("Произошла ошибка при интернет-соединении", requireActivity().findViewById(R.id.rootViewMain))
                }
                is ScreenAllActivitiesState.Error -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                    binding.emptyLayout.visibility = View.GONE
                    binding.errorLayoutTv.text = it.message

                    binding.errorLayoutButton.setOnClickListener {
                        viewModel.fetchActivities()
                    }
                }
                ScreenAllActivitiesState.Empty -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    binding.emptyLayout.visibility = View.VISIBLE
                    binding.emptyLayoutButton.setOnClickListener {
                        if (isOnline) {
                            binding.emptyLayoutButton.setOnClickListener{
                                findNavController().navigate(
                                    R.id.action_allActivitiesFragment_to_selectActivityTypeFragment,
                                    savedInstanceState
                                )
                            }
                        }
                        else showSnackbar("Нет интернет-соединения", requireActivity().findViewById(R.id.rootViewMain))
                    }
                }
            }
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
            mainAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() = binding.recyclerAllActivities.apply {
        mainAdapter = MainAdapter()
        adapter = mainAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

}