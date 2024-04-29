package com.example.sportikitochka.ui

import android.content.Context
import android.widget.TextView
import com.example.sportikitochka.R
import com.example.sportikitochka.data.models.response.statistic.SportActivityStatistic
import com.example.sportikitochka.databinding.MarkerViewBinding
import com.example.sportikitochka.other.TrackingUtility
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomMarkerView(
    val runs: List<SportActivityStatistic>,
    c: Context,
    layoutId: Int
) : MarkerView(c, layoutId) {

    private var _binding: MarkerViewBinding? = null
    private val binding get() = _binding!!

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if(e == null) {
            return
        }
        val curRunId = e.x.toInt()
        val run = runs[curRunId]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        findViewById<TextView>(R.id.tvDate).text = dateFormat.format(calendar.time)

        val avgSpeed = "${run.avgSpeed}km/h"
        findViewById<TextView>(R.id.tvAvgSpeed).text = avgSpeed

        val distanceInKm = "${run.distanceInMeters / 1000f}km"
        findViewById<TextView>(R.id.tvDistance).text = distanceInKm

        findViewById<TextView>(R.id.tvDuration).text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

        val caloriesBurned = "${run.caloriesBurned}kcal"
        findViewById<TextView>(R.id.tvCaloriesBurned).text = caloriesBurned
    }
}