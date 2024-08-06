package com.example.data.models.request.user_data

import com.google.gson.annotations.SerializedName
import java.io.File

data class ChangeDataUserRequest(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: File,
    @SerializedName("weight") val weight: Int,
    @SerializedName("phone") val phone: String,
    @SerializedName("birthday") val birthday: String
)