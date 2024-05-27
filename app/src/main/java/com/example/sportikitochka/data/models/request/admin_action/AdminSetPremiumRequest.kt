package com.example.sportikitochka.data.models.request.admin_action

import com.google.gson.annotations.SerializedName

data class AdminSetPremiumRequest(

    @SerializedName("user_id")
    var user_id: String,

)