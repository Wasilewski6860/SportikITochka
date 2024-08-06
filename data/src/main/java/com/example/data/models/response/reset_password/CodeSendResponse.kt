package com.example.data.models.response.reset_password

import com.google.gson.annotations.SerializedName

data class CodeSendResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("code_sent")
    val code: String
)