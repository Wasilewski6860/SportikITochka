package com.example.data.models.response.admin_action

import com.google.gson.annotations.SerializedName

data class AdminActionResponse(

    @SerializedName("success")
    var success: Boolean,
    @SerializedName("action")
    var action: String,
) {
    fun isSuccess(): Boolean = success
}