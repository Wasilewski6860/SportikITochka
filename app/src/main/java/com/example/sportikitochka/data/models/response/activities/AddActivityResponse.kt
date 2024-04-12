package com.example.sportikitochka.data.models.response.activities

import com.google.gson.annotations.SerializedName

data class AddActivityResponse(
    @SerializedName("success")
    var success : Boolean,
    @SerializedName("activity_id")
    var activityId : Int
)