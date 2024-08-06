package com.example.data.models.request.user_data

import com.google.gson.annotations.SerializedName
import java.io.File

data class ChangeAdminDataRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: File,
    @SerializedName("phone")// В метрах
    val phone: String,
    @SerializedName("birthday")
    val birthday: String
)