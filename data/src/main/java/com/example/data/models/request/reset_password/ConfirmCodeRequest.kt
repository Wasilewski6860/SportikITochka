package com.example.data.models.request.reset_password

import com.google.gson.annotations.SerializedName

data class ConfirmCodeRequest(
    @SerializedName("code_sent") val code1: String,
    @SerializedName("received_code") val code2: String,
)