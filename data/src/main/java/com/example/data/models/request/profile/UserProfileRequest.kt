package com.example.data.models.request.profile

import com.google.gson.annotations.SerializedName

data class UserProfileRequest (
    @SerializedName("period")
    val period: String //week, month, year, all time
)


enum class ProfilePeriod(val period: String) {
    WEEK("week"),
    MONTH("month"),
    YEAR("year"),
    ALL_TIME("all_time");

    override fun toString(): String {
        return period
    }


}