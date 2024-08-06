package com.example.sportikitochka.presentation.main.statistics

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.sportikitochka.R
import com.example.data.models.request.profile.ProfilePeriod
import com.example.domain.models.AdminStatistic
import com.example.domain.models.PremiumStatistic
import com.example.domain.models.UserType
import com.example.sportikitochka.common.State
import com.example.sportikitochka.databinding.FragmentStatisticBinding
import com.example.sportikitochka.other.TrackingUtility
import com.example.sportikitochka.ui.CustomMarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.round

class StatisticFragment : Fragment() {

    companion object {
        fun newInstance() = StatisticFragment()
    }

    private val viewModel: StatisticsViewModel by viewModel()

    private var _binding: FragmentStatisticBinding? = null
    private val binding get() = _binding!!

    private lateinit var userType: UserType
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        userType = viewModel.getType()
        binding.tvGetPremiumStatistic.setOnClickListener {
            findNavController().navigate(
                R.id.action_statisticFragment_to_paymentFragment,
                savedInstanceState
            )
        }
        setContentVisible()
        when(userType) {
            UserType.Admin -> {
                viewModel.fetchAdminStatistic(ProfilePeriod.WEEK)
            }
            UserType.Premium -> {
                viewModel.fetchPremiumStatistic(ProfilePeriod.WEEK)
                setupBarChartPremium()
            }
            else -> Unit
        }
        initObservers()

    }

    private fun setContentVisible() {
        binding.loadingLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        when(userType) {
            UserType.Admin -> {
                AppMetrica.reportEvent("Statistics admin viewed")
                binding.containerStatisticAdmin.visibility = View.VISIBLE
                binding.containerStatistic.visibility = View.GONE
                binding.containerData.visibility = View.VISIBLE
                binding.barChart.visibility = View.GONE
                binding.lineChart.visibility = View.VISIBLE
                binding.containerNoAccess.visibility = View.GONE
            }
            UserType.Normal -> {
                AppMetrica.reportEvent("Statistics block viewed")
                binding.containerStatisticAdmin.visibility = View.GONE
                binding.containerStatistic.visibility = View.GONE
                binding.containerData.visibility = View.GONE
                binding.containerNoAccess.visibility = View.VISIBLE
            }
            UserType.Premium -> {
                AppMetrica.reportEvent("Statistics premium viewed")
                binding.containerStatisticAdmin.visibility = View.GONE
                binding.containerStatistic.visibility = View.VISIBLE
                binding.containerData.visibility = View.VISIBLE
                binding.barChart.visibility = View.VISIBLE
                binding.lineChart.visibility = View.GONE
                binding.containerNoAccess.visibility = View.GONE
            }
        }
    }

    private fun setupBarChartPremium() = with(binding) {
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = R.color.black
            textColor = R.color.black
            setDrawGridLines(false)
        }
        barChart.axisLeft.apply {
            axisLineColor = R.color.black
            textColor = R.color.black
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            axisLineColor = R.color.black
            textColor = R.color.black
            setDrawGridLines(false)
        }
        barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }

    private fun setPremiumData(premiumStatisticsResponse: PremiumStatistic) {
        val allAvgSpeeds = premiumStatisticsResponse.activities.indices.map { i -> BarEntry(i.toFloat(), premiumStatisticsResponse.activities[i].avgSpeed) }
        val bardataSet = BarDataSet(allAvgSpeeds, "Avg Speed Over Time").apply {
            valueTextColor = Color.WHITE
            color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        }
        binding.barChart.data = BarData(bardataSet)
        binding.barChart.marker = CustomMarkerView(premiumStatisticsResponse.activities.reversed(), requireContext(), R.layout.marker_view)
        binding.barChart.invalidate()
    }

    private fun setAdminData(adminStatisticsResponse: AdminStatistic) {
        // сохраняем список graphData в переменную
        val graphDataList = adminStatisticsResponse.graphData
        // создаем список Entry для премиум пользователей
        val premiumEntries = mutableListOf<Entry>()
        // создаем список Entry для обычных пользователей
        val regularEntries = mutableListOf<Entry>()
        // заполняем списки Entry данными из graphDataList
        for (i in 0 until graphDataList.size) {
            val graphData = graphDataList[i]
            premiumEntries.add(Entry(i.toFloat(), graphData.usersWithPremium.toFloat()))
            regularEntries.add(Entry(i.toFloat(), graphData.usersWithoutPremium.toFloat()))
        }
        // создаем LineDataSet для премиум пользователей
        val premiumDataSet = LineDataSet(premiumEntries, "Премиум пользователи")

        // настраиваем LineDataSet для премиум пользователей
        premiumDataSet.color = Color.RED
        premiumDataSet.lineWidth = 2f
        premiumDataSet.circleRadius = 4f
        premiumDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        premiumDataSet.valueTextSize = 12f

        // создаем LineDataSet для обычных пользователей
        val regularDataSet = LineDataSet(regularEntries, "Обычные пользователи")

// настраиваем LineDataSet для обычных пользователей
        regularDataSet.color = Color.BLUE
        regularDataSet.lineWidth = 2f
        regularDataSet.circleRadius = 4f
        regularDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        regularDataSet.valueTextSize = 12f

        // создаем LineData и добавляем в него оба LineDataSet
        val lineData = LineData(premiumDataSet, regularDataSet)


// настраиваем ось X
        val xAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.textColor = Color.BLACK
        xAxis.granularity = 1f
        xAxis.labelCount = graphDataList.size

// настраиваем ось Y
        val yAxis = binding.lineChart.axisLeft
        yAxis.textSize = 12f
        yAxis.textColor = Color.BLACK
        yAxis.granularity = 1f
        yAxis.axisMinimum = 0f

        // отключаем ось Y справа
        binding.lineChart.axisRight.isEnabled = false

        // отключаем легенду
        binding.lineChart.legend.isEnabled = false

        // устанавливаем LineData в LineChart
        binding.lineChart.data = lineData

        // обновляем LineChart
        binding.lineChart.invalidate()
    }

    fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect {
                    with(binding) {
                        when(it.adminState) {
                            is State.Error -> {
                                binding.loadingLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.VISIBLE
                            }
                            State.Loading -> {
                                binding.loadingLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                binding.totalUsersCountTv.text = it.adminState.value.totalUsers.toString()
                                binding.totalPremiumUsersCountTv.text = it.adminState.value.premiumUsers.toString()
                                setAdminData(it.adminState.value)
                            }
                        }
                        when(it.premiumState) {
                            is State.Error -> {
                                binding.loadingLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.VISIBLE
                            }
                            State.Loading -> {
                                binding.loadingLayout.visibility = View.GONE
                                binding.errorLayout.visibility = View.GONE
                            }
                            State.NotStarted -> Unit
                            is State.Success -> {
                                val km = it.premiumState.value.totalDistanceInMeters / 1000f
                                val totalDistance = round(km * 10f) / 10f
                                val totalDistanceString = "${totalDistance}km"
                                binding.tvTotalDistance.text = totalDistanceString

                                binding.tvTotalTime.text = TrackingUtility.getFormattedStopWatchTime(it.premiumState.value.totalTime)
                                binding.tvTotalCalories.text = it.premiumState.value.totalCalories.toString()
                                binding.tvAverageSpeed.text = it.premiumState.value.avgSpeed.toString()
                                setPremiumData(it.premiumState.value)
                            }
                        }
                    }
                }
            }
        }
    }
}