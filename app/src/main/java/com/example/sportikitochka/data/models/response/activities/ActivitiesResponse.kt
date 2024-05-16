package com.example.sportikitochka.data.models.response.activities

import com.google.gson.annotations.SerializedName

data class ActivitiesResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("activities") val activities: InnerActivities,
)

data class InnerActivities(
    @SerializedName("activities") val activities: List<ActivityResponse>,
)