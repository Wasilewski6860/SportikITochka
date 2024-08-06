package com.example.data.models.request.reset_password

import com.google.gson.annotations.SerializedName
import java.io.File

data class ResetPasswordRequest(
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("confirm_password") val confirmPassword: String
)