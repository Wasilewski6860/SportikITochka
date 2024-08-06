package com.example.data.models.response.reset_password

import com.google.gson.annotations.SerializedName

data class ResetBaseResponse(
    @SerializedName("success")
    val success: Boolean
)