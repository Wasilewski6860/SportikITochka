package com.example.sportikitochka.data.models.request.statistic

import com.google.gson.annotations.SerializedName

data class StatisticsRequest (
    @SerializedName("period")
    val period: String //week, month, year, all time
)

